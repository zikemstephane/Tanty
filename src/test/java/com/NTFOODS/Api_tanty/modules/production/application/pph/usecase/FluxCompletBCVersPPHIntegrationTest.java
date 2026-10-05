package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;
import com.NTFOODS.Api_tanty.modules.production.application.pph.mapper.PPHMapper;
import com.NTFOODS.Api_tanty.modules.production.application.pph.service.PlanificationProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.valider.ValiderBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.ComposantEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bloc 7 — flux complet ValiderBonCommandeUseCase → GenererPPHDepuisBCUseCase
 * → PlanificationProductionService → sauvegarde, avec un BC couvrant trois cas
 * réels simultanément : un produit bouillie (formule + emballage), un produit
 * d'un autre type de conditionnement (seau, sans hypothèse sachet), et un
 * produit sans formule ni composition d'emballage connue.
 *
 * <p>Toutes les dépendances sont des fakes en mémoire — même style que
 * {@code PlanificationProductionServiceTest} — pour exercer la VRAIE chaîne
 * d'appels transactionnelle sans nécessiter de base de données.</p>
 */
class FluxCompletBCVersPPHIntegrationTest {

    private static class BonCommandeRepositoryDeTest implements BonCommandeRepository {
        private final Map<String, BonCommande> parNumero = new HashMap<>();

        @Override public BonCommande sauvegarder(BonCommande bc) { parNumero.put(bc.getNumero(), bc); return bc; }
        @Override public Optional<BonCommande> trouverParNumero(String numero) { return Optional.ofNullable(parNumero.get(numero)); }
        @Override public List<BonCommande> trouverParStatut(StatutBonCommande statut) {
            return parNumero.values().stream().filter(b -> b.getStatut() == statut).toList();
        }
        @Override public List<BonCommande> trouverParFournisseur(String codeFournisseur) { return List.of(); }
        @Override public List<BonCommande> trouverTous() { return List.copyOf(parNumero.values()); }
        @Override public List<BonCommande> trouverEnRetard() { return List.of(); }
        @Override public long compterParAnnee(int annee) { return 0; }
    }

    private static class PPHRepositoryDeTest implements PPHRepository {
        private final Map<String, PlanProductionHebdomadaire> parReference = new HashMap<>();

        @Override public PlanProductionHebdomadaire sauvegarder(PlanProductionHebdomadaire pph) {
            parReference.put(pph.getReferencePPH(), pph); return pph;
        }
        @Override public Optional<PlanProductionHebdomadaire> trouverParReference(String referencePPH) {
            return Optional.ofNullable(parReference.get(referencePPH));
        }
        @Override public List<PlanProductionHebdomadaire> listerParStatut(StatutPPH statut) {
            return parReference.values().stream().filter(p -> p.getStatut() == statut).toList();
        }
        @Override public List<PlanProductionHebdomadaire> listerParBC(String referenceBC) {
            return parReference.values().stream().filter(p -> referenceBC.equals(p.getReferenceBC())).toList();
        }
        @Override public Optional<PlanProductionHebdomadaire> trouverEnCours() { return Optional.empty(); }
        @Override public List<PlanProductionHebdomadaire> listerTous() { return List.copyOf(parReference.values()); }
    }

    private static class ProduitRepositoryDeTest implements ProduitRepository {
        private final Map<String, Produit> parCode = new HashMap<>();

        void ajouter(Produit p) { parCode.put(p.getCode().getValeur(), p); }

        @Override public Produit sauvegarder(Produit produit) { ajouter(produit); return produit; }
        @Override public Optional<Produit> trouverParCode(CodeProduit code) { return Optional.ofNullable(parCode.get(code.getValeur())); }
        @Override public Optional<Produit> trouverParId(Long id) { return Optional.empty(); }
        @Override public Optional<Produit> trouverParSku(String sku) { return Optional.empty(); }
        @Override public boolean existeParCode(CodeProduit code) { return parCode.containsKey(code.getValeur()); }
        @Override public List<Produit> trouverTous() { return List.copyOf(parCode.values()); }
        @Override public List<Produit> trouverParCategorie(CategorieProduit categorie) { return List.of(); }
        @Override public List<Produit> rechercher(String marque, String gamme, String variete, Boolean actif) { return List.of(); }
        @Override public void supprimer(CodeProduit code) { parCode.remove(code.getValeur()); }
    }

