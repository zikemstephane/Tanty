package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class DataSeederRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeederRunner.class);

    private final List<DataSeeder> seeders;

    public DataSeederRunner(List<DataSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(String... args) {
        boolean seedRequested = false;
        for (String arg : args) {
            if ("--seed".equals(arg) || "--seed=true".equals(arg)) {
                seedRequested = true;
                break;
            }
        }

        if (!seedRequested) {
            log.info("[SEED] Data seeding skipped (use --seed to enable)");
            return;
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
