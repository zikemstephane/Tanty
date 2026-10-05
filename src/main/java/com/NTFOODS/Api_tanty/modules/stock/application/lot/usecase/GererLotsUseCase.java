package com.NTFOODS.Api_tanty.modules.stock.application.lot.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request.ConsommerLotsRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request.CreerLotRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.response.LotStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.mapper.LotStockMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.repository.LotStockRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case — suivi des lots de stock : déclaration, consultation, péremption
 * et sortie au plus proche de la péremption.
 *
 * <h3>Ce que ce use case ne fait pas</h3>
 * Il ne touche pas aux quantités de {@code niveaux_stock}. Le lot dit d'où
 * vient la marchandise et jusqu'à quand elle vaut ; le mouvement de stock dit
 * combien il en reste. Confondre les deux créerait deux vérités concurrentes
 * sur le même stock physique — et la seconde à s'écarter serait celle que
 * personne ne regarde.
 */
@Service
@Transactional
public class GererLotsUseCase {

    private static final Logger log = LoggerFactory.getLogger(GererLotsUseCase.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final LotStockRepository lotRepository;
    private final LotStockMapper mapper;

    public GererLotsUseCase(LotStockRepository lotRepository, LotStockMapper mapper) {
        this.lotRepository = lotRepository;
        this.mapper = mapper;
    }

    // ── Consultation ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LotStockResponse> lister() {
        return lotRepository.trouverTous().stream().map(mapper::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public LotStockResponse consulter(String numeroLot) {
        return mapper.versResponse(charger(numeroLot));
    }

    @Transactional(readOnly = true)
    public List<LotStockResponse> listerParProduit(String codeProduit) {
        return lotRepository.trouverParProduit(codeProduit).stream().map(mapper::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LotStockResponse> listerParSku(String sku) {
        return lotRepository.trouverParSku(sku).stream().map(mapper::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LotStockResponse> listerParEntrepot(String codeEntrepot) {
        return lotRepository.trouverParEntrepot(codeEntrepot).stream().map(mapper::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LotStockResponse> listerPerimantBientot(int jours) {
        if (jours <= 0) {
            throw new RegleMetierException("L'horizon d'alerte doit être d'au moins un jour : " + jours);
        }
        return lotRepository.trouverPerimantAvant(LocalDate.now().plusDays(jours))
            .stream().map(mapper::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LotStockResponse> listerPerimes() {
        return lotRepository.trouverPerimes().stream().map(mapper::versResponse).toList();
    }

    // ── Écriture ──────────────────────────────────────────────────────────

    public LotStockResponse creer(CreerLotRequest request) {
        String numero = (request.numeroLot() == null || request.numeroLot().isBlank())
            ? genererNumero()
            : request.numeroLot().trim();

        if (lotRepository.existeParNumero(numero)) {
            throw new RegleMetierException("Le lot " + numero + " existe déjà");
        }

        LotStock lot = LotStock.creer(numero, request.codeProduit(), request.sku(), request.designation(),
            request.codeFournisseur(), request.codeEntrepot(), request.dateFabrication(),
            request.datePeremption(), request.quantiteInitiale(), request.observations());

        LotStock sauvegarde = lotRepository.sauvegarder(lot);
        log.info("Lot {} déclaré — {} unité(s) de {} en {}", numero, request.quantiteInitiale(),
            request.codeProduit(), request.codeEntrepot());
        return mapper.versResponse(sauvegarde);
    }

    /**
     * Prélève {@code quantite} sur les lots consommables, du plus proche de la
     * péremption au plus lointain. Si le total disponible ne suffit pas, rien
     * n'est prélevé : la transaction échoue en bloc plutôt que de laisser une
     * consommation à moitié faite.
     */
    public List<LotStockResponse> consommer(ConsommerLotsRequest request) {
        List<LotStock> candidats = lotRepository.trouverConsommables(request.sku(), request.codeEntrepot());

        long disponible = candidats.stream()
            .filter(l -> !l.estPerime())
            .mapToLong(LotStock::getQuantiteRestante)
            .sum();
        if (disponible < request.quantite()) {
            throw new RegleMetierException(
                "Les lots de " + request.sku() + " en " + request.codeEntrepot() + " ne totalisent que "
                    + disponible + " unité(s) consommable(s), " + request.quantite() + " demandée(s)");
        }

        long reste = request.quantite();
        List<LotStockResponse> touches = new ArrayList<>();
        for (LotStock lot : candidats) {
            if (reste <= 0) break;
            if (lot.estPerime()) continue;
            long prise = Math.min(reste, lot.getQuantiteRestante());
            lot.consommer(prise);
            touches.add(mapper.versResponse(lotRepository.sauvegarder(lot)));
            reste -= prise;
        }

        log.info("{} unité(s) de {} prélevée(s) sur {} lot(s) en {}{}",
            request.quantite(), request.sku(), touches.size(), request.codeEntrepot(),
            request.motif() != null ? " — " + request.motif() : "");
        return touches;
    }

    public LotStockResponse changerStatut(String numeroLot, StatutLot statut) {
        LotStock lot = charger(numeroLot);
        lot.changerStatut(statut);
        return mapper.versResponse(lotRepository.sauvegarder(lot));
    }

    public LotStockResponse retirer(String numeroLot) {
        LotStock lot = charger(numeroLot);
        lot.retirer();
        return mapper.versResponse(lotRepository.sauvegarder(lot));
    }

    /**
     * Passe en PERIME les lots dont la date est atteinte et qui ne le sont pas
     * encore déclarés. Rendu comme opération explicite plutôt que comme tâche
     * planifiée : un lot périmé doit disparaître des sorties possibles, et le
     * domaine refuse déjà de le consommer — ceci ne fait qu'aligner le statut
     * affiché sur la réalité.
     */
    public int marquerPerimes() {
        List<LotStock> perimes = lotRepository.trouverPerimes().stream()
            .filter(l -> l.getStatut() != StatutLot.PERIME
                && l.getStatut() != StatutLot.EPUISE
                && l.getStatut() != StatutLot.RETIRE)
            .toList();
        perimes.forEach(l -> {
            l.marquerPerime();
            lotRepository.sauvegarder(l);
        });
        if (!perimes.isEmpty()) {
            log.info("{} lot(s) passé(s) en périmé", perimes.size());
        }
        return perimes.size();
    }

    private LotStock charger(String numeroLot) {
        return lotRepository.trouverParNumero(numeroLot)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Lot", numeroLot));
    }

    private String genererNumero() {
        String base = "LOT-" + FMT.format(LocalDate.now()) + "-";
        for (int suffixe = 1; suffixe <= 9999; suffixe++) {
            String candidat = base + String.format("%04d", suffixe);
            if (!lotRepository.existeParNumero(candidat)) return candidat;
        }
        throw new RegleMetierException("Plus de numéro de lot disponible pour aujourd'hui");
    }
}
