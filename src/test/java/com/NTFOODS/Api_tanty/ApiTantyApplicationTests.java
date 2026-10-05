package com.NTFOODS.Api_tanty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Démarrage complet du contexte applicatif contre une base PostgreSQL réelle.
 *
 * <p>Le profil {@code test} pointe sur {@code erp_tanty_test}, distincte de la base
 * de développement : le schéma y est reconstruit à chaque campagne, ce qui serait
 * destructeur sur {@code erp_tanty_db}.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
class ApiTantyApplicationTests {

	@Test
	void contextLoads() {
	}

}
