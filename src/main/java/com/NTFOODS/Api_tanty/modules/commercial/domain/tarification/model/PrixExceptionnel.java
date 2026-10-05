package com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model;

import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.event.PrixExceptionnelAccordeEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agrégat Racine — Prix Exceptionnel (Pilier 3 : Tarification Verrouillée).
 *
 * Dérogation PONCTUELLE et TRAÇÉE au prix homologué de la {@link GrilleTarifaire},
 * accordée à distance, exclusivement par le DG ou le Directeur Commercial,
 * pour une transaction précisément identifiée (client + produit). Ne modifie
 * jamais la grille elle-même : c'est une exception documentée, jamais une
 * règle silencieuse.
 */
public class PrixExceptionnel {

    private Long id;
    private final String reference;
    private final String codeProduit;
    private final String codeClient;
    private final double prixHomologueFCFA;
    private final double prixAppliqueFCFA;
    private final String motif;
    private final String matriculeAutorisateur;
    private final String referenceVenteCible;
    private final LocalDateTime dateAutorisation;
    private final LocalDateTime dateCreation;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private PrixExceptionnel(String reference, String codeProduit, String codeClient, double prixHomologueFCFA,
                              double prixAppliqueFCFA, String motif, String matriculeAutorisateur,
                              String referenceVenteCible, LocalDateTime dateAutorisation, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference);
        this.codeProduit = Objects.requireNonNull(codeProduit).trim().toUpperCase();
        this.codeClient = Objects.requireNonNull(codeClient);
        if (prixHomologueFCFA <= 0) throw new IllegalArgumentException("Le prix homologué doit être positif");
        this.prixHomologueFCFA = prixHomologueFCFA;
        if (prixAppliqueFCFA <= 0) throw new IllegalArgumentException("Le prix appliqué doit être positif");
        this.prixAppliqueFCFA = prixAppliqueFCFA;
        if (motif == null || motif.isBlank()) {
            throw new IllegalArgumentException("Un motif est obligatoire pour tout déblocage de prix exceptionnel");
        }
        this.motif = motif;
        this.matriculeAutorisateur = Objects.requireNonNull(matriculeAutorisateur);
        this.referenceVenteCible = referenceVenteCible;
        this.dateAutorisation = Objects.requireNonNull(dateAutorisation);
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static PrixExceptionnel accorder(String reference, String codeProduit, String codeClient,
                                             double prixHomologueFCFA, double prixAppliqueFCFA, String motif,
                                             String matriculeAutorisateur, String referenceVenteCible) {
        LocalDateTime maintenant = LocalDateTime.now();
        PrixExceptionnel p = new PrixExceptionnel(reference, codeProduit, codeClient, prixHomologueFCFA,
            prixAppliqueFCFA, motif, matriculeAutorisateur, referenceVenteCible, maintenant, maintenant);
        p.domainEvents.add(new PrixExceptionnelAccordeEvent(reference, codeProduit, codeClient,
            prixHomologueFCFA, prixAppliqueFCFA, matriculeAutorisateur));
        return p;
    }

    public static PrixExceptionnel reconstituer(Long id, String reference, String codeProduit, String codeClient,
                                                 double prixHomologueFCFA, double prixAppliqueFCFA, String motif,
                                                 String matriculeAutorisateur, String referenceVenteCible,
                                                 LocalDateTime dateAutorisation, LocalDateTime dateCreation,
                                                 Long version) {
        PrixExceptionnel p = new PrixExceptionnel(reference, codeProduit, codeClient, prixHomologueFCFA,
            prixAppliqueFCFA, motif, matriculeAutorisateur, referenceVenteCible, dateAutorisation, dateCreation);
        p.id = id;
        p.version = version;
        return p;
    }

    public double getEcartFCFA() { return prixAppliqueFCFA - prixHomologueFCFA; }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public String getCodeProduit() { return codeProduit; }
    public String getCodeClient() { return codeClient; }
    public double getPrixHomologueFCFA() { return prixHomologueFCFA; }
    public double getPrixAppliqueFCFA() { return prixAppliqueFCFA; }
    public String getMotif() { return motif; }
    public String getMatriculeAutorisateur() { return matriculeAutorisateur; }
    public String getReferenceVenteCible() { return referenceVenteCible; }
    public LocalDateTime getDateAutorisation() { return dateAutorisation; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrixExceptionnel that)) return false;
        return Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(reference); }
}
