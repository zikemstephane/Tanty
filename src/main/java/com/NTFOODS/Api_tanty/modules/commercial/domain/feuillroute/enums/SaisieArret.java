package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;

import java.util.Objects;

/**
 * Saisie terrain d'un arrêt sur la feuille de route (§5.8 / §5.9).
 *
 * Si un code {@link InfoClientEnum} est fourni, ceux marqués
 * {@code requireQuantity} doivent inclure une quantité > 0.
 */
public class SaisieArret {

    private final String codeClient;
    private final InfoClientEnum codeInfoClient;
    private final Integer quantiteInfo;
    private final String observations;

    public SaisieArret(String codeClient, InfoClientEnum codeInfoClient, Integer quantiteInfo, String observations) {
        this.codeClient = Objects.requireNonNull(codeClient);
        this.codeInfoClient = codeInfoClient;
        this.observations = observations;
        if (codeInfoClient != null && InfoClientEnum.requiresQuantity(codeInfoClient)) {
            if (quantiteInfo == null || quantiteInfo <= 0) {
                throw new IllegalArgumentException(
                    "Le code " + codeInfoClient + " (" + codeInfoClient.getLibelle()
                    + ") exige une quantité > 0 (§5.9)");
            }
        }
        this.quantiteInfo = quantiteInfo;
    }

    public String getCodeClient() { return codeClient; }
    public InfoClientEnum getCodeInfoClient() { return codeInfoClient; }
    public Integer getQuantiteInfo() { return quantiteInfo; }
    public String getObservations() { return observations; }
}
