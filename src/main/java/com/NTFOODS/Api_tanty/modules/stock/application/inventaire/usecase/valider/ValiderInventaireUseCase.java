package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.mapper.InventaireMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.LigneInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — validation d'un inventaire par le DG. Pour chaque ligne comptée
 * présentant un écart, le stock est ajusté à la quantité réellement comptée et
 * un mouvement AJUSTEMENT_INVENTAIRE est enregistré.
 */
@Service
@Transactional
public class ValiderInventaireUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final InventaireRepository inventaireRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final InventaireMapper inventaireMapper;
    private final EventPublisher eventPublisher;

    public ValiderInventaireUseCase(InventaireRepository inventaireRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository,
                                    InventaireMapper inventaireMapper, EventPublisher eventPublisher) {
        this.inventaireRepository = inventaireRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.inventaireMapper = inventaireMapper;
        this.eventPublisher = eventPublisher;
    }

    public InventaireResponse executer(String numero, String justification, Utilisateur acteur) {
        SessionInventaire session = inventaireRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Inventaire", numero));

        session.valider(acteur.getMatricule().getValeur(), acteur.getRole(), justification);

        String entrepot = session.getCodeEntrepot();
        for (LigneInventaire ligne : session.getLignes()) {
            if (!ligne.estComptee()) continue;

            NiveauStock niveau = niveauStockRepository.trouver(ligne.getCodeProduit(), entrepot)
                .orElseGet(() -> NiveauStock.creer(ligne.getCodeProduit(), entrepot, niveauTypeFallback(entrepot)));

            long delta = ligne.getQuantiteComptee() - niveau.getQuantiteEnBase();
            if (delta == 0) continue;

            if (delta > 0) {
                niveau.entrer(delta);
            } else {
                niveau.sortir(-delta);
            }
            niveauStockRepository.sauvegarder(niveau);

            MouvementStock mouvement = MouvementStock.enregistrer(genererReferenceMouvement(),
                TypeMouvement.AJUSTEMENT_INVENTAIRE, ligne.getCodeProduit(), null, Math.abs(delta),
                delta < 0 ? entrepot : null, delta > 0 ? entrepot : null,
                acteur.getMatricule().getValeur(), session.getNumero(),
                "Ajustement inventaire " + session.getNumero());
            mouvementRepository.enregistrer(mouvement);
        }

        SessionInventaire sauvegarde = inventaireRepository.sauvegarder(session);
        session.getDomainEvents().forEach(eventPublisher::publish);
        session.clearDomainEvents();
        return inventaireMapper.versResponse(sauvegarde);
    }

    private com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot niveauTypeFallback(String entrepot) {
        if (entrepot.startsWith("MOBILE-")) return com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot.MOBILE;
        return com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot.CENTRAL;
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
