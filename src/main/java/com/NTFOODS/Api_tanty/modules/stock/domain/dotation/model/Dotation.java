package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event.DotationApprouveeEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event.DotationSoumiseEvent;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Demande de dotation commerciale (depuis le Tampon).
 *
 * Temporalité : soumise en soirée pour la JOURNÉE SUIVANTE
 * (dateDotation = soumission, dateEffet = lendemain).
 * Le gestionnaire approuve / modifie / substitue / refuse. À l'approbation,
 * le stock tampon est débité et le stock mobile du commercial crédité
 * (orchestré par le use case via GestionDeStock). Le module Stock ne gère
 * aucun montant financier — uniquement le statut physique.
 */
public class Dotation {

    private Long id;
    private final String numero;
    private final String matriculeCommercial;
    private final LocalDate dateDotation;
    private final LocalDate dateEffet;
    private StatutDotation statut;
    private final List<LigneDotation> lignes;
    private String matriculeGestionnaire;
    private String motifRefus;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateDecision;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Dotation(String numero, String matriculeCommercial, LocalDate dateDotation, LocalDate dateEffet,
                     LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.dateDotation = Objects.requireNonNull(dateDotation);
        this.dateEffet = Objects.requireNonNull(dateEffet);
        this.dateCreation = dateCreation;
        this.statut = StatutDotation.SOUMISE;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    /** Soumission par le commercial : date d'effet = lendemain de la soumission. */
    public static Dotation soumettre(String numero, String matriculeCommercial, LocalDate dateDotation) {
        LocalDate effet = dateDotation.plusDays(1);
        Dotation d = new Dotation(numero, matriculeCommercial, dateDotation, effet, LocalDateTime.now());
        d.domainEvents.add(new DotationSoumiseEvent(numero, matriculeCommercial, effet));
        return d;
    }

    public static Dotation reconstituer(Long id, String numero, String matriculeCommercial, LocalDate dateDotation,
                                        LocalDate dateEffet, StatutDotation statut, List<LigneDotation> lignes,
                                        String matriculeGestionnaire, String motifRefus, LocalDateTime dateCreation,
                                        LocalDateTime dateDecision, Long version) {
        Dotation d = new Dotation(numero, matriculeCommercial, dateDotation, dateEffet, dateCreation);
        d.id = id;
        d.statut = statut;
        if (lignes != null) d.lignes.addAll(lignes);
        d.matriculeGestionnaire = matriculeGestionnaire;
        d.motifRefus = motifRefus;
        d.dateDecision = dateDecision;
        d.version = version;
        return d;
    }

    public LigneDotation ajouterLigne(String codeProduit, String sku, long quantiteDemandee) {
        if (statut != StatutDotation.SOUMISE) {
            throw new RegleMetierException("Les lignes ne peuvent être ajoutées qu'à une dotation soumise");
        }
        LigneDotation ligne = LigneDotation.creer(codeProduit, sku, quantiteDemandee);
        lignes.add(ligne);
        return ligne;
    }

    public void modifierLigne(String idLigne, long nouvelleQuantite, String motif, Role role) {
        exigerGestionnaire(role);
        exigerSoumise();
        trouverLigne(idLigne).modifierQuantite(nouvelleQuantite, motif);
    }

    public void substituerConditionnement(String idLigne, String nouveauSku, String motif, Role role) {
        exigerGestionnaire(role);
        exigerSoumise();
        trouverLigne(idLigne).substituerConditionnement(nouveauSku, motif);
    }

    /** Approbation par le gestionnaire → déclenche le débit tampon / crédit mobile (use case). */
    public void approuver(String matriculeGestionnaire, Role role) {
        exigerGestionnaire(role);
        exigerSoumise();
        boolean toutRefuse = lignes.stream().allMatch(l -> l.getQuantiteApprouvee() == 0);
        if (lignes.isEmpty() || toutRefuse) {
            throw new RegleMetierException("Impossible d'approuver une dotation sans aucune quantité");
        }
        this.matriculeGestionnaire = matriculeGestionnaire;
        this.statut = StatutDotation.APPROUVEE;
        this.dateDecision = LocalDateTime.now();
        domainEvents.add(new DotationApprouveeEvent(numero, matriculeCommercial, matriculeGestionnaire, dateEffet));
    }

    public void refuser(String matriculeGestionnaire, Role role, String motif) {
        exigerGestionnaire(role);
        exigerSoumise();
        Objects.requireNonNull(motif, "Le motif de refus est obligatoire");
        this.matriculeGestionnaire = matriculeGestionnaire;
        this.statut = StatutDotation.REFUSEE;
        this.motifRefus = motif;
        this.dateDecision = LocalDateTime.now();
    }

    private void exigerGestionnaire(Role role) {
        if (role != Role.GESTIONNAIRE_STOCK && role != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Gestionnaire de Stock peut traiter une dotation");
        }
    }

    private void exigerSoumise() {
        if (statut != StatutDotation.SOUMISE) {
            throw new RegleMetierException("La dotation n'est plus modifiable (statut : " + statut.getLibelle() + ")");
        }
    }

    private LigneDotation trouverLigne(String idLigne) {
        return lignes.stream().filter(l -> l.getId().equals(idLigne)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Ligne introuvable : " + idLigne));
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDateDotation() { return dateDotation; }
    public LocalDate getDateEffet() { return dateEffet; }
    public StatutDotation getStatut() { return statut; }
    public List<LigneDotation> getLignes() { return Collections.unmodifiableList(lignes); }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
    public String getMotifRefus() { return motifRefus; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateDecision() { return dateDecision; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dotation that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
