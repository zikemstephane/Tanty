package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ConditionnementJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa.EntrepotJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa.EntrepotJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa.FournisseurJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa.FournisseurJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.SeuilStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.SeuilStockJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(2)
public class StockBaseSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(StockBaseSeeder.class);

    private final FournisseurJpaRepository fournisseurRepo;
    private final EntrepotJpaRepository entrepotRepo;
    private final ProduitJpaRepository produitRepo;
    private final SeuilStockJpaRepository seuilRepo;

    public StockBaseSeeder(FournisseurJpaRepository fournisseurRepo,
                           EntrepotJpaRepository entrepotRepo,
                           ProduitJpaRepository produitRepo,
                           SeuilStockJpaRepository seuilRepo) {
        this.fournisseurRepo = fournisseurRepo;
        this.entrepotRepo = entrepotRepo;
        this.produitRepo = produitRepo;
        this.seuilRepo = seuilRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedFournisseurs(now);
        seedEntrepots(now);
        seedProduits(now);
        seedSeuils(now);
        log.info("[StockBaseSeeder] Done: {} fournisseurs, {} entrepots, {} produits, {} seuils",
                fournisseurRepo.count(), entrepotRepo.count(), produitRepo.count(), seuilRepo.count());
    }

    private void seedFournisseurs(LocalDateTime now) {
        String[][] data = {
                {"FOUR-001", "SARL Grands Moulins du Cameroun", "+237243000001", "fournisseur1@exemple.cm", "Zone Industrielle, Douala"},
                {"FOUR-002", "EMC Packaging", "+237243000002", "fournisseur2@exemple.cm", "Bonaberi, Douala"},
                {"FOUR-003", "SODECOTON", "+237243000003", "fournisseur3@exemple.cm", "Garoua"},
                {"FOUR-004", "Plantations du Haut Penja (PHP)", "+237243000004", "fournisseur4@exemple.cm", "Nkongsamba"},
                {"FOUR-005", "SARL Agri-Import", "+237243000005", "fournisseur5@exemple.cm", "Akwa, Douala"},
        };
        for (String[] d : data) {
            if (fournisseurRepo.existsByCode(d[0])) continue;
            fournisseurRepo.save(FournisseurJpaEntity.builder()
                    .code(d[0]).raisonSociale(d[1]).telephone(d[2]).email(d[3]).adresse(d[4])
                    .actif(true).dateCreation(now).build());
        }
    }

    private void seedEntrepots(LocalDateTime now) {
        String[][] data = {
                {"ENT-CENTRAL-01", "Entrepot Central Douala", TypeEntrepot.CENTRAL.name(), null},
                {"ENT-TAMPON-01", "Entrepot Tampon Douala", TypeEntrepot.TAMPON.name(), null},
                {"ENT-MOB-COM1", "Stock Mobile COM-001", TypeEntrepot.MOBILE.name(), "COM-001"},
                {"ENT-MOB-COM2", "Stock Mobile COM-002", TypeEntrepot.MOBILE.name(), "COM-002"},
                {"ENT-MOB-COM3", "Stock Mobile COM-003", TypeEntrepot.MOBILE.name(), "COM-003"},
                {"ENT-MOB-COM4", "Stock Mobile COM-004", TypeEntrepot.MOBILE.name(), "COM-004"},
                {"ENT-MOB-COM5", "Stock Mobile COM-005", TypeEntrepot.MOBILE.name(), "COM-005"},
        };
        for (String[] d : data) {
            if (entrepotRepo.existsByCode(d[0])) continue;
            entrepotRepo.save(EntrepotJpaEntity.builder()
                    .code(d[0]).libelle(d[1]).type(d[2])
                    .matriculeProprietaire(d[3])
                    .actif(true).dateCreation(now).build());
        }
    }

    private void seedProduits(LocalDateTime now) {
        Object[][] data = {
                {"ART-001", "NTF", "Farines", "Ble", "Farine de ble 1kg", CategorieProduit.PRODUIT_FINI, 1200.0, 5500.0, 1000.0, "Sachet 1kg", "KG"},
                {"ART-002", "NTF", "Farines", "Ble", "Farine de ble 5kg", CategorieProduit.PRODUIT_FINI, 5500.0, 5000.0, 5000.0, "Sachet 5kg", "KG"},
                {"ART-003", "NTF", "Farines", "Ble", "Farine de ble 10kg", CategorieProduit.PRODUIT_FINI, 10500.0, 10000.0, 10000.0, "Sachet 10kg", "KG"},
                {"ART-004", "NTF", "Farines", "Mais", "Farine de mais 1kg", CategorieProduit.PRODUIT_FINI, 1000.0, 4500.0, 1000.0, "Sachet 1kg", "KG"},
                {"ART-005", "NTF", "Farines", "Mais", "Farine de mais 5kg", CategorieProduit.PRODUIT_FINI, 4500.0, 4000.0, 5000.0, "Sachet 5kg", "KG"},
                {"ART-010", "NTF", "Eau", "Minerale", "Eau minerale 50cl", CategorieProduit.PRODUIT_FINI, 300.0, 250.0, 300.0, "Bouteille 50cl", "L"},
                {"ART-011", "NTF", "Eau", "Minerale", "Eau minerale 1L", CategorieProduit.PRODUIT_FINI, 500.0, 400.0, 500.0, "Bouteille 1L", "L"},
                {"ART-012", "NTF", "Eau", "Minerale", "Eau minerale 1.5L", CategorieProduit.PRODUIT_FINI, 700.0, 600.0, 700.0, "Bouteille 1.5L", "L"},
                {"ART-013", "NTF", "Eau", "Minerale", "Eau minerale pack 6x1.5L", CategorieProduit.PRODUIT_FINI, 4000.0, 3500.0, 9000.0, "Pack 6x1.5L", "L"},
                {"ART-020", "NTF", "Jus", "Orange", "Jus d'orange 25cl", CategorieProduit.PRODUIT_FINI, 400.0, 350.0, 250.0, "Bouteille 25cl", "L"},
                {"ART-021", "NTF", "Jus", "Orange", "Jus d'orange 1L", CategorieProduit.PRODUIT_FINI, 1500.0, 1200.0, 1000.0, "Bouteille 1L", "L"},
                {"ART-022", "NTF", "Jus", "Mangue", "Jus de mangue 25cl", CategorieProduit.PRODUIT_FINI, 400.0, 350.0, 250.0, "Bouteille 25cl", "L"},
                {"ART-023", "NTF", "Jus", "Mangue", "Jus de mangue 1L", CategorieProduit.PRODUIT_FINI, 1500.0, 1200.0, 1000.0, "Bouteille 1L", "L"},
                {"ART-030", "NTF", "Snacks", "Chips", "Chips nature 50g", CategorieProduit.PRODUIT_FINI, 250.0, 200.0, 50.0, "Sachet 50g", "G"},
                {"ART-031", "NTF", "Snacks", "Chips", "Chips paprika 50g", CategorieProduit.PRODUIT_FINI, 250.0, 200.0, 50.0, "Sachet 50g", "G"},
                {"ART-040", "NTF", "Biscuits", "Secs", "Biscuits secs 200g", CategorieProduit.PRODUIT_FINI, 800.0, 700.0, 200.0, "Boite 200g", "G"},
                {"ART-041", "NTF", "Biscuits", "Chocolat", "Biscuits chocolat 150g", CategorieProduit.PRODUIT_FINI, 750.0, 650.0, 150.0, "Boite 150g", "G"},
                {"ART-050", "NTF", "Confiseries", "Bonbons", "Bonbons assortis 100g", CategorieProduit.PRODUIT_FINI, 300.0, 250.0, 100.0, "Sachet 100g", "G"},
                {"ART-060", "NTF", "Matières Premières", "Ble", "Grain de ble (sac 50kg)", CategorieProduit.MATIERE_PREMIERE, 0.0, 0.0, 50000.0, "Sac 50kg", "KG"},
                {"ART-061", "NTF", "Matières Premières", "Mais", "Grain de mais (sac 50kg)", CategorieProduit.MATIERE_PREMIERE, 0.0, 0.0, 50000.0, "Sac 50kg", "KG"},
                {"ART-062", "NTF", "Consommables", "Emballage", "Sachet plastique 1kg (lot 1000)", CategorieProduit.CONSOMMABLE, 0.0, 0.0, 0.0, "Lot 1000", "UNITE"},
                {"ART-063", "NTF", "Consommables", "Emballage", "Bouteille PET 50cl (lot 500)", CategorieProduit.CONSOMMABLE, 0.0, 0.0, 0.0, "Lot 500", "UNITE"},
        };

        for (Object[] d : data) {
            String code = (String) d[0];
            if (produitRepo.existsByCode(code)) continue;

            CategorieProduit cat = (CategorieProduit) d[5];
            ProduitJpaEntity produit = ProduitJpaEntity.builder()
                    .code(code)
                    .marque((String) d[1])
                    .gamme((String) d[2])
                    .variete((String) d[3])
                    .designation((String) d[4])
                    .categorie(cat.name())
                    .actif(true)
                    .prixDistributeur((Double) d[6])
                    .prixGrossiste((Double) d[7])
                    .prixDetaillant((Double) d[8])
                    .tva(true)
                    .conditionnementCommercial((String) d[9])
                    .uniteVenteCommerciale((String) d[10])
                    .dateCreation(now)
                    .conditionnements(new ArrayList<>())
                    .build();

            List<ConditionnementJpaEntity> conds = new ArrayList<>();
            if (cat == CategorieProduit.PRODUIT_FINI) {
                conds.add(buildCond(produit, code, NiveauConditionnement.UNITE_BASE, "Unite de base", 1, (Double) d[8], 0, 0));
                if ((Double) d[8] > 1000) {
                    conds.add(buildCond(produit, code, NiveauConditionnement.CARTON, "Carton de 12", 12, ((Double) d[8]) * 12, 0, 0));
                }
            } else if (cat == CategorieProduit.MATIERE_PREMIERE) {
                conds.add(buildCond(produit, code, NiveauConditionnement.UNITE_BASE, "Kg", 1, 0.0, 1000, 0));
                conds.add(buildCond(produit, code, NiveauConditionnement.CARTON, "Sac 50kg", 50, 0.0, 50000, 0));
            } else {
                conds.add(buildCond(produit, code, NiveauConditionnement.UNITE_BASE, "Unite", 1, 0.0, 0, 0));
            }
            produit.setConditionnements(conds);
            produitRepo.save(produit);
        }
    }

    private ConditionnementJpaEntity buildCond(ProduitJpaEntity produit, String codeProduit,
                                                NiveauConditionnement niveau, String libelle,
                                                long facteur, double prixHt,
                                                double poidsG, double volCm3) {
        return ConditionnementJpaEntity.builder()
                .id(UUID.randomUUID().toString())
                .produit(produit)
                .codeProduit(codeProduit)
                .sku(codeProduit + "-" + niveau.name())
                .niveau(niveau.name())
                .libelle(libelle)
                .facteurConversionVersBase(facteur)
                .poidsUnitaireGrammes(poidsG)
                .volumeUnitaireCm3(volCm3)
                .prixUnitaireHT(BigDecimal.valueOf(prixHt))
                .codeBarres(null)
                .build();
    }

    private void seedSeuils(LocalDateTime now) {
        String[][] data = {
                {"ART-001", TypeEntrepot.CENTRAL.name(), "100"},
                {"ART-002", TypeEntrepot.CENTRAL.name(), "50"},
                {"ART-003", TypeEntrepot.CENTRAL.name(), "30"},
                {"ART-004", TypeEntrepot.CENTRAL.name(), "100"},
                {"ART-005", TypeEntrepot.CENTRAL.name(), "50"},
                {"ART-010", TypeEntrepot.CENTRAL.name(), "200"},
                {"ART-011", TypeEntrepot.CENTRAL.name(), "150"},
                {"ART-012", TypeEntrepot.CENTRAL.name(), "100"},
                {"ART-020", TypeEntrepot.CENTRAL.name(), "200"},
                {"ART-021", TypeEntrepot.CENTRAL.name(), "100"},
                {"ART-030", TypeEntrepot.CENTRAL.name(), "300"},
                {"ART-040", TypeEntrepot.CENTRAL.name(), "150"},
                {"ART-001", TypeEntrepot.TAMPON.name(), "50"},
                {"ART-004", TypeEntrepot.TAMPON.name(), "50"},
                {"ART-010", TypeEntrepot.TAMPON.name(), "100"},
                {"ART-020", TypeEntrepot.TAMPON.name(), "100"},
        };
        for (String[] d : data) {
            if (seuilRepo.findByCodeProduitAndTypeEntrepot(d[0], d[1]).isPresent()) continue;
            seuilRepo.save(SeuilStockJpaEntity.builder()
                    .codeProduit(d[0])
                    .typeEntrepot(d[1])
                    .seuilReapprovisionnement(Long.parseLong(d[2]))
                    .actif(true)
                    .dateModification(now)
                    .build());
        }
    }
}
