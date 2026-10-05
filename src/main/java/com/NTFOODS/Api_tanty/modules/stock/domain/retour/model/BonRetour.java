package com.NTFOODS.Api_tanty.modules.stock.domain.retour.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.enums.StatutBonRetour;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Constat d'invendus de fin de tournée.
 *
 * <p><strong>Aucun mouvement de stock n'est produit ici.</strong> Les invendus ne
 * repartent pas au dépôt : ils restent dans le stock mobile du commercial pour la
 * tournée suivante. Ce que l'agrégat enregistre est un constat — ce que le
 * commercial annonce rapporter, puis ce que le gestionnaire compte réellement — et
 * l'écart entre les deux.</p>
 *
 * <p>Seules les avaries et les échanges donnent lieu à une variation de stock ;
 * ils sont traités comme transactions non marchandes, hors de cet agrégat.</p>
 */
public class BonRetour {

    private Long id;
    private final String numero;
    private final String matriculeCommercial;
    private final List<LigneRetour> lignes;
    private final LocalDate dateRetour;
    private final LocalDateTime dateCreation;
    private StatutBonRetour statut;
    private String matriculeGestionnaire;
    private LocalDateTime dateComptage;
    private Long version;

    private BonRetour(String numero, String matriculeCommercial, LocalDate dateRetour,
                      LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.dateRetour = Objects.requireNonNull(dateRetour);
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.statut = StatutBonRetour.DECLARE;
        this.version = 0L;
    }

    /** Déclaration du commercial : le gestionnaire n'est pas encore connu. */
    public static BonRetour declarer(String numero, String matriculeCommercial, LocalDate dateRetour) {
        return new BonRetour(numero, matriculeCommercial, dateRetour, LocalDateTime.now());
    }

    public static BonRetour reconstituer(Long id, String numero, String matriculeCommercial,
                                         String matriculeGestionnaire, List<LigneRetour> lignes,
                                         LocalDate dateRetour, LocalDateTime dateCreation,
                                         StatutBonRetour statut, LocalDateTime dateComptage, Long version) {
        BonRetour b = new BonRetour(numero, matriculeCommercial, dateRetour, dateCreation);
        b.id = id;
        b.matriculeGestionnaire = matriculeGestionnaire;
        if (lignes != null) b.lignes.addAll(lignes);
        b.statut = statut;
        b.dateComptage = dateComptage;
        b.version = version;
        return b;
    }

    public LigneRetour ajouterLigne(String codeProduit, String sku, long quantiteDeclaree, String motif) {
        if (statut != StatutBonRetour.DECLARE) {
            throw new RegleMetierException("Le constat " + numero + " est déjà compté : il n'accepte plus de ligne");
        }
        LigneRetour ligne = LigneRetour.creer(codeProduit, sku, quantiteDeclaree, motif);
        lignes.add(ligne);
        return ligne;
    }

    public void exigerNonVide() {
        if (lignes.isEmpty()) throw new RegleMetierException("Un constat d'invendus doit contenir au moins une ligne");
    }

    /**
     * Comptage par le gestionnaire.
     *
     * <p>Chaque ligne déclarée doit recevoir une quantité comptée, y compris zéro :
     * laisser une ligne sans réponse laisserait croire qu'elle a été vérifiée alors
     * qu'elle ne l'a pas été. Une quantité portant sur un produit non déclaré est
     * refusée — elle relèverait d'un ajustement d'inventaire, pas d'un constat.</p>
     *
     * @param quantitesComptees quantité constatée par code produit
     */
    public void compter(Map<String, Long> quantitesComptees, String matriculeGestionnaire) {
        if (statut != StatutBonRetour.DECLARE) {
            throw new RegleMetierException("Le constat " + numero + " a déjà été compté le " + dateComptage);
        }
        Objects.requireNonNull(matriculeGestionnaire, "Le matricule du gestionnaire est obligatoire");
        Map<String, Long> saisies = new HashMap<>(quantitesComptees);

        for (LigneRetour ligne : lignes) {
            Long quantite = saisies.remove(ligne.getCodeProduit());
            if (quantite == null) {
                throw new RegleMetierException(
                    "Le produit " + ligne.getCodeProduit() + " a été déclaré mais n'a pas été compté");
            }
            ligne.enregistrerComptage(quantite);
        }
        if (!saisies.isEmpty()) {
            throw new RegleMetierException(
                "Produits comptés mais jamais déclarés : " + String.join(", ", saisies.keySet()));
        }

        this.matriculeGestionnaire = matriculeGestionnaire;
        this.dateComptage = LocalDateTime.now();
        this.statut = StatutBonRetour.COMPTE;
    }

    /** Somme des écarts lignes à lignes, ou {@code null} tant que rien n'est compté. */
    public Long ecartTotal() {
        if (statut != StatutBonRetour.COMPTE) return null;
        return lignes.stream().map(LigneRetour::ecart).filter(Objects::nonNull).mapToLong(Long::longValue).sum();
    }

    public boolean estCompte() {
        return statut == StatutBonRetour.COMPTE;
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
    public List<LigneRetour> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDate getDateRetour() { return dateRetour; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public StatutBonRetour getStatut() { return statut; }
    public LocalDateTime getDateComptage() { return dateComptage; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BonRetour that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
