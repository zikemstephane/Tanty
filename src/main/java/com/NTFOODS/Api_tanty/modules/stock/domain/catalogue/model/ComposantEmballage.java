package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;

import java.util.Objects;

/**
 * Ligne de composition d'emballage d'un {@link Conditionnement} : un type de
 * composant et le ratio auquel il est consommé — {@code quantiteComposant}
 * unités de ce composant pour chaque {@code quantiteUnitesProduites} unités
 * produites.
 *
 * Exemple : 1 couvercle pour 24 sachets s'exprime par
 * {@code new ComposantEmballage(COUVERCLE, 1, 24)}. Un composant consommé
 * un pour un (le sachet lui-même) s'exprime par {@code (SACHET, 1, 1)}.
 */
public class ComposantEmballage {

    private final TypeEmballage type;
    private final int quantiteComposant;
    private final int quantiteUnitesProduites;

    public ComposantEmballage(TypeEmballage type, int quantiteComposant, int quantiteUnitesProduites) {
        this.type = Objects.requireNonNull(type, "Le type de composant est obligatoire");
        if (quantiteComposant <= 0) {
            throw new IllegalArgumentException("La quantité du composant " + type + " doit être strictement positive");
        }
        if (quantiteUnitesProduites <= 0) {
            throw new IllegalArgumentException("Le nombre d'unités produites associé au composant " + type + " doit être strictement positif");
        }
        this.quantiteComposant = quantiteComposant;
        this.quantiteUnitesProduites = quantiteUnitesProduites;
    }

    public TypeEmballage getType() { return type; }
    public int getQuantiteComposant() { return quantiteComposant; }
    public int getQuantiteUnitesProduites() { return quantiteUnitesProduites; }
}
