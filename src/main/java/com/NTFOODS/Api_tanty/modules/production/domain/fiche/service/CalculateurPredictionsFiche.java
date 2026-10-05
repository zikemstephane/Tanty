package com.NTFOODS.Api_tanty.modules.production.domain.fiche.service;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.PosteProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.LigneBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service domaine — calcule les prédictions dosage et postes d'une fiche journalière.
 *
 * Prédiction Dosage : répartition du poids de bouillie produit (fûts × 50 kg)
 * entre les produits TANTY_BOUILLIE_DE_SOJA du BC, au prorata de leur poids demandé.
 *
 * Prédiction Postes : conversion des réalisations poste vers l'unité de conditionnement
 * du produit, pour les catégories métier couvertes par les postes saisis.
 */
@Service
public class CalculateurPredictionsFiche {

    /** Poids d'un fût de mélange : 50 kg = 50 000 grammes. */
    public static final double POIDS_FUT_50KG_GRAMMES = 50_000.0;

    private final ProduitRepository produitRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final AffectationJournaliereRepository affectationRepository;
    private final SessionDosageRepository sessionDosageRepository;
    private final PosteProductionRepository posteProductionRepository;

    public CalculateurPredictionsFiche(ProduitRepository produitRepository,
                                         BonCommandeRepository bonCommandeRepository,
                                         AffectationJournaliereRepository affectationRepository,
                                         SessionDosageRepository sessionDosageRepository,
                                         PosteProductionRepository posteProductionRepository) {
        this.produitRepository = produitRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.affectationRepository = affectationRepository;
        this.sessionDosageRepository = sessionDosageRepository;
        this.posteProductionRepository = posteProductionRepository;
    }

    /**
     * Recalcule les maps predictionDosage et predictionPostes de la fiche.
     * La fiche est mutée directement (les maps sont modifiables en interne via les getters
     * ici car on les reconstruit et on les réinjecte via les méthodes métier).
     */
    public ResultatPrediction calculer(FicheProduitJournaliere fiche,
                                       PlanProductionHebdomadaire pph) {
        int nbFutsProduits = sessionDosageRepository.trouverParPPHEtDate(fiche.getReferencePPH(), fiche.getDate())
            .map(SessionDosage::getNbFutsProduits)
            .orElse(0);
        Map<String, Integer> predictionDosage = calculerPredictionDosage(fiche, pph, nbFutsProduits);
        Map<String, Integer> predictionPostes = calculerPredictionPostes(fiche, pph);
        return new ResultatPrediction(predictionDosage, predictionPostes);
    }

    private Map<String, Integer> calculerPredictionDosage(FicheProduitJournaliere fiche,
                                                            PlanProductionHebdomadaire pph,
                                                            int nbFutsProduits) {
        Map<String, Integer> resultat = new HashMap<>();
        if (nbFutsProduits <= 0) {
            return resultat;
        }

        Optional<BonCommande> bcOpt = pph.getReferenceBC() != null
            ? bonCommandeRepository.trouverParNumero(pph.getReferenceBC())
            : Optional.empty();
        if (bcOpt.isEmpty()) {
            return resultat;
        }

        Map<String, Produit> produits = chargerProduits(pph);
        Map<String, LigneBonCommande> lignesBcParProduit = bcOpt.get().getLignes().stream()
            .collect(Collectors.toMap(LigneBonCommande::getCodeProduit, l -> l, (a, b) -> a));

        // poids demandé par produit bouillie (en grammes)
        Map<String, Double> poidsDemandes = new HashMap<>();
        for (LignePPH lignePph : pph.getLignes()) {
            Produit produit = produits.get(lignePph.getCodeProduit());
            if (produit == null || produit.getCategorieMetier() != CategorieMetier.TANTY_BOUILLIE_DE_SOJA) {
                continue;
            }
            LigneBonCommande ligneBc = lignesBcParProduit.get(lignePph.getCodeProduit());
            if (ligneBc == null) continue;

            Optional<Conditionnement> conditionnementOpt = produit.trouverParSku(ligneBc.getSku());
            if (conditionnementOpt.isEmpty()) continue;

            double poidsDemande = ligneBc.getQuantiteCommandee() * conditionnementOpt.get().getPoidsUnitaireGrammes();
            poidsDemandes.put(lignePph.getCodeProduit(), poidsDemande);
        }

        double poidsDemandeTotal = poidsDemandes.values().stream().mapToDouble(Double::doubleValue).sum();
        double poidsProduitJour = nbFutsProduits * POIDS_FUT_50KG_GRAMMES;

        if (poidsDemandeTotal <= 0 || poidsProduitJour <= 0) {
            return resultat;
        }

        for (var entry : poidsDemandes.entrySet()) {
            String codeProduit = entry.getKey();
            double poidsAttribue = (entry.getValue() / poidsDemandeTotal) * poidsProduitJour;

            LigneBonCommande ligneBc = lignesBcParProduit.get(codeProduit);
            Produit produit = produits.get(codeProduit);
            Optional<Conditionnement> conditionnementOpt = produit != null && ligneBc != null
                ? produit.trouverParSku(ligneBc.getSku())
                : Optional.empty();
            if (conditionnementOpt.isEmpty() || conditionnementOpt.get().getPoidsUnitaireGrammes() <= 0) {
                continue;
            }
            Conditionnement conditionnement = conditionnementOpt.get();

            int prediction = (int) Math.round(poidsAttribue / conditionnement.getPoidsUnitaireGrammes());
            resultat.put(codeProduit, prediction);
        }

        return resultat;
    }

