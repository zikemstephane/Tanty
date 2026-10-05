package com.NTFOODS.Api_tanty.modules.stock.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response.TableauBordEnrichiResponse;
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
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository.StockMobileRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Use Case — Tableau de bord enrichi avec stats percutantes.
 * Niveaux du tampon, stock central, actions rapides, performances commerciaux,
 * alertes invendus (qui n'a pas pu vendre telle quantité tel jour).
 */
@Service
@Transactional(readOnly = true)
public class ConsulterTableauBordEnrichiUseCase {

    private final NiveauStockRepository niveauStockRepository;
    private final ProduitRepository produitRepository;
    private final SeuilStockRepository seuilStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final ReceptionRepository receptionRepository;
    private final LotRepository lotRepository;
    private final StockMobileRepository stockMobileRepository;
    private final SessionCommercialRepository sessionRepository;

    public ConsulterTableauBordEnrichiUseCase(NiveauStockRepository niveauStockRepository,
                                                ProduitRepository produitRepository,
                                                SeuilStockRepository seuilStockRepository,
                                                MouvementStockRepository mouvementStockRepository,
                                                ReceptionRepository receptionRepository,
                                                LotRepository lotRepository,
                                                StockMobileRepository stockMobileRepository,
                                                SessionCommercialRepository sessionRepository) {
        this.niveauStockRepository = niveauStockRepository;
        this.produitRepository = produitRepository;
        this.seuilStockRepository = seuilStockRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.receptionRepository = receptionRepository;
        this.lotRepository = lotRepository;
        this.stockMobileRepository = stockMobileRepository;
        this.sessionRepository = sessionRepository;
    }

    public TableauBordEnrichiResponse executer() {
        var niveaux = niveauStockRepository.trouverTous();

        BigDecimal valeurTotale = BigDecimal.ZERO;
        long alertesCritiques = 0, alertesFaibles = 0, alertesSecurite = 0;
        long stockCentralTotal = 0, stockTamponTotal = 0, stockMobileTotal = 0;

        Map<String, Long> centralParProduit = new HashMap<>();
        Map<String, Long> tamponParProduit = new HashMap<>();

        for (NiveauStock niveau : niveaux) {
            Optional<Produit> produit = produitRepository.trouverParCode(CodeProduit.de(niveau.getCodeProduit()));
            valeurTotale = valeurTotale.add(valoriser(niveau, produit.orElse(null)));

            if (niveau.getTypeEntrepot() == TypeEntrepot.CENTRAL) {
                stockCentralTotal += niveau.getQuantiteEnBase();
                centralParProduit.merge(niveau.getCodeProduit(), niveau.getQuantiteEnBase(), Long::sum);
            } else if (niveau.getTypeEntrepot() == TypeEntrepot.TAMPON) {
                stockTamponTotal += niveau.getQuantiteEnBase();
                tamponParProduit.merge(niveau.getCodeProduit(), niveau.getQuantiteEnBase(), Long::sum);
            } else if (niveau.getTypeEntrepot() == TypeEntrepot.MOBILE) {
                stockMobileTotal += niveau.getQuantiteEnBase();
            }

            if (niveau.getTypeEntrepot() != TypeEntrepot.MOBILE) {
                Optional<SeuilStock> seuil = seuilStockRepository.trouver(
                    niveau.getCodeProduit(), niveau.getTypeEntrepot());
                if (seuil.isPresent() && seuil.get().isActif()) {
                    long s = seuil.get().getSeuilReapprovisionnement();
                    long ss = seuil.get().getSeuilSecurite();
                    long q = niveau.getQuantiteEnBase();
                    if (ss > 0 && q <= ss) alertesSecurite++;
                    else if (q <= s) alertesCritiques++;
                    else if (q <= s + (s / 2)) alertesFaibles++;
                }
            }
        }

        long receptionsEnAttente =
            receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_1).size()
            + receptionRepository.trouverParStatut(StatutReception.EN_ATTENTE_VALIDATION_2).size();

        LocalDate aujourdhui = LocalDate.now();
        long receptionsDuJour = receptionRepository.trouverTous().stream()
            .filter(r -> aujourdhui.equals(r.getDateReception())).count();
        long mouvementsDuJour = mouvementStockRepository.trouverParPeriode(aujourdhui, aujourdhui).size();
        long lotsEnAttente = lotRepository.listerParStatut(StatutLot.DECLARE).size();

        // Sessions
        List<SessionCommercial> sessionsOuvertes = sessionRepository.listerParStatut(StatutSession.OUVERTE.name());
        List<SessionCommercial> sessionsPrep = sessionRepository.listerParStatut(StatutSession.PREPARATION.name());
        Set<String> commerciauxActifs = new HashSet<>();
        sessionsOuvertes.forEach(s -> commerciauxActifs.add(s.getMatriculeCommercial()));
        sessionsPrep.forEach(s -> commerciauxActifs.add(s.getMatriculeCommercial()));

        // Stocks mobiles
        List<StockMobile> stocksMobiles = stockMobileRepository.listerTous();
        stockMobileTotal = stocksMobiles.stream().mapToLong(StockMobile::totalRestant).sum();

        // Niveaux tampon
        List<TableauBordEnrichiResponse.StatutTamponResponse> niveauxTampon = buildNiveauxTampon(centralParProduit, tamponParProduit);

        // Performances
        List<TableauBordEnrichiResponse.PerformanceCommercialResponse> performances = buildPerformances(stocksMobiles, sessionsOuvertes);

        // Alertes invendus
        List<TableauBordEnrichiResponse.InvenduResponse> invendus = buildAlertesInvendus(stocksMobiles);

        // Actions rapides
        List<TableauBordEnrichiResponse.ActionRapideResponse> actions = List.of(
            new TableauBordEnrichiResponse.ActionRapideResponse("Nouvelle réception", "/api/stock/receptions", "blue"),
            new TableauBordEnrichiResponse.ActionRapideResponse("Approuver dotation", "/api/stock/dotations", "green"),
            new TableauBordEnrichiResponse.ActionRapideResponse("Définir seuil", "/api/stock/seuils", "orange"),
            new TableauBordEnrichiResponse.ActionRapideResponse("Préparer session", "/api/stock/sessions/preparer", "purple"),
            new TableauBordEnrichiResponse.ActionRapideResponse("Export rapport", "/api/stock/rapports", "gray")
        );

        return new TableauBordEnrichiResponse(
            niveaux.size(), valeurTotale, alertesCritiques, alertesFaibles, alertesSecurite,
            receptionsEnAttente, receptionsDuJour, mouvementsDuJour, lotsEnAttente,
            0L, BigDecimal.ZERO,
            stockCentralTotal, stockTamponTotal, stockMobileTotal,
            sessionsOuvertes.size(), sessionsPrep.size(), commerciauxActifs.size(),
            niveauxTampon, performances, invendus, actions
        );
    }

    private List<TableauBordEnrichiResponse.StatutTamponResponse> buildNiveauxTampon(
            Map<String, Long> central, Map<String, Long> tampon) {
        Set<String> produits = new HashSet<>();
        produits.addAll(central.keySet());
        produits.addAll(tampon.keySet());

        return produits.stream().map(code -> {
            long qCentral = central.getOrDefault(code, 0L);
            long qTampon = tampon.getOrDefault(code, 0L);
            Optional<SeuilStock> seuil = seuilStockRepository.trouver(code, TypeEntrepot.TAMPON);
            long sReappro = seuil.map(SeuilStock::getSeuilReapprovisionnement).orElse(0L);
            long sSecurite = seuil.map(SeuilStock::getSeuilSecurite).orElse(0L);
            String statut = sSecurite > 0 && qTampon <= sSecurite ? "CRITIQUE"
                : qTampon <= sReappro ? "ALERT"
                : qTampon <= sReappro + (sReappro / 2) ? "FAIBLE"
                : "OK";
            String designation = produitRepository.trouverParCode(CodeProduit.de(code))
                .map(Produit::getDesignation).orElse(code);
            return new TableauBordEnrichiResponse.StatutTamponResponse(
                code, designation, qCentral, qTampon, sReappro, sSecurite, statut);
        }).toList();
    }

    private List<TableauBordEnrichiResponse.PerformanceCommercialResponse> buildPerformances(
            List<StockMobile> stocks, List<SessionCommercial> sessions) {
        Map<String, String> sessionStatut = new HashMap<>();
        sessions.forEach(s -> sessionStatut.put(s.getMatriculeCommercial(), s.getStatut().name()));

        return stocks.stream().map(sm -> {
            long totalInit = sm.totalVentes() + sm.totalRestant() + sm.totalAvaries();
            double taux = totalInit > 0 ? (sm.totalVentes() * 100.0 / totalInit) : 0;
            return new TableauBordEnrichiResponse.PerformanceCommercialResponse(
                sm.getMatriculeCommercial(), sm.totalVentes(), sm.totalRestant(),
                sm.totalAvaries(), taux, sessionStatut.getOrDefault(sm.getMatriculeCommercial(), "N/A"));
        }).toList();
    }

    private List<TableauBordEnrichiResponse.InvenduResponse> buildAlertesInvendus(List<StockMobile> stocks) {
        return stocks.stream()
            .flatMap(sm -> sm.getLignes().stream()
                .filter(l -> l.getQuantiteDisponible() > 0)
                .map(l -> new TableauBordEnrichiResponse.InvenduResponse(
                    sm.getMatriculeCommercial(), l.getCodeProduit(), l.getDesignation(),
                    l.getQuantiteDisponible(), sm.getDateSession())))
            .toList();
    }

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