    private static class FormuleProductionRepositoryDeTest implements FormuleProductionRepository {
        private final Map<String, FormuleProduction> formules = new HashMap<>();
        void enregistrer(FormuleProduction f) { formules.put(f.getCodeProduit(), f); }
        @Override public FormuleProduction sauvegarder(FormuleProduction f) { enregistrer(f); return f; }
        @Override public Optional<FormuleProduction> trouverParCodeProduit(String codeProduit) { return Optional.ofNullable(formules.get(codeProduit)); }
        @Override public List<FormuleProduction> listerToutes() { return List.copyOf(formules.values()); }
        @Override public void supprimerParCodeProduit(String codeProduit) { formules.remove(codeProduit); }
    }

    private static class EventPublisherDeTest implements EventPublisher {
        final List<Object> publies = new ArrayList<>();
        @Override public void publish(Object event) { publies.add(event); }
        @Override public void publishAll(Iterable<?> events) { events.forEach(publies::add); }
    }

    private Produit produitAvec(String code, String designation, CategorieMetier categorie,
                                double poidsUnitaireGrammes, TypeEmballage type, List<ComposantEmballage> composants) {
        Produit p = Produit.creer(CodeProduit.de(code), "TANTY", "Test", designation, designation,
            CategorieProduit.PRODUIT_FINI, categorie);
        Conditionnement c = p.ajouterConditionnement(code + "-UNITE", NiveauConditionnement.UNITE_BASE, "Unité",
            1L, poidsUnitaireGrammes, 0d, null, null);
        if (type != null) c.definirTypeEtComposants(type, composants);
        return p;
    }