    private Map<String, Integer> calculerPredictionPostes(FicheProduitJournaliere fiche,
                                                          PlanProductionHebdomadaire pph) {
        Map<String, Integer> resultat = new HashMap<>();

        Optional<AffectationJournaliere> affectationOpt = affectationRepository
            .trouverParPPHEtDate(fiche.getReferencePPH(), fiche.getDate());
        if (affectationOpt.isEmpty()) {
            return resultat;
        }

        AffectationJournaliere affectation = affectationOpt.get();
        if (affectation.getSaisies().isEmpty()) {
            return resultat;
        }

        Map<String, Produit> produits = chargerProduits(pph);
        Map<String, LigneBonCommande> lignesBcParProduit = new HashMap<>();
        if (pph.getReferenceBC() != null) {
            Optional<BonCommande> bcOpt = bonCommandeRepository.trouverParNumero(pph.getReferenceBC());
            bcOpt.ifPresent(bc -> bc.getLignes().forEach(l -> lignesBcParProduit.put(l.getCodeProduit(), l)));
        }

        for (LignePPH lignePph : pph.getLignes()) {
            Produit produit = produits.get(lignePph.getCodeProduit());
            if (produit == null || produit.getCategorieMetier() != CategorieMetier.TANTY_A_GRIGNOTER) {
                continue;
            }
            Optional<String> codePoste = posteFabricantDe(produit.getDesignation());
            if (codePoste.isEmpty()) {
                continue;
            }
            Optional<SaisieRealisationPoste> saisie = affectation.getSaisies().stream()
                .filter(s -> s.getPoste().equals(codePoste.get()))
                .findFirst();
            if (saisie.isEmpty()) {
                continue;
            }

            LigneBonCommande ligneBc = lignesBcParProduit.get(lignePph.getCodeProduit());
            Optional<Conditionnement> conditionnementOpt = ligneBc != null
                ? produit.trouverParSku(ligneBc.getSku())
                : Optional.empty();
            if (conditionnementOpt.isEmpty()) {
                continue;
            }

            double unites = convertirRealisationPosteVersConditionnement(saisie.get(), conditionnementOpt.get());
            if (unites > 0) {
                resultat.put(lignePph.getCodeProduit(), (int) Math.round(unites));
            }
        }

        return resultat;
    }

    /**
     * Poste qui fabrique un produit grignoter, rattaché par son nom : tous les produits « caramel » relèvent du poste
     * CARAMEL, « croquette » de CROQUETTE, « arachide » ou « enrobé » de ARACHIDE_ENROBEE, « chips » de CHIPS_PLANTAIN.
     * Les postes de datage et d'étiquetage ne fabriquent aucun produit : ils ne figurent pas ici.
     */
    public static Optional<String> posteFabricantDe(String designation) {
        String nom = normaliserNom(designation);
        if (nom.contains("caramel")) return Optional.of("CARAMEL");
        if (nom.contains("croquette")) return Optional.of("CROQUETTE");
        if (nom.contains("arachide") || nom.contains("enrob")) return Optional.of("ARACHIDE_ENROBEE");
        if (nom.contains("chips")) return Optional.of("CHIPS_PLANTAIN");
        return Optional.empty();
    }

    private static String normaliserNom(String nom) {
        if (nom == null) return "";
        return Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    private double convertirRealisationPosteVersConditionnement(SaisieRealisationPoste saisie,
                                                                Conditionnement conditionnement) {
        double quantite = saisie.getQuantiteRealisee();
        String unitePoste = normaliserUnite(saisie.getUnite());
        String uniteConditionnement = normaliserUnite(conditionnement.getUniteConditionnement());

        if ("kg".equals(unitePoste)) {
            if (conditionnement.getPoidsUnitaireGrammes() > 0) {
                return (quantite * 1000.0) / conditionnement.getPoidsUnitaireGrammes();
            }
            return 0.0;
        }

        if (uniteConditionnement != null && uniteConditionnement.equals(unitePoste)
            && conditionnement.getNombreUnites() > 0) {
            return quantite / conditionnement.getNombreUnites();
        }
        return 0.0;
    }

    private Set<CategorieMetier> categoriesMetierDepuisPoste(PosteProduction poste) {
        if (poste.getCategoriesMetier() == null) return Set.of();
        return poste.getCategoriesMetier().stream()
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .map(CategorieMetier::depuisLibelle)
            .collect(Collectors.toSet());
    }

    private String normaliserUnite(String unite) {
        if (unite == null) return "";
        String u = unite.trim().toLowerCase()
            .replaceAll("[éèêë]", "e")
            .replaceAll("[àâä]", "a")
            .replaceAll("[ùûü]", "u")
            .replaceAll("[îï]", "i")
            .replaceAll("[ôö]", "o");
        return u.endsWith("s") && u.length() > 1 ? u.substring(0, u.length() - 1) : u;
    }

    private Map<String, Produit> chargerProduits(PlanProductionHebdomadaire pph) {
        Map<String, Produit> produits = new HashMap<>();
        for (LignePPH ligne : pph.getLignes()) {
            produitRepository.trouverParCode(CodeProduit.de(ligne.getCodeProduit()))
                .ifPresent(p -> produits.put(ligne.getCodeProduit(), p));
        }
        return produits;
    }

    public record ResultatPrediction(Map<String, Integer> predictionDosage,
                                      Map<String, Integer> predictionPostes) {}
}
