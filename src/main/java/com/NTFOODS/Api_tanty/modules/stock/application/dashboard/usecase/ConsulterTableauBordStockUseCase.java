package com.NTFOODS.Api_tanty.modules.stock.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response.TableauBordStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Use Case — Agrège les KPIs du tableau de bord Stock à partir des
 * différents agrégats (niveaux, seuils, mouvements, réceptions, lots).
 *
 * Lecture seule. Les KPIs hors module Stock (ventes du jour, commandes
 * internes) sont à 0 en attendant la connexion des modules concernés.
 */
@Service
@Transactional(readOnly = true)
public class ConsulterTableauBordStockUseCase {

    private final NiveauStockRepository niveauStockRepository;
    private final ProduitRepository produitRepository;
    private final SeuilStockRepository seuilStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final ReceptionRepository receptionRepository;
    private final LotRepository lotRepository;

    public ConsulterTableauBordStockUseCase(NiveauStockRepository niveauStockRepository,
                                            ProduitRepository produitRepository,
                                            SeuilStockRepository seuilStockRepository,
                                            MouvementStockRepository mouvementStockRepository,
                                            ReceptionRepository receptionRepository,
                                            LotRepository lotRepository) {
        this.niveauStockRepository = niveauStockRepository;
        this.produitRepository = produitRepository;
        this.seuilStockRepository = seuilStockRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.receptionRepository = receptionRepository;
        this.lotRepository = lotRepository;
    }

    public TableauBordStockResponse executer() {
        var niveaux = niveauStockRepository.trouverTous();

        BigDecimal valeurTotale = BigDecimal.ZERO;
        long alertesCritiques = 0;
        long alertesFaibles = 0;

        for (NiveauStock niveau : niveaux) {
            Optional<Produit> produit = produitRepository.trouverParCode(CodeProduit.de(niveau.getCodeProduit()));
            valeurTotale = valeurTotale.add(valoriser(niveau, produit.orElse(null)));

            if (niveau.getTypeEntrepot() != TypeEntrepot.MOBILE) {
                Optional<SeuilStock> seuil = seuilStockRepository.trouver(
                    niveau.getCodeProduit(), niveau.getTypeEntrepot());
                if (seuil.isPresent() && seuil.get().isActif()) {
                    long s = seuil.get().getSeuilReapprovisionnement();
                    long q = niveau.getQuantiteEnBase();
                    if (q <= s) {
                        alertesCritiques++;
                    } else if (q <= s + (s / 2)) {
                        alertesFaibles++;
                    }
                }
            }
        }

        long receptionsEnAttente =
            receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_1).size()
            + receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_2).size();

        LocalDate aujourdhui = LocalDate.now();
        long receptionsDuJour = receptionRepository.trouverTous().stream()
            .filter(r -> aujourdhui.equals(r.getDateReception()))
            .count();

        long mouvementsDuJour = mouvementStockRepository.trouverParPeriode(aujourdhui, aujourdhui).size();

        long lotsEnAttente = lotRepository.listerParStatut(StatutLot.DECLARE).size();

        return new TableauBordStockResponse(
            niveaux.size(),
            valeurTotale,
            alertesCritiques,
            alertesFaibles,
            receptionsEnAttente,
            receptionsDuJour,
            mouvementsDuJour,
            lotsEnAttente,
            0L,                 // commandes internes — module non connecté (WP5)
            BigDecimal.ZERO     // ventes du jour — module commercial non connecté
        );
    }

    /** Valorise un niveau : quantité (unités de base) × prix de l'unité de base. */
    private BigDecimal valoriser(NiveauStock niveau, Produit produit) {
        if (produit == null) return BigDecimal.ZERO;
        return produit.getConditionnements().stream()
            .filter(c -> c.getFacteurConversionVersBase() == 1L)
            .findFirst()
            .map(Conditionnement::getPrixUnitaireHT)
            .map(prix -> prix.multiply(BigDecimal.valueOf(niveau.getQuantiteEnBase())))
            .orElse(BigDecimal.ZERO);
    }
}
