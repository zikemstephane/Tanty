package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BaremePrimeRepository {

    BaremePrime sauvegarder(BaremePrime bareme);

    Optional<BaremePrime> trouverParReference(String reference);

    /** Barème en vigueur à une date donnée — socle du recalcul d'une semaine passée. */
    Optional<BaremePrime> trouverApplicableA(LocalDate date);

    /** Barème courant, c'est-à-dire celui qu'aucun successeur n'a clôturé. */
    Optional<BaremePrime> trouverCourant();

    /** Historique complet, du plus récent au plus ancien. */
    List<BaremePrime> listerHistorique();
}