    @Test
    @DisplayName("Bloc 7 — flux complet : bouillie 500kg + produit en seau + produit sans formule, sommes exactes de bout en bout")
    void flux_complet_bc_vers_pph() {
        BonCommandeRepositoryDeTest bonCommandeRepository = new BonCommandeRepositoryDeTest();
        PPHRepositoryDeTest pphRepository = new PPHRepositoryDeTest();
        ProduitRepositoryDeTest produitRepository = new ProduitRepositoryDeTest();
        FormuleProductionRepositoryDeTest formuleRepository = new FormuleProductionRepositoryDeTest();
        EventPublisherDeTest eventPublisher = new EventPublisherDeTest();

        // 1) Produit bouillie : formule 40/9/1 (base 50) + emballage sachet/couvercle (1 pour 24).
        Produit bouillie = produitAvec("BOUILLIE-500", "PRODUIT BOUILLIE", CategorieMetier.TANTY_BOUILLIE_DE_SOJA,
            100, TypeEmballage.SACHET, List.of(
                new ComposantEmballage(TypeEmballage.SACHET, 1, 1),
                new ComposantEmballage(TypeEmballage.COUVERCLE, 1, 24)));
        formuleRepository.enregistrer(FormuleProduction.creer("BOUILLIE-500", BigDecimal.valueOf(50), List.of(
            new LigneFormule("Maïs", BigDecimal.valueOf(40)),
            new LigneFormule("Soja", BigDecimal.valueOf(9)),
            new LigneFormule("Arachide", BigDecimal.valueOf(1)))));

        // 2) Produit d'un autre type de conditionnement (seau) : même formule, composition différente.
        Produit seau = produitAvec("SEAU-TEST", "PRODUIT EN SEAU", CategorieMetier.TANTY_BOUILLIE_DE_SOJA,
            1000, TypeEmballage.SEAU, List.of(new ComposantEmballage(TypeEmballage.SEAU, 1, 1)));
        formuleRepository.enregistrer(FormuleProduction.creer("SEAU-TEST", BigDecimal.valueOf(50), List.of(
            new LigneFormule("Maïs", BigDecimal.valueOf(40)),
            new LigneFormule("Soja", BigDecimal.valueOf(9)),
            new LigneFormule("Arachide", BigDecimal.valueOf(1)))));

        // 3) Produit sans formule ni composition d'emballage connue.
        Produit sansFormule = produitAvec("SANS-FORMULE", "PRODUIT SANS FORMULE", CategorieMetier.TANTY_A_GRIGNOTER,
            200, null, List.of());

        produitRepository.ajouter(bouillie);
        produitRepository.ajouter(seau);
        produitRepository.ajouter(sansFormule);

        BonCommande bc = BonCommande.creer("BC-FLUX-0001", null, LocalDate.now(), null, "CP001");
        bc.ajouterLigne("BOUILLIE-500", "PRODUIT BOUILLIE", null, 5000, null);
        bc.ajouterLigne("SEAU-TEST", "PRODUIT EN SEAU", null, 7, null);
        bc.ajouterLigne("SANS-FORMULE", "PRODUIT SANS FORMULE", null, 8, null);
        bonCommandeRepository.sauvegarder(bc);

        PlanificationProductionService planificationService = new PlanificationProductionService(formuleRepository);
        GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase =
            new GenererPPHDepuisBCUseCase(pphRepository, produitRepository, planificationService);
        ValiderBonCommandeUseCase validerBonCommandeUseCase = new ValiderBonCommandeUseCase(
            bonCommandeRepository, new BonCommandeMapper(), eventPublisher, genererPPHDepuisBCUseCase);

        Utilisateur acteur = Utilisateur.creer(Matricule.de("CP001"), NomComplet.de("Jean", "Dupont"),
            Email.de("jean.dupont@test.local"), MotDePasse.deHash("hash-quelconque"), Role.CHEF_PRODUCTION);

        // ── Exécution du flux réel : valider le BC déclenche la génération du PPH, dans la même transaction logique.
        var reponse = validerBonCommandeUseCase.executer(bc.getNumero(), acteur);

        assertEquals(StatutBonCommande.VALIDE.name(), reponse.statut());
        assertFalse(eventPublisher.publies.isEmpty(), "l'événement de validation du BC doit être publié");

        List<PlanProductionHebdomadaire> pphs = pphRepository.listerParBC(bc.getNumero());
        assertEquals(1, pphs.size(), "un seul PPH doit avoir été généré pour ce BC");
        PlanProductionHebdomadaire pph = pphs.get(0);
        assertEquals(StatutPPH.BROUILLON, pph.getStatut(), "le PPH généré n'est pas automatiquement validé");

        List<LignePlanDetail> lignesBouillie = filtrerParProduit(pph, "BOUILLIE-500");
        List<LignePlanDetail> lignesSeau = filtrerParProduit(pph, "SEAU-TEST");
        List<LignePlanDetail> lignesSansFormule = filtrerParProduit(pph, "SANS-FORMULE");

        assertEquals(5, lignesBouillie.size(), "5 jours ouvrés (semaine de repli, aucune période explicite)");
        assertEquals(5000, sommeCartons(lignesBouillie));
        assertEquals(0, BigDecimal.valueOf(400).compareTo(sommeMatiere(lignesBouillie, "Maïs")));
        assertEquals(0, BigDecimal.valueOf(90).compareTo(sommeMatiere(lignesBouillie, "Soja")));
        assertEquals(0, BigDecimal.valueOf(10).compareTo(sommeMatiere(lignesBouillie, "Arachide")));
        assertEquals(5000L, sommeEmballage(lignesBouillie, TypeEmballage.SACHET));
        assertEquals(209L, sommeEmballage(lignesBouillie, TypeEmballage.COUVERCLE), "ceil(5000/24) = 209");

        assertEquals(7, sommeCartons(lignesSeau));
        assertEquals(0, BigDecimal.valueOf(5.6).setScale(2).compareTo(sommeMatiere(lignesSeau, "Maïs")));
        assertEquals(0, BigDecimal.valueOf(1.26).setScale(2).compareTo(sommeMatiere(lignesSeau, "Soja")));
        assertEquals(0, BigDecimal.valueOf(0.14).setScale(2).compareTo(sommeMatiere(lignesSeau, "Arachide")));
        assertEquals(7L, sommeEmballage(lignesSeau, TypeEmballage.SEAU));
        assertEquals(0L, sommeEmballage(lignesSeau, TypeEmballage.SACHET), "aucune hypothèse sachet pour un produit en seau");

        assertEquals(8, sommeCartons(lignesSansFormule));
        assertTrue(lignesSansFormule.stream().allMatch(LignePlanDetail::isFormuleManquante));
        assertTrue(lignesSansFormule.stream().allMatch(LignePlanDetail::isCompositionEmballageManquante));
        assertTrue(lignesSansFormule.stream().allMatch(l -> l.getBesoinsMatieresPremiere().isEmpty()));
        assertTrue(lignesSansFormule.stream().allMatch(l -> l.getBesoinsEmballage().isEmpty()));
    }

    private List<LignePlanDetail> filtrerParProduit(PlanProductionHebdomadaire pph, String codeProduit) {
        return pph.getLignesDetaillees().stream().filter(l -> l.getCodeProduit().equals(codeProduit)).toList();
    }

    private int sommeCartons(List<LignePlanDetail> lignes) {
        return lignes.stream().mapToInt(LignePlanDetail::getQuantiteCartons).sum();
    }

    private BigDecimal sommeMatiere(List<LignePlanDetail> lignes, String matiere) {
        return lignes.stream().map(l -> l.getBesoinMatierePremiere(matiere)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long sommeEmballage(List<LignePlanDetail> lignes, TypeEmballage type) {
        return lignes.stream().mapToLong(l -> l.getBesoinEmballage(type)).sum();
    }
}
