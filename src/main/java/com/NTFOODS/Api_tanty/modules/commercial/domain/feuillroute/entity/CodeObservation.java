package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity;

import java.util.Objects;

/**
 * Code de saisie terrain pour un arrêt non visité (§5.9).
 *
 * <p>Autrefois figé dans une enum Java, ce référentiel est désormais géré dans
 * le CMS : un code porte un libellé, indique s'il exige une quantité, et peut
 * être désactivé sans suppression pour conserver l'historique.</p>
 */
public class CodeObservation {

    private final Long id;
    private final String code;
    private String libelle;
    private boolean requireQuantity;
    private boolean actif;
    private int ordre;

    public CodeObservation(Long id, String code, String libelle, boolean requireQuantity, boolean actif, int ordre) {
        this.id = id;
        this.code = Objects.requireNonNull(code, "Le code est obligatoire");
        this.libelle = Objects.requireNonNull(libelle, "Le libellé est obligatoire");
        this.requireQuantity = requireQuantity;
        this.actif = actif;
        this.ordre = ordre;
    }

    public static CodeObservation creer(String code, String libelle, boolean requireQuantity, int ordre) {
        return new CodeObservation(null, code, libelle, requireQuantity, true, ordre);
    }

    public void mettreAJour(String libelle, boolean requireQuantity, boolean actif, int ordre) {
        this.libelle = Objects.requireNonNull(libelle);
        this.requireQuantity = requireQuantity;
        this.actif = actif;
        this.ordre = ordre;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public boolean isRequireQuantity() { return requireQuantity; }
    public boolean isActif() { return actif; }
    public int getOrdre() { return ordre; }
}
