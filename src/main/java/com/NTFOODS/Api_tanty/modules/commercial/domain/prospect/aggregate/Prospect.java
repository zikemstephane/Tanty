package com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Prospect (§5.13).
 *
 * Un prospect est un client potentiel identifié par un commercial
 * lors de sa tournée. Il peut ensuite être converti en client actif.
 */
public class Prospect {

    private Long id;
    private final String codeProspect;
    private String nom;
    private String telephone;
    private String adresse;
    private String localite;
    private String matriculeCommercial;
    private CanalProspection canal;
    private StatutProspect statut;
    private String observations;
    private String codeClientConverti;
    private final LocalDate dateProspection;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Prospect(String codeProspect, String nom, String telephone, String adresse,
                     String localite, String matriculeCommercial, CanalProspection canal,
                     LocalDate dateProspection, LocalDateTime dateCreation) {
        this.codeProspect = Objects.requireNonNull(codeProspect);
        this.nom = Objects.requireNonNull(nom);
        this.telephone = telephone;
        this.adresse = adresse;
        this.localite = localite;
        this.matriculeCommercial = matriculeCommercial;
        this.canal = canal != null ? canal : CanalProspection.VISITE_TERRAIN;
        this.dateProspection = dateProspection;
        this.dateCreation = dateCreation;
        this.statut = StatutProspect.NOUVEAU;
        this.version = 0L;
    }

    public static Prospect creer(String codeProspect, String nom, String telephone, String adresse,
                                  String localite, String matriculeCommercial, CanalProspection canal,
                                  LocalDate dateProspection, String observations) {
        Prospect p = new Prospect(codeProspect, nom, telephone, adresse, localite,
            matriculeCommercial, canal, dateProspection, LocalDateTime.now());
        p.observations = observations;
        return p;
    }

    public static Prospect reconstituer(Long id, String codeProspect, String nom, String telephone,
                                          String adresse, String localite, String matriculeCommercial,
                                          CanalProspection canal, StatutProspect statut, String observations,
                                          String codeClientConverti, LocalDate dateProspection,
                                          LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        Prospect p = new Prospect(codeProspect, nom, telephone, adresse, localite,
            matriculeCommercial, canal, dateProspection, dateCreation);
        p.id = id;
        p.statut = statut;
        p.observations = observations;
        p.codeClientConverti = codeClientConverti;
        p.dateModification = dateModification;
        p.version = version;
        return p;
    }

    public void convertir(String codeClient) {
        if (statut != StatutProspect.NOUVEAU) {
            throw new IllegalStateException("Seul un prospect NOUVEAU peut être converti en client");
        }
        this.codeClientConverti = Objects.requireNonNull(codeClient);
        this.statut = StatutProspect.CONVERTI;
        this.dateModification = LocalDateTime.now();
    }

    public void marquerNonInteresse(String motif) {
        if (statut != StatutProspect.NOUVEAU) {
            throw new IllegalStateException("Seul un prospect NOUVEAU peut être marqué non intéressé");
        }
        this.observations = motif;
        this.statut = StatutProspect.NON_INTERESSE;
        this.dateModification = LocalDateTime.now();
    }

    public void archiver() {
        this.statut = StatutProspect.ARCHIVE;
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCodeProspect() { return codeProspect; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public String getLocalite() { return localite; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public CanalProspection getCanal() { return canal; }
    public StatutProspect getStatut() { return statut; }
    public String getObservations() { return observations; }
    public String getCodeClientConverti() { return codeClientConverti; }
    public LocalDate getDateProspection() { return dateProspection; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prospect)) return false;
        return Objects.equals(codeProspect, ((Prospect) o).codeProspect);
    }
    @Override public int hashCode() { return Objects.hash(codeProspect); }
}
