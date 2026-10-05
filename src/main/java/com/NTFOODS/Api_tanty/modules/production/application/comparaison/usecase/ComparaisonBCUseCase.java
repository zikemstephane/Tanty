package com.NTFOODS.Api_tanty.modules.production.application.comparaison.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse.FutsComparaison;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse.MatiereComparaison;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse.ParCategorie;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse.PosteRealise;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse.ProduitBC;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Compare, pour une journée, le plan issu du bon de commande avec les sessions de dosage et la production réalisée.
 * Toutes les sessions du plan pour la date sont additionnées (un agent doseur peut en ouvrir plusieurs par jour).
 */
@Service
@Transactional(readOnly = true)
public class ComparaisonBCUseCase {

    private static final String CATEGORIE_BOUILLIE = "TANTY_BOUILLIE_DE_SOJA";
    private static final String POSTE_CONDITIONNEMENT_SCEAUX = "CONDITIONNEMENT_SCEAUX_2L";

    private final PPHRepository pphRepository;
    private final SessionDosageRepository dosageRepository;
    private final AffectationJournaliereRepository affectationRepository;
    private final PosteProductionService posteService;

    public ComparaisonBCUseCase(PPHRepository pphRepository,
                                SessionDosageRepository dosageRepository,
                                AffectationJournaliereRepository affectationRepository,
                                PosteProductionService posteService) {
        this.pphRepository = pphRepository;
        this.dosageRepository = dosageRepository;
        this.affectationRepository = affectationRepository;
        this.posteService = posteService;
    }

