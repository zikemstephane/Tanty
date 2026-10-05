package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums;

import java.util.Objects;

/**
 * Saisie terrain d'un arrêt sur la feuille de route (§5.8 / §5.9).
 *
 * <p>Le {@code codeInfoClient} est désormais une chaîne libre validée par le
 * référentiel {@code CodeObservation}. Ce domain object ne connaît pas le
 * référentiel : c'est l'appelant (use case) qui valide avant de construire la
 * saisie.</p>
 */
public class SaisieArret {

    private final String codeClient;
    private final String codeInfoClient;
    private final Integer quantiteInfo;
    private final String observations;

    public SaisieArret(String codeClient, String codeInfoClient, Integer quantiteInfo, String observations) {
        this.codeClient = Objects.requireNonNull(codeClient);
        this.codeInfoClient = codeInfoClient;
        this.observations = observations;
        this.quantiteInfo = quantiteInfo;
    }

    public String getCodeClient() { return codeClient; }
    public String getCodeInfoClient() { return codeInfoClient; }
    public Integer getQuantiteInfo() { return quantiteInfo; }
    public String getObservations() { return observations; }
}
