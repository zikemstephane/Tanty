package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.event.AlerteCreeeEvent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Alerte stock actionnable.
 *
 * Chaque alerte porte une action suggérée (déduite du type) et cible un rôle.
 * Suivi : NOUVELLE → LUE → TRAITEE (avec délai de traitement mesurable).
 */
public class Alerte {

    private Long id;
    private final String reference;
    private final TypeAlerte type;
    private final String message;
    private final String destinataireRole;
    private final String actionSuggeree;
    private final String codeProduit;
    private final String codeEntrepot;
    private final String referenceLiee;     // BC, BR, dotation… concerné
    private StatutAlerte statut;
    private String matriculeTraitant;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateLecture;
    private LocalDateTime dateTraitement;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Alerte(String reference, TypeAlerte type, String message, String codeProduit,
                   String codeEntrepot, String referenceLiee, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference);
        this.type = Objects.requireNonNull(type);
        this.message = Objects.requireNonNull(message);
        this.destinataireRole = type.getDestinataire().name();
        this.actionSuggeree = type.getActionSuggeree();
        this.codeProduit = codeProduit;
        this.codeEntrepot = codeEntrepot;
        this.referenceLiee = referenceLiee;
        this.dateCreation = dateCreation;
        this.statut = StatutAlerte.NOUVELLE;
        this.version = 0L;
    }

    public static Alerte creer(String reference, TypeAlerte type, String message, String codeProduit,
                               String codeEntrepot, String referenceLiee) {
        Alerte a = new Alerte(reference, type, message, codeProduit, codeEntrepot, referenceLiee, LocalDateTime.now());
        a.domainEvents.add(new AlerteCreeeEvent(reference, type, message, type.getDestinataire().name()));
        return a;
    }

    public static Alerte reconstituer(Long id, String reference, TypeAlerte type, String message,
                                      String codeProduit, String codeEntrepot, String referenceLiee,
                                      StatutAlerte statut, String matriculeTraitant, LocalDateTime dateCreation,
                                      LocalDateTime dateLecture, LocalDateTime dateTraitement, Long version) {
        Alerte a = new Alerte(reference, type, message, codeProduit, codeEntrepot, referenceLiee, dateCreation);
        a.id = id;
        a.statut = statut;
        a.matriculeTraitant = matriculeTraitant;
        a.dateLecture = dateLecture;
        a.dateTraitement = dateTraitement;
        a.version = version;
        return a;
    }

    public void marquerLue() {
        if (statut == StatutAlerte.NOUVELLE) {
            this.statut = StatutAlerte.LUE;
            this.dateLecture = LocalDateTime.now();
        }
    }

    public void traiter(String matricule) {
        this.statut = StatutAlerte.TRAITEE;
        this.matriculeTraitant = matricule;
        this.dateTraitement = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public TypeAlerte getType() { return type; }
    public String getMessage() { return message; }
    public String getDestinataireRole() { return destinataireRole; }
    public String getActionSuggeree() { return actionSuggeree; }
    public String getCodeProduit() { return codeProduit; }
    public String getCodeEntrepot() { return codeEntrepot; }
    public String getReferenceLiee() { return referenceLiee; }
    public StatutAlerte getStatut() { return statut; }
    public String getMatriculeTraitant() { return matriculeTraitant; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateLecture() { return dateLecture; }
    public LocalDateTime getDateTraitement() { return dateTraitement; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alerte that)) return false;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() { return Objects.hash(reference); }
}
