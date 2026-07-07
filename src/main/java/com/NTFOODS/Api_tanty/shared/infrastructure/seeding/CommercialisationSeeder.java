package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypeCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.CampagneMarketingJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.CampagneMarketingJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.PromotionJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa.PromotionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Order(8)
public class CommercialisationSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(CommercialisationSeeder.class);

    private final CampagneMarketingJpaRepository campagneRepo;
    private final PromotionJpaRepository promotionRepo;

    public CommercialisationSeeder(CampagneMarketingJpaRepository campagneRepo,
                                   PromotionJpaRepository promotionRepo) {
        this.campagneRepo = campagneRepo;
        this.promotionRepo = promotionRepo;
    }

    @Override
    @Transactional
    public void seed() {
        seedCampagnes();
        seedPromotions();
        log.info("[CommercialisationSeeder] Done: {} campagnes, {} promotions",
                campagneRepo.count(), promotionRepo.count());
    }

    private void seedCampagnes() {
        Object[][] data = {
                {"CAMP-001", "Lancement Farine de Ble", "Campagne lancement nouveau produit", TypeCampagne.LANCEMENT_PRODUIT,
                        LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "Zone Centre", 500000, 150000, StatutCampagne.EN_COURS, "DG-001", "PROMO-001,PROMO-002"},
                {"CAMP-002", "Promo Fetes de Fin d'Annee", "Reductions sur toutes les boissons", TypeCampagne.PROMO_SAISONNIERE,
                        LocalDate.now().plusDays(15), LocalDate.now().plusDays(45), "Toutes zones", 800000, 0, StatutCampagne.PLANIFIEE, "DG-001", "PROMO-003"},
                {"CAMP-003", "Fidelisation Grossistes", "Remises speciales pour gros volumes", TypeCampagne.FIDELISATION,
                        LocalDate.now().minusDays(30), LocalDate.now().minusDays(1), "Zone Nord", 300000, 280000, StatutCampagne.TERMINEE, "DG-001", "PROMO-004"},
                {"CAMP-004", "Prospection Zone Sud", "Ouverture nouveau marche", TypeCampagne.PROSPECTION,
                        LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), "Zone Sud", 400000, 50000, StatutCampagne.EN_COURS, "DG-001", ""},
                {"CAMP-005", "Communication Institutionnelle", "Marque NTFOODS", TypeCampagne.COMMUNICATION,
                        LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), "National", 1000000, 1000000, StatutCampagne.ANNULEE, "DG-001", ""},
        };
        for (Object[] d : data) {
            String code = (String) d[0];
            if (campagneRepo.findByCode(code).isPresent()) continue;
            CampagneMarketingJpaEntity camp = new CampagneMarketingJpaEntity();
            camp.setCode(code);
            camp.setLibelle((String) d[1]);
            camp.setDescription((String) d[2]);
            camp.setType((TypeCampagne) d[3]);
            camp.setDateDebut((LocalDate) d[4]);
            camp.setDateFin((LocalDate) d[5]);
            camp.setZoneCible((String) d[6]);
            camp.setBudgetPrevuFCFA((Double) d[7]);
            camp.setBudgetDepenseFCFA((Double) d[8]);
            camp.setStatut((StatutCampagne) d[9]);
            camp.setMatriculeCreateur((String) d[10]);
            camp.setDateCreation(LocalDate.now());
            camp.setPromotionsLieesCsv((String) d[11]);
            campagneRepo.save(camp);
        }
    }

    private void seedPromotions() {
        Object[][] data = {
                {"PROMO-001", "Remise 10% Farine de Ble", "10% sur farine de ble 1kg", TypePromotion.POURCENTAGE, 10.0, "ART-001", TypeClient.GROSSISTE,
                        LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), StatutPromotion.ACTIVEE, 45, 100, "DG-001"},
                {"PROMO-002", "Achat 5 obtenez 1", "Achetez 5 farines, 1 gratuite", TypePromotion.GRATUITE, 1.0, "ART-001", TypeClient.SEMI_GROSSISTE,
                        LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), StatutPromotion.ACTIVEE, 20, 50, "DG-001"},
                {"PROMO-003", "Remise 500 FCFA Boissons", "500 FCFA de remise sur boissons", TypePromotion.MONTANT_FIXE, 500.0, "ART-010", TypeClient.DETAIL,
                        LocalDate.now().plusDays(15), LocalDate.now().plusDays(45), StatutPromotion.BROUILLON, 0, 200, "DG-001"},
                {"PROMO-004", "Remise Volume Grossistes", "Paliers de remise selon quantite", TypePromotion.REMISE_VOLUME, 15.0, "ART-004", TypeClient.GROSSISTE,
                        LocalDate.now().minusDays(30), LocalDate.now().minusDays(1), StatutPromotion.EXPIREE, 80, 100, "DG-001"},
                {"PROMO-005", "Promo Eau Minerale Suspendue", "Suspendue temporairement", TypePromotion.POURCENTAGE, 5.0, "ART-010", TypeClient.DETAIL,
                        LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), StatutPromotion.SUSPENDUE, 10, 150, "DG-001"},
                {"PROMO-006", "Promo Annulee Jus", "Annulee - stock insuffisant", TypePromotion.MONTANT_FIXE, 200.0, "ART-020", TypeClient.SEMI_GROSSISTE,
                        LocalDate.now().minusDays(3), LocalDate.now().plusDays(7), StatutPromotion.ANNULEE, 0, 100, "DG-001"},
        };
        for (Object[] d : data) {
            String code = (String) d[0];
            if (promotionRepo.findByCode(code).isPresent()) continue;
            PromotionJpaEntity promo = new PromotionJpaEntity();
            promo.setCode(code);
            promo.setLibelle((String) d[1]);
            promo.setDescription((String) d[2]);
            promo.setType((TypePromotion) d[3]);
            promo.setValeurReduction((Double) d[4]);
            promo.setCodeProduit((String) d[5]);
            promo.setSegmentCible((TypeClient) d[6]);
            promo.setDateDebut((LocalDate) d[7]);
            promo.setDateFin((LocalDate) d[8]);
            promo.setStatut((StatutPromotion) d[9]);
            promo.setNombreUtilisations((Integer) d[10]);
            promo.setLimiteUtilisations((Integer) d[11]);
            promo.setMatriculeCreateur((String) d[12]);
            promo.setDateCreation(LocalDate.now());
            promotionRepo.save(promo);
        }
    }
}
