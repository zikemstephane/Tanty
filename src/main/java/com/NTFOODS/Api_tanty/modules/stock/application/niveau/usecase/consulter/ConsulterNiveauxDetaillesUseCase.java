package com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockDetailResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Use Case — état des stocks ENRICHI (produit + entrepôt + seuil + niveau
 * d'alerte + valorisation). Sert les écrans Inventaire et Alertes du web.
 */
@Service
@Transactional(readOnly = true)
public class ConsulterNiveauxDetaillesUseCase {

    private final NiveauStockRepository niveauStockRepository;
    private final ProduitRepository produitRepository;
    private final SeuilStockRepository seuilStockRepository;
    private final EntrepotRepository entrepotRepository;

    public ConsulterNiveauxDetaillesUseCase(NiveauStockRepository niveauStockRepository,
                                            ProduitRepository produitRepository,
                                            SeuilStockRepository seuilStockRepository,
                                            EntrepotRepository entrepotRepository) {
        this.niveauStockRepository = niveauStockRepository;
        this.produitRepository = produitRepository;
        this.seuilStockRepository = seuilStockRepository;
        this.entrepotRepository = entrepotRepository;
    }

    /** Tous les niveaux (filtre optionnel par code entrepôt). */
    public List<NiveauStockDetailResponse> lister(String codeEntrepot) {
        List<NiveauStock> niveaux = (codeEntrepot != null && !codeEntrepot.isBlank())
            ? niveauStockRepository.trouverParEntrepot(codeEntrepot.trim().toUpperCase())
            : niveauStockRepository.trouverTous();
        return enrichir(niveaux);
    }

    /** Niveaux d'un entrepôt désigné par son id numérique. */
    public List<NiveauStockDetailResponse> listerParEntrepotId(Long entrepotId) {
        return entrepotRepository.trouverTous().stream()
            .filter(e -> entrepotId.equals(e.getId()))
            .findFirst()
            .map(e -> enrichir(niveauStockRepository.trouverParEntrepot(e.getCode())))
            .orElse(List.of());
    }

    /** Détail enrichi d'un niveau par son id (utilisé après une écriture). */
    public Optional<NiveauStockDetailResponse> consulterParId(Long id) {
        return niveauStockRepository.trouverTous().stream()
            .filter(n -> id.equals(n.getId()))
            .findFirst()
            .map(n -> versDetail(n, entrepotRepository.trouverTous().stream()
                .collect(Collectors.toMap(Entrepot::getCode, Function.identity(), (a, b) -> a))));
    }

    /** Niveaux en alerte (FAIBLE + CRITIQUE), ou critiques uniquement. */
    public List<NiveauStockDetailResponse> listerAlertes(boolean critiquesSeulement) {
        return enrichir(niveauStockRepository.trouverTous()).stream()
            .filter(d -> critiquesSeulement
                ? "CRITIQUE".equals(d.niveauAlerte())
                : ("CRITIQUE".equals(d.niveauAlerte()) || "FAIBLE".equals(d.niveauAlerte())))
            .toList();
    }

    // ── Enrichissement ────────────────────────────────────────────────────────

    private List<NiveauStockDetailResponse> enrichir(List<NiveauStock> niveaux) {
        Map<String, Entrepot> entrepotsParCode = entrepotRepository.trouverTous().stream()
            .collect(Collectors.toMap(Entrepot::getCode, Function.identity(), (a, b) -> a));
        return niveaux.stream().map(n -> versDetail(n, entrepotsParCode)).toList();
    }

    private NiveauStockDetailResponse versDetail(NiveauStock niveau, Map<String, Entrepot> entrepots) {
        Optional<Produit> produit = produitRepository.trouverParCode(CodeProduit.de(niveau.getCodeProduit()));
        Entrepot entrepot = entrepots.get(niveau.getCodeEntrepot());

        long seuil = 0L;
        if (niveau.getTypeEntrepot() != TypeEntrepot.MOBILE) {
            seuil = seuilStockRepository.trouver(niveau.getCodeProduit(), niveau.getTypeEntrepot())
                .filter(SeuilStock::isActif)
                .map(SeuilStock::getSeuilReapprovisionnement)
                .orElse(0L);
        }

        BigDecimal prixBase = produit.map(this::prixUnitaireBase).orElse(BigDecimal.ZERO);
        BigDecimal valeur = prixBase.multiply(BigDecimal.valueOf(niveau.getQuantiteEnBase()));

        return new NiveauStockDetailResponse(
            niveau.getId(),
            produit.map(Produit::getId).orElse(null),
            niveau.getCodeProduit(),
            produit.flatMap(this::skuBase).orElse(niveau.getCodeProduit()),
            produit.map(Produit::getDesignation).orElse(niveau.getCodeProduit()),
            produit.map(Produit::getMarque).orElse(null),
            produit.map(Produit::getGamme).orElse(null),
            produit.map(p -> p.getCategorie().name()).orElse(null),
            entrepot != null ? entrepot.getId() : null,
            niveau.getCodeEntrepot(),
            entrepot != null ? entrepot.getLibelle() : niveau.getCodeEntrepot(),
            niveau.getTypeEntrepot().name(),
            niveau.getQuantiteEnBase(),
            seuil,
            0L, // stock de sécurité — non modélisé au domaine pour l'instant
            calculerNiveauAlerte(niveau.getQuantiteEnBase(), seuil, niveau.getTypeEntrepot()),
            valeur,
            niveau.getDateModification()
        );
    }

    private String calculerNiveauAlerte(long quantite, long seuil, TypeEntrepot type) {
        if (type == TypeEntrepot.MOBILE || seuil <= 0) return "NORMAL";
        if (quantite <= seuil) return "CRITIQUE";
        if (quantite <= seuil + (seuil / 2)) return "FAIBLE";
        if (quantite > seuil * 3) return "SURPLUS";
        return "NORMAL";
    }

    private BigDecimal prixUnitaireBase(Produit produit) {
        return produit.getConditionnements().stream()
            .filter(c -> c.getFacteurConversionVersBase() == 1L)
            .findFirst()
            .map(Conditionnement::getPrixUnitaireHT)
            .orElse(BigDecimal.ZERO);
    }

    private Optional<String> skuBase(Produit produit) {
        return produit.getConditionnements().stream()
            .filter(c -> c.getFacteurConversionVersBase() == 1L)
            .findFirst()
            .map(Conditionnement::getSku);
    }
}
