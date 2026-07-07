package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa.ClientJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa.ClientJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.BoutiquePotentielleJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.BoutiquePotentielleJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.ZoneDeVenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa.ZoneDeVenteJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa.ProspectJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa.ProspectJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.GrilleTarifaireJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.GrilleTarifaireJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa.TerminalJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa.TerminalJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa.VehiculeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa.VehiculeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(3)
public class CommercialBaseSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(CommercialBaseSeeder.class);

    private final ClientJpaRepository clientRepo;
    private final ProspectJpaRepository prospectRepo;
    private final GrilleTarifaireJpaRepository grilleRepo;
    private final ZoneDeVenteJpaRepository zoneRepo;
    private final BoutiquePotentielleJpaRepository boutiqueRepo;
    private final VehiculeJpaRepository vehiculeRepo;
    private final TerminalJpaRepository terminalRepo;

    public CommercialBaseSeeder(ClientJpaRepository clientRepo,
                                ProspectJpaRepository prospectRepo,
                                GrilleTarifaireJpaRepository grilleRepo,
                                ZoneDeVenteJpaRepository zoneRepo,
                                BoutiquePotentielleJpaRepository boutiqueRepo,
                                VehiculeJpaRepository vehiculeRepo,
                                TerminalJpaRepository terminalRepo) {
        this.clientRepo = clientRepo;
        this.prospectRepo = prospectRepo;
        this.grilleRepo = grilleRepo;
        this.zoneRepo = zoneRepo;
        this.boutiqueRepo = boutiqueRepo;
        this.vehiculeRepo = vehiculeRepo;
        this.terminalRepo = terminalRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedClients(now);
        seedProspects(now);
        seedGrilleTarifaire(now);
        seedZonesDeVente(now);
        seedBoutiques(now);
        seedVehicules(now);
        seedTerminaux(now);
        log.info("[CommercialBaseSeeder] Done: {} clients, {} prospects, {} grilles, {} zones, {} boutiques, {} vehicules, {} terminaux",
                clientRepo.count(), prospectRepo.count(), grilleRepo.count(), zoneRepo.count(),
                boutiqueRepo.count(), vehiculeRepo.count(), terminalRepo.count());
    }

    private void seedClients(LocalDateTime now) {
        Object[][] data = {
                {"CLI-001", "Epicerie Bon Marche", "+237691111111", "Marche Central", "Douala", TypeClient.DISTRIBUTEUR, "COM-001"},
                {"CLI-002", "Supermarche Le Panier", "+237692222222", "Akwa", "Douala", TypeClient.GROSSISTE, "COM-001"},
                {"CLI-003", "Boutique Chez Paul", "+237693333333", "Bonanjo", "Douala", TypeClient.DETAIL, "COM-002"},
                {"CLI-004", "Quincaillerie & Alim. La Source", "+237694444444", "Bepanda", "Douala", TypeClient.SEMI_GROSSISTE, "COM-002"},
                {"CLI-005", "Mini-marche du Quartier", "+237695555555", "Makepe", "Douala", TypeClient.DETAIL, "COM-003"},
                {"CLI-006", "Distribution Tropicale SARL", "+237696666666", "Bonapriso", "Douala", TypeClient.DISTRIBUTEUR, "COM-001"},
                {"CLI-007", "Chez Mama Adele", "+237697777777", "Deido", "Douala", TypeClient.DETAIL, "COM-002"},
                {"CLI-008", "Grossiste Nord SARL", "+237698888888", "Quartier Commercial", "Garoua", TypeClient.GROSSISTE, "COM-003"},
                {"CLI-009", "Boutique La Joie", "+237699999999", "Mvan", "Yaounde", TypeClient.DETAIL, "COM-004"},
                {"CLI-010", "Supermarche Bon Prix", "+237680000001", "Bastos", "Yaounde", TypeClient.SEMI_GROSSISTE, "COM-004"},
                {"CLI-011", "Epicerie Le Terroir", "+237680000002", "Mokolo", "Yaounde", TypeClient.DETAIL, "COM-004"},
                {"CLI-012", "Revendeur Marche Mfoundi", "+237680000003", "Mfoundi", "Yaounde", TypeClient.REVENDEUR, "COM-004"},
                {"CLI-013", "Distribution Ouest SARL", "+237680000004", "Marche A", "Bafoussam", TypeClient.DISTRIBUTEUR, "COM-005"},
                {"CLI-014", "Boutique Les Delices", "+237680000005", "Centre Ville", "Bafoussam", TypeClient.DETAIL, "COM-005"},
                {"CLI-015", "Grossiste Ouest SARL", "+237680000006", "Zone Commerciale", "Bafoussam", TypeClient.GROSSISTE, "COM-005"},
                {"CLI-016", "Alimentation La Fontaine", "+237680000007", "Akwa", "Douala", TypeClient.DETAIL, "COM-001"},
                {"CLI-017", "Chez Tonton SARL", "+237680000008", "Bonamoussadi", "Douala", TypeClient.SEMI_GROSSISTE, "COM-001"},
                {"CLI-018", "Pavillon Frais", "+237680000009", "Ngousso", "Yaounde", TypeClient.DETAIL, "COM-004"},
                {"CLI-019", "Marchand Express", "+237680000010", "Nkoulouloun", "Yaounde", TypeClient.REVENDEUR, "COM-004"},
                {"CLI-020", "SARL Commerciale du Nord", "+237680000011", "Plateau", "Garoua", TypeClient.SEMI_GROSSISTE, "COM-003"},
        };
        for (Object[] d : data) {
            String code = (String) d[0];
            if (clientRepo.existsByCodeClient(code)) continue;
            clientRepo.save(ClientJpaEntity.builder()
                    .codeClient(code)
                    .nom((String) d[1])
                    .telephone((String) d[2])
                    .adresse((String) d[3])
                    .localite((String) d[4])
                    .type((TypeClient) d[5])
                    .matriculeCommercialReferent((String) d[6])
                    .actif(true)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedProspects(LocalDateTime now) {
        Object[][] data = {
                {"PROS-001", "Boulangerie Moderne", "+237680000020", "Akwa", "Douala", "COM-001", CanalProspection.VISITE_TERRAIN, StatutProspect.NOUVEAU, null, "Bon potentiel, tres interesse"},
                {"PROS-002", "Snack Le Coin", "+237680000021", "Bonanjo", "Douala", "COM-001", CanalProspection.APPEL_TELEPHONIQUE, StatutProspect.NOUVEAU, null, "A recontacter"},
                {"PROS-003", "Restaurant Chez Kadi", "+237680000022", "Deido", "Douala", "COM-002", CanalProspection.RECOMMANDATION, StatutProspect.CONVERTI, "CLI-007", "Converti en client"},
                {"PROS-004", "Bar Le Relais", "+237680000023", "Bepanda", "Douala", "COM-002", CanalProspection.VISITE_TERRAIN, StatutProspect.NON_INTERESSE, null, "Pas interesse pour le moment"},
                {"PROS-005", "Cantine Scolaire Les Sages", "+237680000024", "Mvan", "Yaounde", "COM-004", CanalProspection.RESEAU_SOCIAL, StatutProspect.NOUVEAU, null, "Trouve sur Facebook"},
                {"PROS-006", "Patisserie Douceur", "+237680000025", "Bastos", "Yaounde", "COM-004", CanalProspection.VISITE_TERRAIN, StatutProspect.CONVERTI, "CLI-010", "Converti en client"},
                {"PROS-007", "Hotel Mont Febe", "+237680000026", "Bonapriso", "Douala", "COM-001", CanalProspection.APPEL_TELEPHONIQUE, StatutProspect.NOUVEAU, null, "Gros volume potentiel"},
                {"PROS-008", "Marche de Gros Mokolo", "+237680000027", "Mokolo", "Yaounde", "COM-004", CanalProspection.VISITE_TERRAIN, StatutProspect.ARCHIVE, null, "Archive - non repondu 3x"},
                {"PROS-009", "Boutique La Perle", "+237680000028", "Centre Ville", "Bafoussam", "COM-005", CanalProspection.RECOMMANDATION, StatutProspect.NOUVEAU, null, "Recommande par CLI-014"},
                {"PROS-010", "Superette Express", "+237680000029", "Ngousso", "Yaounde", "COM-004", CanalProspection.VISITE_TERRAIN, StatutProspect.NOUVEAU, null, "Interesse par farines et jus"},
        };
        for (Object[] d : data) {
            String code = (String) d[0];
            if (prospectRepo.findByCodeProspect(code).isPresent()) continue;
            prospectRepo.save(ProspectJpaEntity.builder()
                    .codeProspect(code)
                    .nom((String) d[1])
                    .telephone((String) d[2])
                    .adresse((String) d[3])
                    .localite((String) d[4])
                    .matriculeCommercial((String) d[5])
                    .canal((CanalProspection) d[6])
                    .statut((StatutProspect) d[7])
                    .codeClientConverti((String) d[8])
                    .observations((String) d[9])
                    .dateProspection(LocalDate.now().minusDays(15))
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedGrilleTarifaire(LocalDateTime now) {
        String[] productCodes = {"ART-001", "ART-002", "ART-003", "ART-004", "ART-005", "ART-010", "ART-011", "ART-012", "ART-020", "ART-021", "ART-022", "ART-023", "ART-030", "ART-031", "ART-040", "ART-041", "ART-050"};
        TypeClient[] segments = {TypeClient.DISTRIBUTEUR, TypeClient.GROSSISTE, TypeClient.SEMI_GROSSISTE, TypeClient.DETAIL, TypeClient.REVENDEUR};
        double[][] prices = {
                {1000, 1100, 1150, 1200, 1200},
                {4800, 5000, 5200, 5500, 5500},
                {9000, 9500, 10000, 10500, 10500},
                {800, 900, 950, 1000, 1000},
                {3800, 4000, 4200, 4500, 4500},
                {200, 250, 270, 300, 300},
                {400, 430, 470, 500, 500},
                {550, 600, 650, 700, 700},
                {300, 330, 370, 400, 400},
                {1200, 1300, 1400, 1500, 1500},
                {300, 330, 370, 400, 400},
                {1200, 1300, 1400, 1500, 1500},
                {180, 200, 220, 250, 250},
                {180, 200, 220, 250, 250},
                {650, 700, 750, 800, 800},
                {600, 650, 700, 750, 750},
                {230, 250, 280, 300, 300},
        };
        for (int i = 0; i < productCodes.length; i++) {
            for (int j = 0; j < segments.length; j++) {
                if (grilleRepo.findByCodeProduitAndSegment(productCodes[i], segments[j]).isPresent()) continue;
                grilleRepo.save(GrilleTarifaireJpaEntity.builder()
                        .codeProduit(productCodes[i])
                        .segment(segments[j])
                        .prixHomologueFCFA(prices[i][j])
                        .actif(true)
                        .matriculeAuteur("DC-001")
                        .dateCreation(now)
                        .build());
            }
        }
    }

    private void seedZonesDeVente(LocalDateTime now) {
        Object[][] data = {
                {"Douala Centre", "URBAIN", "Quartiers centraux de Douala", 120},
                {"Douala Peripherie", "URBAIN", "Quartiers peripheriques de Douala", 200},
                {"Yaounde Centre", "URBAIN", "Quartiers centraux de Yaounde", 150},
                {"Yaounde Peripherie", "URBAIN", "Quartiers peripheriques de Yaounde", 180},
                {"Bafoussam", "URBAIN", "Zone de Bafoussam", 80},
                {"Garoua", "URBAIN", "Zone de Garoua", 60},
                {"Zone Semi-Rurale Sud", "SEMI_RURAL", "Zones semi-rurales du Sud", 40},
                {"Zone Semi-Rurale Nord", "SEMI_RURAL", "Zones semi-rurales du Nord", 35},
        };
        for (Object[] d : data) {
            String nom = (String) d[0];
            if (zoneRepo.findByNom(nom).isPresent()) continue;
            zoneRepo.save(ZoneDeVenteJpaEntity.builder()
                    .nom(nom)
                    .type((String) d[1])
                    .description((String) d[2])
                    .clientsPotentiels((Integer) d[3])
                    .actif(true)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedBoutiques(LocalDateTime now) {
        Object[][] data = {
                {"Boutique Akwa 1", "Akwa", "Rue de la Gare, Akwa", "RP-001"},
                {"Boutique Akwa 2", "Akwa", "Carrefour Akwa", "RP-001"},
                {"Boutique Bonanjo", "Bonanjo", "Boulevard de la Liberte", "RP-001"},
                {"Boutique Deido", "Deido", "Carrefour Deido", "RP-001"},
                {"Boutique Bepanda", "Bepanda", "Marche Bepanda", "RP-001"},
                {"Boutique Bastos", "Bastos", "Rue Bastos", "RP-001"},
                {"Boutique Mvan", "Mvan", "Carrefour Mvan", "RP-001"},
                {"Boutique Mokolo", "Mokolo", "Marche Mokolo", "RP-001"},
                {"Boutique Centre Ville Bfo", "Centre Ville", "Avenue du Marche, Bafoussam", "RP-001"},
                {"Boutique Garoua Commercial", "Quartier Commercial", "Marche Central, Garoua", "RP-001"},
        };
        for (Object[] d : data) {
            boutiqueRepo.save(BoutiquePotentielleJpaEntity.builder()
                    .nom((String) d[0])
                    .secteur((String) d[1])
                    .adresse((String) d[2])
                    .matriculeRecenseur((String) d[3])
                    .actif(true)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedVehicules(LocalDateTime now) {
        Object[][] data = {
                {"CE 1234 AA", "Toyota", "Hilux", "COM-001", 8.5},
                {"CE 5678 BB", "Mitsubishi", "L200", "COM-002", 9.0},
                {"CE 9012 CC", "Ford", "Ranger", "COM-003", 8.8},
                {"LT 3456 DD", "Toyota", "Corolla", "COM-004", 6.5},
                {"OU 7890 EE", "Renault", "Kangoo", "COM-005", 6.0},
        };
        for (Object[] d : data) {
            String immat = (String) d[0];
            if (vehiculeRepo.findByImmatriculation(immat).isPresent()) continue;
            vehiculeRepo.save(VehiculeJpaEntity.builder()
                    .immatriculation(immat)
                    .marque((String) d[1])
                    .modele((String) d[2])
                    .matriculeCommercial((String) d[3])
                    .actif(true)
                    .consommationMoyenneL100((Double) d[4])
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedTerminaux(LocalDateTime now) {
        Object[][] data = {
                {"356938035643809", "Samsung Galaxy A14", "COM-001", 85000, 0},
                {"356938035643810", "Samsung Galaxy A14", "COM-002", 85000, 15000},
                {"356938035643811", "Tecno Spark 10", "COM-003", 65000, 25000},
                {"356938035643812", "Tecno Spark 10", "COM-004", 65000, 0},
                {"356938035643813", "Itel A60", "COM-005", 45000, 20000},
        };
        for (Object[] d : data) {
            String imei = (String) d[0];
            if (terminalRepo.findByImei(imei).isPresent()) continue;
            terminalRepo.save(TerminalJpaEntity.builder()
                    .imei(imei)
                    .modele((String) d[1])
                    .matriculeCommercial((String) d[2])
                    .prixAchat((Double) d[3])
                    .resteAPayer((Double) d[4])
                    .actif(true)
                    .dateCreation(now)
                    .build());
        }
    }
}
