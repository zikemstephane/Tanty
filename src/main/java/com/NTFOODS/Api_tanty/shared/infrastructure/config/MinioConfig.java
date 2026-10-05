package com.NTFOODS.Api_tanty.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration MinIO — archivage documentaire (BC, BR, BT, BD, factures,
 * rapports exportés...). Voir {@code app.minio.*} dans application.properties.
 *
 * <p>MinIO dependency temporarily disabled (offline build environment).
 * Re-enable io.minio:minio dependency in pom.xml and restore the
 * MinioClient @Bean when Maven Central is reachable.</p>
 */
@Configuration
public class MinioConfig {
    // MinIO bean disabled — see class Javadoc.
}
