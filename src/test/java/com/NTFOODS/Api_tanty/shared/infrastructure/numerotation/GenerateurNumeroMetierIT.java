package com.NTFOODS.Api_tanty.shared.infrastructure.numerotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Numérotation métier éprouvée contre une base PostgreSQL réelle.
 *
 * <p>Ces garanties ne peuvent pas être vérifiées sans base : elles reposent sur
 * le comportement de {@code nextval}, atomique et non transactionnel. La classe
 * n'est pas annotée {@code @Transactional} — un rollback n'annule pas un tirage
 * de séquence, et c'est précisément la propriété recherchée.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
class GenerateurNumeroMetierIT {

    private static final Pattern FORMAT_VENTE = Pattern.compile("^VTE-\\d{8}-\\d{6}$");
    private static final Pattern FORMAT_PRECOMMANDE = Pattern.compile("^PCM-\\d{8}-\\d{6}$");

    @Autowired
    private GenerateurNumeroMetier generateur;

    @Test
    @DisplayName("Le numéro respecte le format PREFIXE-yyyyMMdd-NNNNNN")
    void format_du_numero() {
        String vente = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.of(2026, 8, 15));
        String precommande = generateur.genererNumero(FamilleNumero.PRECOMMANDE, LocalDate.of(2026, 8, 15));

        assertTrue(FORMAT_VENTE.matcher(vente).matches(), "format inattendu : " + vente);
        assertTrue(FORMAT_PRECOMMANDE.matcher(precommande).matches(), "format inattendu : " + precommande);
        assertTrue(vente.contains("-20260815-"), "la date métier doit être portée par le numéro");
    }

    @Test
    @DisplayName("La date transmise prime sur la date système")
    void la_date_metier_est_conservee() {
        String numero = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.of(2026, 1, 3));

        assertTrue(numero.startsWith("VTE-20260103-"),
            "une vente saisie hors ligne doit conserver sa date réelle : " + numero);
    }

    @Test
    @DisplayName("Deux cents tirages consécutifs sont tous distincts")
    void tirages_consecutifs_tous_distincts() {
        Set<String> numeros = new HashSet<>();

        for (int i = 0; i < 200; i++) {
            numeros.add(generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now()));
        }

        assertEquals(200, numeros.size(), "aucun numéro ne doit être réattribué");
    }

    @Test
    @DisplayName("Les familles ont des compteurs indépendants")
    void familles_independantes() {
        String venteAvant = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now());
        generateur.genererNumero(FamilleNumero.PRECOMMANDE, LocalDate.now());
        generateur.genererNumero(FamilleNumero.PRECOMMANDE, LocalDate.now());
        String venteApres = generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now());

        long avant = compteurDe(venteAvant);
        long apres = compteurDe(venteApres);

        assertEquals(avant + 1, apres,
            "les tirages sur une famille ne doivent pas décaler l'autre");
    }

    @Test
    @DisplayName("Huit fils concurrents n'obtiennent jamais le même numéro")
    void concurrence_sans_collision() throws Exception {
        final int fils = 8;
        final int tiragesParFil = 25;

        ExecutorService pool = Executors.newFixedThreadPool(fils);
        CountDownLatch depart = new CountDownLatch(1);
        List<Future<List<String>>> resultats = new java.util.ArrayList<>();

        try {
            for (int i = 0; i < fils; i++) {
                resultats.add(pool.submit(() -> {
                    depart.await();
                    return IntStream.range(0, tiragesParFil)
                        .mapToObj(n -> generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now()))
                        .toList();
                }));
            }

            // Départ simultané : maximise le recouvrement des tirages.
            depart.countDown();

            Set<String> tous = Collections.synchronizedSet(new HashSet<>());
            int total = 0;
            for (Future<List<String>> resultat : resultats) {
                List<String> lot = resultat.get(30, TimeUnit.SECONDS);
                tous.addAll(lot);
                total += lot.size();
            }

            assertEquals(fils * tiragesParFil, total, "tous les tirages doivent aboutir");
            assertEquals(total, tous.size(),
                "deux transactions simultanées ne doivent jamais obtenir le même numéro");
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    @DisplayName("Le compteur progresse strictement, sans retour en arrière")
    void compteur_strictement_croissant() {
        long precedent = -1;

        for (int i = 0; i < 20; i++) {
            long courant = compteurDe(generateur.genererNumero(FamilleNumero.VENTE, LocalDate.now()));
            assertTrue(courant > precedent,
                "le compteur ne doit jamais régresser : " + courant + " après " + precedent);
            precedent = courant;
        }
    }

    /** Extrait la partie numérique finale d'un numéro métier. */
    private long compteurDe(String numero) {
        return Long.parseLong(numero.substring(numero.lastIndexOf('-') + 1));
    }
}
