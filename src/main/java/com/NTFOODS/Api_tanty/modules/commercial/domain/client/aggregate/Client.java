package com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;

import java.time.LocalDateTime;
import java.util.Objects;

public class Client {

    private Long id;
    private final String codeClient;
    private String nom;
    private String telephone;
    private String adresse;
    private String localite;
    private TypeClient type;
    private String matriculeCommercialReferent;
    private boolean actif;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Client(String codeClient, String nom, String telephone, String adresse,
                   String localite, TypeClient type, String matriculeCommercialReferent,
                   LocalDateTime dateCreation) {
        this.codeClient = Objects.requireNonNull(codeClient);
        this.nom = Objects.requireNonNull(nom);
        this.telephone = telephone;
        this.adresse = adresse;
        this.localite = localite;
        this.type = Objects.requireNonNull(type);
        this.matriculeCommercialReferent = matriculeCommercialReferent;
        this.actif = true;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Client creer(String codeClient, String nom, String telephone, String adresse,
                               String localite, TypeClient type, String matriculeCommercialReferent) {
        return new Client(codeClient, nom, telephone, adresse, localite, type,
            matriculeCommercialReferent, LocalDateTime.now());
    }

    public static Client reconstituer(Long id, String codeClient, String nom, String telephone,
                                       String adresse, String localite, TypeClient type,
                                       String matriculeCommercialReferent, boolean actif,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version) {
        Client c = new Client(codeClient, nom, telephone, adresse, localite, type,
            matriculeCommercialReferent, dateCreation);
        c.id = id;
        c.actif = actif;
        c.dateModification = dateModification;
        c.version = version;
        return c;
    }

    public void mettreAJour(String nom, String telephone, String adresse, String localite, TypeClient type) {
        this.nom = Objects.requireNonNull(nom);
        this.telephone = telephone;
        this.adresse = adresse;
        this.localite = localite;
        this.type = Objects.requireNonNull(type);
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() { this.actif = false; this.dateModification = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getCodeClient() { return codeClient; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public String getLocalite() { return localite; }
    public TypeClient getType() { return type; }
    public String getMatriculeCommercialReferent() { return matriculeCommercialReferent; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        return Objects.equals(codeClient, ((Client) o).codeClient);
    }
    @Override public int hashCode() { return Objects.hash(codeClient); }
}
