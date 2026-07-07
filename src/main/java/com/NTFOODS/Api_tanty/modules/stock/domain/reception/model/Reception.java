package com.NTFOODS.Api_tanty.modules.stock.domain.reception.model;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.SourceReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.event.ReceptionValideeEvent;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Réception de marchandise (Bon de Réception).
 *
 * Deux circuits :
 * - FOURNISSEUR : lié à un BC, double validation (étape 1 Gestionnaire, étape 2
 *   Comptable pour les matières premières / Contrôleur Général pour les
 *   consommables). Le gestionnaire enregistre uniquement les quantités physiques
 *   devant lui, sans vérifier la conformité à la commande.
 * - PRODUCTION : réception de produits finis déclarés par la Production ; le
 *   Gestionnaire effectue la réception physique puis valide.
 *
 * À la validation finale, le stock central est mis à jour (orchestré par le use case).
 */
public class Reception {

    private Long id;
    private final String numero;
    private final SourceReception source;
    private final CategorieProduit categorie;
    private final String codeFournisseur;      // FOURNISSEUR
    private final String numeroBonCommande;    // FOURNISSEUR
    private final String numeroLotProduction;  // PRODUCTION
    private final String codeEntrepotCible;    // stock central
    private StatutReception statut;
    private final List<LigneReception> lignes;
    private String matriculeValidateur1;
    private String matriculeValidateur2;
    private String motifRejet;
    private final double seuilEcartPourcentage;
    private final LocalDate dateReception;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Reception(String numero, SourceReception source, CategorieProduit categorie, String codeFournisseur,
                      String numeroBonCommande, String numeroLotProduction, String codeEntrepotCible,
                      double seuilEcartPourcentage, LocalDate dateReception, LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.source = Objects.requireNonNull(source);
        this.categorie = Objects.requireNonNull(categorie);
        this.codeFournisseur = codeFournisseur;
        this.numeroBonCommande = numeroBonCommande;
        this.numeroLotProduction = numeroLotProduction;
        this.codeEntrepotCible = Objects.requireNonNull(codeEntrepotCible);
        this.seuilEcartPourcentage = seuilEcartPourcentage;
        this.dateReception = Objects.requireNonNull(dateReception);
        this.dateCreation = dateCreation;
        this.statut = StatutReception.EN_ATTENTE_VALIDATION_1;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    public static Reception creerFournisseur(String numero, CategorieProduit categorie, String codeFournisseur,
                                             String numeroBonCommande, String codeEntrepotCible,
                                             double seuilEcartPourcentage, LocalDate dateReception) {
        if (categorie == CategorieProduit.PRODUIT_FINI) {
            throw new RegleMetierException("Une réception fournisseur ne peut concerner des produits finis");
        }
        Objects.requireNonNull(codeFournisseur, "Le fournisseur est obligatoire");
        Objects.requireNonNull(numeroBonCommande, "Le numéro de BC lié est obligatoire pour une réception fournisseur");
        return new Reception(numero, SourceReception.FOURNISSEUR, categorie, codeFournisseur, numeroBonCommande,
            null, codeEntrepotCible, seuilEcartPourcentage, dateReception, LocalDateTime.now());
    }

    public static Reception creerProduction(String numero, String numeroLotProduction, String codeEntrepotCible,
                                            double seuilEcartPourcentage, LocalDate dateReception) {
        Objects.requireNonNull(numeroLotProduction, "Le numéro de lot de production est obligatoire");
        return new Reception(numero, SourceReception.PRODUCTION, CategorieProduit.PRODUIT_FINI, null, null,
            numeroLotProduction, codeEntrepotCible, seuilEcartPourcentage, dateReception, LocalDateTime.now());
    }

    public static Reception reconstituer(Long id, String numero, SourceReception source, CategorieProduit categorie,
                                         String codeFournisseur, String numeroBonCommande, String numeroLotProduction,
                                         String codeEntrepotCible, StatutReception statut, List<LigneReception> lignes,
                                         String matriculeValidateur1, String matriculeValidateur2, String motifRejet,
                                         double seuilEcartPourcentage, LocalDate dateReception,
                                         LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        Reception r = new Reception(numero, source, categorie, codeFournisseur, numeroBonCommande, numeroLotProduction,
            codeEntrepotCible, seuilEcartPourcentage, dateReception, dateCreation);
        r.id = id;
        r.statut = statut;
        if (lignes != null) r.lignes.addAll(lignes);
        r.matriculeValidateur1 = matriculeValidateur1;
        r.matriculeValidateur2 = matriculeValidateur2;
        r.motifRejet = motifRejet;
        r.dateModification = dateModification;
        r.version = version;
        return r;
    }

    public LigneReception ajouterLigne(String codeProduit, String sku, long quantiteAttendue,
                                       long quantiteRecue, String idLigneBC) {
        if (statut != StatutReception.EN_ATTENTE_VALIDATION_1) {
            throw new RegleMetierException("Les lignes ne peuvent être ajoutées qu'avant la 1ʳᵉ validation");
        }
        LigneReception ligne = LigneReception.creer(codeProduit, sku, quantiteAttendue, quantiteRecue, idLigneBC);
        lignes.add(ligne);
        this.dateModification = LocalDateTime.now();
        return ligne;
    }

    /**
     * Le gestionnaire enregistre uniquement les quantités physiques devant lui.
     * Il ne cherche pas à savoir si cela correspond à la commande ou non.
     */
    public LigneReception ajouterLigneSimple(String codeProduit, String sku, long quantiteRecue, String idLigneBC) {
        if (statut != StatutReception.EN_ATTENTE_VALIDATION_1) {
            throw new RegleMetierException("Les lignes ne peuvent être ajoutées qu'avant la 1ʳᵉ validation");
        }
        LigneReception ligne = LigneReception.creerSimple(codeProduit, sku, quantiteRecue, idLigneBC);
        lignes.add(ligne);
        this.dateModification = LocalDateTime.now();
        return ligne;
    }

    /** Étape 1 — vérification physique par le Gestionnaire de Stock (ou Chef de Production pour les produits finis). */
    public void validerEtape1(String matriculeValidateur, Role role) {
        if (role != Role.GESTIONNAIRE_STOCK && role != Role.ADMIN) {
            if (!(categorie == CategorieProduit.PRODUIT_FINI && role == Role.CHEF_PRODUCTION)) {
                throw new AccesRefuseException("Seul le Gestionnaire de Stock (ou le Chef de Production pour les produits finis) peut effectuer la 1ʳᵉ validation");
            }
        }
        if (statut != StatutReception.EN_ATTENTE_VALIDATION_1) {
            throw new RegleMetierException("Réception non en attente de 1ʳᵉ validation");
        }
        exigerNonVide();
        this.matriculeValidateur1 = matriculeValidateur;
        this.statut = StatutReception.EN_ATTENTE_VALIDATION_2;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Étape 2 — validation métier selon la catégorie :
     * MATIERE_PREMIERE → Comptable ; CONSOMMABLE → Contrôleur Général ;
     * PRODUIT_FINI (production) → Gestionnaire de Stock.
     * Publie {@link ReceptionValideeEvent} (le use case met à jour le stock central).
     */
    public void validerEtape2(String matriculeValidateur, Role role) {
        if (statut != StatutReception.EN_ATTENTE_VALIDATION_2) {
            throw new RegleMetierException("Réception non en attente de 2ᵉ validation");
        }
        verifierRoleEtape2(role);
        this.matriculeValidateur2 = matriculeValidateur;
        this.statut = StatutReception.VALIDEE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new ReceptionValideeEvent(numero, source, codeEntrepotCible, numeroBonCommande));
    }

    public void rejeter(String matricule, Role role, String motif) {
        if (statut.estTerminal()) {
            throw new RegleMetierException("Réception déjà terminée");
        }
        verifierRoleEtape2(role);
        Objects.requireNonNull(motif, "Le motif de rejet est obligatoire");
        this.statut = StatutReception.REJETEE;
        this.motifRejet = motif;
        this.matriculeValidateur2 = matricule;
        this.dateModification = LocalDateTime.now();
    }

    public boolean aEcartSignificatif() {
        return lignes.stream().anyMatch(l -> l.pourcentageEcart() > seuilEcartPourcentage);
    }

    private void verifierRoleEtape2(Role role) {
        if (role == Role.ADMIN) return;
        switch (categorie) {
            case MATIERE_PREMIERE -> {
                if (role != Role.COMPTABLE) {
                    throw new AccesRefuseException("La 2ᵉ validation des matières premières revient au Comptable");
                }
            }
            case CONSOMMABLE -> {
                if (role != Role.CONTROLEUR_GENERAL) {
                    throw new AccesRefuseException("La 2ᵉ validation des consommables revient au Contrôleur Général");
                }
            }
            case PRODUIT_FINI -> {
                if (role != Role.GESTIONNAIRE_STOCK) {
                    throw new AccesRefuseException("La validation d'une réception production revient au Gestionnaire de Stock");
                }
            }
        }
    }

    private void exigerNonVide() {
        if (lignes.isEmpty()) throw new RegleMetierException("Une réception doit contenir au moins une ligne");
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public SourceReception getSource() { return source; }
    public CategorieProduit getCategorie() { return categorie; }
    public String getCodeFournisseur() { return codeFournisseur; }
    public String getNumeroBonCommande() { return numeroBonCommande; }
    public String getNumeroLotProduction() { return numeroLotProduction; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
    public StatutReception getStatut() { return statut; }
    public List<LigneReception> getLignes() { return Collections.unmodifiableList(lignes); }
    public String getMatriculeValidateur1() { return matriculeValidateur1; }
    public String getMatriculeValidateur2() { return matriculeValidateur2; }
    public String getMotifRejet() { return motifRejet; }
    public double getSeuilEcartPourcentage() { return seuilEcartPourcentage; }
    public LocalDate getDateReception() { return dateReception; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reception that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
