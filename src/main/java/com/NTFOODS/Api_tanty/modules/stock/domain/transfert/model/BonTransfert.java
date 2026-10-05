package com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.event.TransfertExecuteEvent;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Bon de Transfert (Stock central → Stock tampon).
 *
 * Règle d'architecture : le tampon est exclusivement alimenté par des
 * transferts depuis le central. À l'exécution, deux mouvements sont produits
 * (SORTIE_CENTRALE + ENTREE_TAMPON) — orchestrés par le use case via GestionDeStock.
 */
public class BonTransfert {

    private Long id;
    private final String numero;
    private final String codeEntrepotSource;   // central
    private final String codeEntrepotCible;    // tampon
    private StatutTransfert statut;
    private final String matriculeGestionnaire;
    private final boolean declencheParSeuil;
    private final List<LigneTransfert> lignes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateExecution;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private BonTransfert(String numero, String codeEntrepotSource, String codeEntrepotCible,
                         String matriculeGestionnaire, boolean declencheParSeuil, LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.codeEntrepotSource = Objects.requireNonNull(codeEntrepotSource);
        this.codeEntrepotCible = Objects.requireNonNull(codeEntrepotCible);
        this.matriculeGestionnaire = Objects.requireNonNull(matriculeGestionnaire);
        this.declencheParSeuil = declencheParSeuil;
        this.dateCreation = dateCreation;
        this.statut = StatutTransfert.CREE;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    public static BonTransfert creer(String numero, String codeEntrepotSource, String codeEntrepotCible,
                                     String matriculeGestionnaire, boolean declencheParSeuil) {
        return new BonTransfert(numero, codeEntrepotSource, codeEntrepotCible, matriculeGestionnaire,
            declencheParSeuil, LocalDateTime.now());
    }

    public static BonTransfert reconstituer(Long id, String numero, String codeEntrepotSource, String codeEntrepotCible,
                                            StatutTransfert statut, String matriculeGestionnaire, boolean declencheParSeuil,
                                            List<LigneTransfert> lignes, LocalDateTime dateCreation,
                                            LocalDateTime dateExecution, Long version) {
        BonTransfert bt = new BonTransfert(numero, codeEntrepotSource, codeEntrepotCible, matriculeGestionnaire,
            declencheParSeuil, dateCreation);
        bt.id = id;
        bt.statut = statut;
        if (lignes != null) bt.lignes.addAll(lignes);
        bt.dateExecution = dateExecution;
        bt.version = version;
        return bt;
    }

    public LigneTransfert ajouterLigne(String codeProduit, String sku, long quantite) {
        if (statut != StatutTransfert.CREE) {
            throw new RegleMetierException("Un transfert " + statut.getLibelle() + " ne peut plus être modifié");
        }
        LigneTransfert ligne = LigneTransfert.creer(codeProduit, sku, quantite);
        lignes.add(ligne);
        return ligne;
    }

    /** Marque le transfert comme exécuté (l'application du stock est faite par le use case). */
    public void marquerExecute() {
        if (statut != StatutTransfert.CREE) {
            throw new RegleMetierException("Seul un transfert créé peut être exécuté");
        }
        if (lignes.isEmpty()) {
            throw new RegleMetierException("Un transfert doit contenir au moins une ligne");
        }
        this.statut = StatutTransfert.EXECUTE;
        this.dateExecution = LocalDateTime.now();
        domainEvents.add(new TransfertExecuteEvent(numero, codeEntrepotSource, codeEntrepotCible));
    }

    public void annuler() {
        if (statut == StatutTransfert.EXECUTE) {
            throw new RegleMetierException("Un transfert exécuté ne peut pas être annulé");
        }
        this.statut = StatutTransfert.ANNULE;
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public String getCodeEntrepotSource() { return codeEntrepotSource; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
    public StatutTransfert getStatut() { return statut; }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
    public boolean isDeclencheParSeuil() { return declencheParSeuil; }
    public List<LigneTransfert> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateExecution() { return dateExecution; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BonTransfert that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
