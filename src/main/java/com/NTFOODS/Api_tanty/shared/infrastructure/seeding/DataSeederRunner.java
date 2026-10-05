package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Injection des jeux d'essai au démarrage.
 *
 * <p>Cette exécution était jusqu'ici inconditionnelle : elle avait donc lieu en
 * production à chaque redémarrage. C'est de là que proviennent, dans la base de
 * travail, des promotions pointant vers des codes produit inexistants et des
 * lots de « farine de blé » ou d'« eau minérale », étrangers au catalogue réel
 * de l'entreprise.</p>
 *
 * <p>Le peuplement est désormais explicite : il n'a lieu que si
 * {@code app.seeding.enabled=true}. Le drapeau est positionné dans le profil de
 * développement ; la production ne reçoit plus de données fictives.</p>
 *
 * <p>Les lignes déjà présentes en base ne sont pas supprimées — ce serait une
 * destruction de données, qui relève d'une décision distincte.</p>
 */
@Component
@Order(1)
@ConditionalOnProperty(name = "app.seeding.enabled", havingValue = "true")
public class DataSeederRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeederRunner.class);

    private final List<DataSeeder> seeders;

    public DataSeederRunner(List<DataSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(String... args) {
        for (String arg : args) {
            if ("--no-seed".equals(arg) || "--seed=false".equals(arg)) {
                log.info("[SEED] Data seeding skipped (--no-seed)");
                return;
            }
        }

        log.info("[SEED] Starting data seeding with {} seeders...", seeders.size());

        for (DataSeeder seeder : seeders) {
            String name = seeder.getClass().getSimpleName();
            try {
                log.info("[SEED] Running {}...", name);
                seeder.seed();
                log.info("[SEED] {} completed", name);
            } catch (Exception e) {
                log.error("[SEED] {} failed: {}", name, e.getMessage(), e);
            }
        }

        log.info("[SEED] Data seeding finished");
    }
}
