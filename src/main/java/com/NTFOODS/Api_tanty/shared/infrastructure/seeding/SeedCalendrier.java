package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import java.time.LocalDate;

/**
 * Calendrier commun à tous les seeders.
 *
 * <p><strong>Rien de peuplé ne touche la journée courante.</strong> C'est la
 * seule règle, et elle vaut d'être tenue en un seul endroit : les données
 * d'amorçage servent à remplir les états et les historiques, pas à préempter les
 * actes que l'utilisateur vient poser lui-même. Une vente peuplée aujourd'hui
 * apparaîtrait dans le chiffre du jour ; une session ouverte aujourd'hui
 * empêcherait d'en préparer une vraie.</p>
 *
 * <p>Corollaire : tout ce qui est peuplé est <em>clos</em>. Une tournée en cours
 * ou une vente en attente de synchronisation, datée d'il y a trois semaines,
 * décrirait un incident jamais résolu plutôt qu'un historique.</p>
 */
public final class SeedCalendrier {

    /** Le peuplement commence la veille et remonte dans le temps. */
    public static final int RECUL_MINIMAL_JOURS = 1;

    private SeedCalendrier() {
    }

    /**
     * Date située {@code jours} jours avant aujourd'hui.
     *
     * @throws IllegalArgumentException si l'appelant vise la journée courante ou
     *         le futur — mieux vaut un seeder qui refuse de démarrer qu'une
     *         donnée fictive glissée dans le chiffre du jour.
     */
    public static LocalDate ilYA(int jours) {
        if (jours < RECUL_MINIMAL_JOURS) {
            throw new IllegalArgumentException(
                "Les données d'amorçage ne peuvent pas toucher la journée courante (recul demandé : "
                    + jours + " jour(s))");
        }
        return LocalDate.now().minusDays(jours);
    }

    /** Vrai si la date est utilisable par un seeder. */
    public static boolean estPeuplable(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
}