    public ComparaisonBCResponse executer(String referencePPH, LocalDate date) {
        PlanProductionHebdomadaire plan = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));

        List<LignePlanDetail> lignesDuJour = plan.getLignesDetaillees().stream()
            .filter(l -> date.equals(l.getDateJour()))
            .toList();
        List<SessionDosage> sessions = dosageRepository.listerParDate(date).stream()
            .filter(s -> referencePPH.equals(s.getReferencePPH()))
            .toList();
        AffectationJournaliere affectation = affectationRepository.trouverParPPHEtDate(referencePPH, date).orElse(null);

        return new ComparaisonBCResponse(
            referencePPH, plan.getReferenceBC(), date, sessions.size(),
            calculerFuts(lignesDuJour, sessions),
            calculerMatieres(lignesDuJour, sessions),
            produits(lignesDuJour),
            postesRealises(affectation),
            productionBouillie(lignesDuJour, affectation));
    }

    private FutsComparaison calculerFuts(List<LignePlanDetail> lignes, List<SessionDosage> sessions) {
        int futsNets = sessions.stream().mapToInt(SessionDosage::getNbFutsNets).sum();
        double prevus = lignes.stream().mapToDouble(LignePlanDetail::getNombreFuts50kg).sum();
        double ecart = futsNets - prevus;
        double ecartPct = prevus > 0 ? ecart / prevus * 100 : 0;
        return new FutsComparaison(futsNets, prevus, ecart, ecartPct);
    }

    /** Théorique : besoins des produits du jour, par matière et par catégorie. Dosé : matières des sessions du jour. */
    private List<MatiereComparaison> calculerMatieres(List<LignePlanDetail> lignes, List<SessionDosage> sessions) {
        Map<String, Map<String, Double>> theorique = new TreeMap<>();
        for (LignePlanDetail l : lignes) {
            for (BesoinMatierePremiere b : l.getBesoinsMatieresPremiere()) {
                String matiere = SessionDosage.normaliserMatiere(b.matierePremiere());
                theorique.computeIfAbsent(matiere, k -> new TreeMap<>())
                    .merge(l.getCategorieMetier(), b.quantiteKg().doubleValue(), Double::sum);
            }
        }
        Map<String, Double> dosee = new TreeMap<>();
        for (SessionDosage s : sessions) {
            for (SessionDosage.MatierePremiereUtilisee m : s.getMatieresUtilisees()) {
                dosee.merge(SessionDosage.normaliserMatiere(m.getTypeMatiere()), m.getQuantiteKg(), Double::sum);
            }
        }

        Set<String> matieres = new TreeSet<>();
        matieres.addAll(theorique.keySet());
        matieres.addAll(dosee.keySet());

        List<MatiereComparaison> resultat = new ArrayList<>();
        for (String matiere : matieres) {
            Map<String, Double> parCategorie = theorique.getOrDefault(matiere, Map.of());
            double theoriqueKg = parCategorie.values().stream().mapToDouble(Double::doubleValue).sum();
            double doseeKg = dosee.getOrDefault(matiere, 0.0);
            double ecart = doseeKg - theoriqueKg;
            double ecartPct = theoriqueKg > 0 ? ecart / theoriqueKg * 100 : 0;
            List<ParCategorie> details = parCategorie.entrySet().stream()
                .map(e -> new ParCategorie(e.getKey(), e.getValue()))
                .toList();
            resultat.add(new MatiereComparaison(matiere, theoriqueKg, doseeKg, ecart, ecartPct, details));
        }
        return resultat;
    }

    private List<ProduitBC> produits(List<LignePlanDetail> lignes) {
        return lignes.stream()
            .map(l -> new ProduitBC(
                l.getCodeProduit(), l.getNomProduit(), l.getCategorieMetier(),
                l.getQuantiteCartons(), l.getTotalUnites(), l.getTotalPoidsKg(),
                l.getTotalMatieresPremieresKg().doubleValue()))
            .toList();
    }

    /**
     * Convertit les sceaux de bouillie réalisés en kg, via le poids d'un sceau (même donnée que les
     * prédictions de fiche, #100). Refuse la conversion quand elle serait ambiguë : aucun produit bouillie
     * prévu ce jour, plusieurs produits bouillie prévus le même jour, ou aucune réalisation à ce poste.
     */
    private ComparaisonBCResponse.ProductionBouillie productionBouillie(List<LignePlanDetail> lignes,
                                                                        AffectationJournaliere affectation) {
        List<LignePlanDetail> lignesBouillie = lignes.stream()
            .filter(l -> CATEGORIE_BOUILLIE.equals(l.getCategorieMetier()))
            .toList();
        if (lignesBouillie.isEmpty()) {
            return new ComparaisonBCResponse.ProductionBouillie(0, false, "Aucun produit de bouillie prévu ce jour.");
        }
        if (lignesBouillie.size() > 1) {
            return new ComparaisonBCResponse.ProductionBouillie(0, false,
                "Plusieurs produits de bouillie prévus ce jour : la conversion serait ambiguë.");
        }
        double poidsUnitaireGrammes = lignesBouillie.get(0).getPoidsUnitaireGrammes();
        if (poidsUnitaireGrammes <= 0) {
            return new ComparaisonBCResponse.ProductionBouillie(0, false,
                "Poids du conditionnement inconnu pour ce produit.");
        }
        if (affectation == null) {
            return new ComparaisonBCResponse.ProductionBouillie(0, false, "Aucune affectation ce jour.");
        }
        var saisieSceaux = affectation.getSaisies().stream()
            .filter(s -> POSTE_CONDITIONNEMENT_SCEAUX.equals(s.getPoste()))
            .findFirst();
        if (saisieSceaux.isEmpty()) {
            return new ComparaisonBCResponse.ProductionBouillie(0, false,
                "Aucune réalisation saisie au poste de conditionnement sceaux.");
        }
        double kg = saisieSceaux.get().getQuantiteRealisee() * poidsUnitaireGrammes / 1000.0;
        return new ComparaisonBCResponse.ProductionBouillie(kg, true, null);
    }

    private List<PosteRealise> postesRealises(AffectationJournaliere affectation) {
        if (affectation == null) return List.of();
        return affectation.getSaisies().stream()
            .map(s -> {
                var poste = posteService.parCode(s.getPoste());
                return new PosteRealise(poste.getCode(), poste.getLibelle(), poste.getCategoriesMetier(),
                    s.getQuantiteRealisee(), s.getObjectifAjuste(), poste.getUnite());
            })
            .toList();
    }
}
