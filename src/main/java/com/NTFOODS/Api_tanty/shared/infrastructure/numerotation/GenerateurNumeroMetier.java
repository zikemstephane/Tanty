package com.NTFOODS.Api_tanty.shared.infrastructure.numerotation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.Set;

/**
 * Générateur unique des numéros métier, adossé à des séquences PostgreSQL.
 *
 * <p>Remplace les compteurs {@code AtomicLong} en mémoire qui repartaient de zéro
 * à chaque redémarrage — provoquant une collision avec la contrainte d'unicité dès
 * la première création suivant un redémarrage le même jour — ainsi que le calcul
 * {@code count() + 1}, faussé par toute suppression et non sûr en concurrence.</p>
 *
 * <p>Le tirage passe par {@code nextval}, atomique et sûr en concurrence comme en
 * multi-instance : deux transactions simultanées obtiennent toujours deux valeurs
 * distinctes, et un {@code nextval} n'est jamais annulé par un rollback — ce qui
 * peut créer des trous dans la numérotation, mais jamais de doublon. L'unicité
 * prime ici sur la continuité.</p>
 *
 * <p>Le compteur est continu et ne se réinitialise pas chaque jour : la date reste
 * portée par le numéro lui-même ({@code VTE-20260815-000042}). Un compteur
 * journalier exigerait une remise à zéro non atomique, incompatible avec la
 * garantie d'unicité recherchée.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GenerateurNumeroMetier implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(GenerateurNumeroMetier.class);
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int LONGUEUR_COMPTEUR = 6;

    /** Familles dont la séquence est confirmée présente, pour éviter un DDL à chaque tirage. */
    private final Set<FamilleNumero> sequencesPretes = EnumSet.noneOf(FamilleNumero.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Crée les séquences absentes au démarrage, avant tout autre initialiseur.
     *
     * <p>Le schéma étant piloté par {@code ddl-auto=update}, Hibernate ne crée pas
     * les séquences qu'aucune entité ne déclare. La reprise par Flyway est prévue
     * au lot 6 ; d'ici là, cette initialisation idempotente tient ce rôle. La
     * priorité maximale garantit que les jeux de données initiaux, eux aussi
     * exécutés au démarrage, disposent déjà des séquences.</p>
     */
    @Override
    @Transactional
    public void run(String... args) {
        for (FamilleNumero famille : FamilleNumero.values()) {
            creerSequenceSiAbsente(famille);
        }
    }

    /**
     * Tire le prochain numéro d'une famille, au format {@code PREFIXE-yyyyMMdd-NNNNNN}.
     *
     * <p>Rejoint la transaction de l'appelant lorsqu'il en existe une, afin que le
     * numéro et l'entité qui le porte soient écrits dans la même unité de travail.</p>
     *
     * @param famille famille de numéros concernée
     * @param date    date métier portée par le numéro (jamais la date système, afin
     *                qu'une opération saisie hors ligne conserve sa date réelle)
     */
    @Transactional
    public String genererNumero(FamilleNumero famille, LocalDate date) {
        creerSequenceSiAbsente(famille);

        Number valeur = (Number) entityManager
            .createNativeQuery("SELECT nextval('" + famille.getNomSequence() + "')")
            .getSingleResult();

        return String.format("%s-%s-%0" + LONGUEUR_COMPTEUR + "d",
            famille.getPrefixe(),
            (date != null ? date : LocalDate.now()).format(FORMAT_DATE),
            valeur.longValue());
    }

    /**
     * Filet de sécurité : une base recréée en cours d'exécution, ou un contexte de test
     * qui n'exécute pas les {@code CommandLineRunner}, ne doit pas faire échouer la
     * génération. L'opération est idempotente et n'est tentée qu'une fois par famille.
     */
    private void creerSequenceSiAbsente(FamilleNumero famille) {
        if (sequencesPretes.contains(famille)) {
            return;
        }
        entityManager
            .createNativeQuery("CREATE SEQUENCE IF NOT EXISTS " + famille.getNomSequence()
                + " START WITH 1 INCREMENT BY 1 NO CYCLE")
            .executeUpdate();
        sequencesPretes.add(famille);
        log.debug("Séquence de numérotation prête : {}", famille.getNomSequence());
    }
}
