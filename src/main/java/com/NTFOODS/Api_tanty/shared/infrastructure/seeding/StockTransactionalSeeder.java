package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.TypeInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.SourceReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.BonCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.BonCommandeJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.LigneBonCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.DotationJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.DotationJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.LigneDotationJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.LigneInventaireJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.SessionInventaireJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.SessionInventaireJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.LigneReceptionJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.ReceptionJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.ReceptionJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.BonRetourJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.BonRetourJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa.LigneRetourJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa.SessionCommercialJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa.SessionCommercialJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.BonTransfertJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.BonTransfertJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa.LigneTransfertJpaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(5)
public class StockTransactionalSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(StockTransactionalSeeder.class);

    private final BonCommandeJpaRepository bonCommandeRepo;
    private final ReceptionJpaRepository receptionRepo;
    private final BonTransfertJpaRepository bonTransfertRepo;
    private final DotationJpaRepository dotationRepo;
    private final BonRetourJpaRepository bonRetourRepo;
    private final SessionCommercialJpaRepository sessionCommercialRepo;
    private final NiveauStockJpaRepository niveauStockRepo;
    private final MouvementStockJpaRepository mouvementRepo;
    private final AlerteJpaRepository alerteRepo;
    private final SessionInventaireJpaRepository sessionInventaireRepo;

    public StockTransactionalSeeder(BonCommandeJpaRepository bonCommandeRepo,
                                    ReceptionJpaRepository receptionRepo,
                                    BonTransfertJpaRepository bonTransfertRepo,
                                    DotationJpaRepository dotationRepo,
                                    BonRetourJpaRepository bonRetourRepo,
                                    SessionCommercialJpaRepository sessionCommercialRepo,
                                    NiveauStockJpaRepository niveauStockRepo,
                                    MouvementStockJpaRepository mouvementRepo,
                                    AlerteJpaRepository alerteRepo,
                                    SessionInventaireJpaRepository sessionInventaireRepo) {
        this.bonCommandeRepo = bonCommandeRepo;
        this.receptionRepo = receptionRepo;
        this.bonTransfertRepo = bonTransfertRepo;
        this.dotationRepo = dotationRepo;
        this.bonRetourRepo = bonRetourRepo;
        this.sessionCommercialRepo = sessionCommercialRepo;
        this.niveauStockRepo = niveauStockRepo;
        this.mouvementRepo = mouvementRepo;
        this.alerteRepo = alerteRepo;
        this.sessionInventaireRepo = sessionInventaireRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedBonsCommande(now);
        seedReceptions(now);
        seedBonsTransfert(now);
        seedDotations(now);
        seedBonsRetour(now);
        seedSessionsCommercial(now);
        seedNiveauxStock(now);
        seedMouvements(now);
        seedAlertes(now);
        seedInventaires(now);
        log.info("[StockTransactionalSeeder] Done: {} BC, {} receptions, {} transferts, {} dotations, {} retours, {} sessions, {} niveaux, {} mouvements, {} alertes, {} inventaires",
                bonCommandeRepo.count(), receptionRepo.count(), bonTransfertRepo.count(),
                dotationRepo.count(), bonRetourRepo.count(), sessionCommercialRepo.count(),
                niveauStockRepo.count(), mouvementRepo.count(), alerteRepo.count(),
                sessionInventaireRepo.count());
    }

    private void seedBonsCommande(LocalDateTime now) {
        Object[][] data = {
                {"BC-0001", "FOUR-001", StatutBonCommande.RECU_TOTAL.name(), "ART-060", "Grain de ble (sac 50kg)", 100, 100, 18000.0},
                {"BC-0002", "FOUR-003", StatutBonCommande.RECU_TOTAL.name(), "ART-061", "Grain de mais (sac 50kg)", 80, 80, 15000.0},
                {"BC-0003", "FOUR-002", StatutBonCommande.ENVOYE.name(), "ART-062", "Sachet plastique 1kg (lot 1000)", 50, 0, 5000.0},
                {"BC-0004", "FOUR-001", StatutBonCommande.RECU_PARTIEL.name(), "ART-060", "Grain de ble (sac 50kg)", 120, 80, 18000.0},
                {"BC-0005", "FOUR-005", StatutBonCommande.BROUILLON.name(), "ART-063", "Bouteille PET 50cl (lot 500)", 30, 0, 8000.0},
        };
        for (Object[] d : data) {
            String numero = (String) d[0];
            if (bonCommandeRepo.findByNumero(numero).isPresent()) continue;
            List<LigneBonCommandeJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneBonCommandeJpaEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .codeProduit((String) d[4])
                    .designation((String) d[5])
                    .sku((String) d[4] + "-CARTON")
                    .quantiteCommandee((Long) d[6])
                    .quantiteRecue((Long) d[7])
                    .prixUnitaireHT(BigDecimal.valueOf((Double) d[8]))
                    .build());
            BonCommandeJpaEntity bc = BonCommandeJpaEntity.builder()
                    .numero(numero)
                    .codeFournisseur((String) d[1])
                    .statut((String) d[2])
                    .dateCommande(LocalDate.now().minusDays(10))
                    .dateLivraisonPrevue(LocalDate.now().minusDays(3))
                    .matriculeCreateur("GS-001")
                    .lignes(lignes)
                    .dateCreation(now)
                    .build();
            lignes.get(0).setBonCommande(bc);
            bonCommandeRepo.save(bc);
        }
    }

    private void seedReceptions(LocalDateTime now) {
        Object[][] data = {
                {"REC-0001", SourceReception.FOURNISSEUR.name(), "MATIERE_PREMIERE", "FOUR-001", "BC-0001", null, "ENT-CENTRAL-01", "VALIDEE", "GS-001", "CG-001"},
                {"REC-0002", SourceReception.FOURNISSEUR.name(), "MATIERE_PREMIERE", "FOUR-003", "BC-0002", null, "ENT-CENTRAL-01", "VALIDEE", "GS-001", "CG-001"},
                {"REC-0003", SourceReception.FOURNISSEUR.name(), "CONSOMMABLE", "FOUR-002", "BC-0003", null, "ENT-CENTRAL-01", "EN_ATTENTE_VALIDATION_1", "GS-001", null},
                {"REC-0004", SourceReception.PRODUCTION.name(), "PRODUIT_FINI", null, null, "LOT-0001", "ENT-CENTRAL-01", "EN_ATTENTE_VALIDATION_2", "GS-001", "CP-001"},
                {"REC-0005", SourceReception.FOURNISSEUR.name(), "MATIERE_PREMIERE", "FOUR-001", "BC-0004", null, "ENT-CENTRAL-01", "REJETEE", "GS-001", null},
        };
        String[][] products = {
                {"ART-060", "ART-060-CARTON"},
                {"ART-061", "ART-061-CARTON"},
                {"ART-062", "ART-062-UNITE_BASE"},
                {"ART-001", "ART-001-UNITE_BASE"},
                {"ART-060", "ART-060-CARTON"},
        };
        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            String numero = (String) d[0];
            if (receptionRepo.findByNumero(numero).isPresent()) continue;
            String statut = (String) d[7];
            List<LigneReceptionJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneReceptionJpaEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .codeProduit(products[i][0])
                    .sku(products[i][1])
                    .quantiteAttendue(100)
                    .quantiteRecue(statut.equals("REJETEE") ? 80 : 100)
                    .build());
            ReceptionJpaEntity r = ReceptionJpaEntity.builder()
                    .numero(numero)
                    .source((String) d[1])
                    .categorie((String) d[2])
                    .codeFournisseur((String) d[3])
                    .numeroBonCommande((String) d[4])
                    .numeroLotProduction((String) d[5])
                    .codeEntrepotCible((String) d[6])
                    .statut(statut)
                    .lignes(lignes)
                    .matriculeValidateur1((String) d[8])
                    .matriculeValidateur2((String) d[9])
                    .motifRejet(statut.equals("REJETEE") ? "Ecart quantite trop important" : null)
                    .seuilEcartPourcentage(5.0)
                    .dateReception(LocalDate.now().minusDays(5))
                    .dateCreation(now)
                    .build();
            lignes.get(0).setReception(r);
            receptionRepo.save(r);
        }
    }

    private void seedBonsTransfert(LocalDateTime now) {
        Object[][] data = {
                {"BT-0001", "ENT-CENTRAL-01", "ENT-TAMPON-01", StatutTransfert.EXECUTE.name(), "GS-001", false, "ART-001", 200},
                {"BT-0002", "ENT-CENTRAL-01", "ENT-TAMPON-01", StatutTransfert.EXECUTE.name(), "GS-001", false, "ART-010", 300},
                {"BT-0003", "ENT-CENTRAL-01", "ENT-TAMPON-01", StatutTransfert.CREE.name(), "GS-001", true, "ART-004", 150},
                {"BT-0004", "ENT-CENTRAL-01", "ENT-TAMPON-01", StatutTransfert.ANNULE.name(), "GS-001", false, "ART-020", 100},
        };
        for (Object[] d : data) {
            String numero = (String) d[0];
            if (bonTransfertRepo.findByNumero(numero).isPresent()) continue;
            List<LigneTransfertJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneTransfertJpaEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .codeProduit((String) d[6])
                    .sku((String) d[6] + "-UNITE_BASE")
                    .quantite((Long) d[7])
                    .build());
            BonTransfertJpaEntity bt = BonTransfertJpaEntity.builder()
                    .numero(numero)
                    .codeEntrepotSource((String) d[1])
                    .codeEntrepotCible((String) d[2])
                    .statut((String) d[3])
                    .matriculeGestionnaire((String) d[4])
                    .declencheParSeuil((Boolean) d[5])
                    .lignes(lignes)
                    .dateCreation(now)
                    .dateExecution(d[3].equals(StatutTransfert.EXECUTE.name()) ? now.minusDays(1) : null)
                    .build();
            lignes.get(0).setBonTransfert(bt);
            bonTransfertRepo.save(bt);
        }
    }

    private void seedDotations(LocalDateTime now) {
        Object[][] data = {
                {"DOT-0001", "COM-001", StatutDotation.APPROUVEE.name(), "GS-001"},
                {"DOT-0002", "COM-002", StatutDotation.APPROUVEE.name(), "GS-001"},
                {"DOT-0003", "COM-003", StatutDotation.SOUMISE.name(), null},
                {"DOT-0004", "COM-004", StatutDotation.APPROUVEE.name(), "GS-001"},
                {"DOT-0005", "COM-005", StatutDotation.REFUSEE.name(), "GS-001"},
        };
        Object[][] products = {
                {"ART-001", "ART-001-UNITE_BASE", 50L},
                {"ART-010", "ART-010-UNITE_BASE", 100L},
                {"ART-004", "ART-004-UNITE_BASE", 30L},
                {"ART-020", "ART-020-UNITE_BASE", 80L},
                {"ART-011", "ART-011-UNITE_BASE", 60L},
        };
        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            String numero = (String) d[0];
            if (dotationRepo.findByNumero(numero).isPresent()) continue;
            String statut = (String) d[2];
            List<LigneDotationJpaEntity> lignes = new ArrayList<>();
            Object[] prod = products[i];
            long qteDemandee = (Long) prod[2];
            lignes.add(LigneDotationJpaEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .codeProduit((String) prod[0])
                    .sku((String) prod[1])
                    .quantiteDemandee(qteDemandee)
                    .quantiteApprouvee(statut.equals(StatutDotation.APPROUVEE.name()) ? qteDemandee : 0)
                    .build());
            DotationJpaEntity dot = DotationJpaEntity.builder()
                    .numero(numero)
                    .matriculeCommercial((String) d[1])
                    .dateDotation(LocalDate.now().minusDays(2))
                    .dateEffet(LocalDate.now())
                    .statut(statut)
                    .lignes(lignes)
                    .matriculeGestionnaire((String) d[3])
                    .motifRefus(statut.equals(StatutDotation.REFUSEE.name()) ? "Stock insuffisant" : null)
                    .dateCreation(now)
                    .dateDecision(statut.equals(StatutDotation.SOUMISE.name()) ? null : now)
                    .build();
            lignes.get(0).setDotation(dot);
            dotationRepo.save(dot);
        }
    }

    private void seedBonsRetour(LocalDateTime now) {
        Object[][] data = {
                {"BR-0001", "COM-001", "GS-001", "ART-001", "ART-001-UNITE_BASE", 5, "Produits endommages"},
                {"BR-0002", "COM-002", "GS-001", "ART-010", "ART-010-UNITE_BASE", 10, "Emballees erronee"},
                {"BR-0003", "COM-004", "GS-001", "ART-020", "ART-020-UNITE_BASE", 3, "Perimes"},
        };
        for (Object[] d : data) {
            String numero = (String) d[0];
            if (bonRetourRepo.findByNumero(numero).isPresent()) continue;
            List<LigneRetourJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneRetourJpaEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .codeProduit((String) d[3])
                    .sku((String) d[4])
                    .quantite((Long) d[5])
                    .motif((String) d[6])
                    .build());
            BonRetourJpaEntity br = BonRetourJpaEntity.builder()
                    .numero(numero)
                    .matriculeCommercial((String) d[1])
                    .matriculeGestionnaire((String) d[2])
                    .lignes(lignes)
                    .dateRetour(LocalDate.now().minusDays(3))
                    .dateCreation(now)
                    .build();
            lignes.get(0).setBonRetour(br);
            bonRetourRepo.save(br);
        }
    }

    private void seedSessionsCommercial(LocalDateTime now) {
        Object[][] data = {
                {"SC-0001", "COM-001", StatutSession.TERMINEE.name(), "SEC-001", "CPT-001"},
                {"SC-0002", "COM-002", StatutSession.VALIDE_COMPTABLE.name(), "SEC-001", "CPT-001"},
                {"SC-0003", "COM-003", StatutSession.VALIDE_SECRETAIRE.name(), "SEC-001", null},
                {"SC-0004", "COM-004", StatutSession.CLOTUREE.name(), null, null},
                {"SC-0005", "COM-005", StatutSession.OUVERTE.name(), null, null},
                {"SC-0006", "COM-001", StatutSession.PREPARATION.name(), null, null},
        };
        for (Object[] d : data) {
            String numero = (String) d[0];
            if (sessionCommercialRepo.findByNumeroSession(numero).isPresent()) continue;
            sessionCommercialRepo.save(SessionCommercialJpaEntity.builder()
                    .numeroSession(numero)
                    .matriculeCommercial((String) d[1])
                    .datePreparation(LocalDate.now().minusDays(1))
                    .dateEffet(LocalDate.now())
                    .statut(StatutSession.valueOf((String) d[2]))
                    .dotationsIds(new ArrayList<>())
                    .matriculeValidateurSecretaire((String) d[3])
                    .matriculeValidateurComptable((String) d[4])
                    .heureOuverture(now.minusHours(8))
                    .heureCloture(d[2].equals(StatutSession.TERMINEE.name()) ? now.minusHours(1) : null)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedNiveauxStock(LocalDateTime now) {
        String[] centralProducts = {"ART-001", "ART-004", "ART-010", "ART-011", "ART-020", "ART-021", "ART-030", "ART-040", "ART-060", "ART-061"};
        long[] centralQtys = {500, 400, 800, 600, 500, 300, 400, 300, 200, 150};
        String[] tamponProducts = {"ART-001", "ART-004", "ART-010", "ART-020"};
        long[] tamponQtys = {150, 120, 200, 100};

        for (int i = 0; i < centralProducts.length; i++) {
            String code = centralProducts[i];
            if (niveauStockRepo.findByCodeProduitAndCodeEntrepot(code, "ENT-CENTRAL-01").isPresent()) continue;
            niveauStockRepo.save(NiveauStockJpaEntity.builder()
                    .codeProduit(code)
                    .codeEntrepot("ENT-CENTRAL-01")
                    .typeEntrepot("CENTRAL")
                    .quantiteEnBase(centralQtys[i])
                    .dateModification(now)
                    .build());
        }
        for (int i = 0; i < tamponProducts.length; i++) {
            String code = tamponProducts[i];
            if (niveauStockRepo.findByCodeProduitAndCodeEntrepot(code, "ENT-TAMPON-01").isPresent()) continue;
            niveauStockRepo.save(NiveauStockJpaEntity.builder()
                    .codeProduit(code)
                    .codeEntrepot("ENT-TAMPON-01")
                    .typeEntrepot("TAMPON")
                    .quantiteEnBase(tamponQtys[i])
                    .dateModification(now)
                    .build());
        }
        String[] mobileProducts = {"ART-001", "ART-010", "ART-020"};
        long[] mobileQtys = {30, 50, 20};
        String[] mobileEntrepots = {"ENT-MOB-COM1", "ENT-MOB-COM2", "ENT-MOB-COM3"};
        for (int i = 0; i < mobileProducts.length; i++) {
            String code = mobileProducts[i];
            if (niveauStockRepo.findByCodeProduitAndCodeEntrepot(code, mobileEntrepots[i]).isPresent()) continue;
            niveauStockRepo.save(NiveauStockJpaEntity.builder()
                    .codeProduit(code)
                    .codeEntrepot(mobileEntrepots[i])
                    .typeEntrepot("MOBILE")
                    .quantiteEnBase(mobileQtys[i])
                    .dateModification(now)
                    .build());
        }
    }

    private void seedMouvements(LocalDateTime now) {
        Object[][] data = {
                {"MVT-0001", "ENTREE_RECEPTION_FOURNISSEUR", "ART-060", null, "ENT-CENTRAL-01", 100, "GS-001", "BC-0001"},
                {"MVT-0002", "ENTREE_RECEPTION_FOURNISSEUR", "ART-061", null, "ENT-CENTRAL-01", 80, "GS-001", "BC-0002"},
                {"MVT-0003", "TRANSFERT_CENTRAL_VERS_TAMPON", "ART-001", "ENT-CENTRAL-01", "ENT-TAMPON-01", 200, "GS-001", "BT-0001"},
                {"MVT-0004", "TRANSFERT_CENTRAL_VERS_TAMPON", "ART-010", "ENT-CENTRAL-01", "ENT-TAMPON-01", 300, "GS-001", "BT-0002"},
                {"MVT-0005", "SORTIE_DOTATION", "ART-001", "ENT-TAMPON-01", "ENT-MOB-COM1", 50, "GS-001", "DOT-0001"},
                {"MVT-0006", "RETOUR_COMMERCIAL", "ART-001", "ENT-MOB-COM1", "ENT-CENTRAL-01", 5, "GS-001", "BR-0001"},
        };
        for (Object[] d : data) {
            mouvementRepo.save(MouvementStockJpaEntity.builder()
                    .reference((String) d[0])
                    .type((String) d[1])
                    .codeProduit((String) d[2])
                    .codeEntrepotSource((String) d[3])
                    .codeEntrepotCible((String) d[4])
                    .quantiteEnBase((Long) d[5])
                    .matriculeActeur((String) d[6])
                    .referenceDocument((String) d[7])
                    .motif("Seeding initial")
                    .horodatage(now.minusDays(3))
                    .build());
        }
    }

    private void seedAlertes(LocalDateTime now) {
        Object[][] data = {
                {"ALT-0001", "TAMPON_SOUS_SEUIL", "Stock tampon ART-004 sous seuil", "GESTIONNAIRE_STOCK", "ART-004", "ENT-TAMPON-01", "NOUVELLE"},
                {"ALT-0002", "CENTRAL_SOUS_SEUIL", "Stock central ART-061 sous seuil", "GESTIONNAIRE_STOCK", "ART-061", "ENT-CENTRAL-01", "LUE"},
                {"ALT-0003", "BC_RETARD_LIVRAISON", "BC-0003 en retard de livraison", "GESTIONNAIRE_STOCK", null, null, "TRAITEE"},
                {"ALT-0004", "PEREMPTION_IMMINENTE", "Lot LOT-0001 proche peremption", "GESTIONNAIRE_STOCK", "ART-001", "ENT-CENTRAL-01", "NOUVELLE"},
                {"ALT-0005", "ECART_RECEPTION", "Ecart reception REC-0005", "CONTROLEUR_GENERAL", null, "ENT-CENTRAL-01", "NOUVELLE"},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (alerteRepo.findByReference(ref).isPresent()) continue;
            alerteRepo.save(AlerteJpaEntity.builder()
                    .reference(ref)
                    .type((String) d[1])
                    .message((String) d[2])
                    .destinataireRole((String) d[3])
                    .actionSuggeree("Verifier et prendre action")
                    .codeProduit((String) d[4])
                    .codeEntrepot((String) d[5])
                    .referenceLiee(ref)
                    .statut((String) d[6])
                    .dateCreation(now)
                    .dateLecture(d[6].equals("LUE") || d[6].equals("TRAITEE") ? now.minusHours(2) : null)
                    .dateTraitement(d[6].equals("TRAITEE") ? now.minusHours(1) : null)
                    .matriculeTraitant(d[6].equals("TRAITEE") ? "GS-001" : null)
                    .build());
        }
    }

    private void seedInventaires(LocalDateTime now) {
        Object[][] data = {
                {"INV-0001", TypeInventaire.COMPLET.name(), "ENT-CENTRAL-01", StatutInventaire.VALIDE.name(), "DG-001"},
                {"INV-0002", TypeInventaire.TOURNANT.name(), "ENT-TAMPON-01", StatutInventaire.OUVERT.name(), null},
                {"INV-0003", TypeInventaire.CIBLE.name(), "ENT-CENTRAL-01", StatutInventaire.CLOTURE.name(), "DG-001"},
        };
        String[][] invProducts = {
                {"ART-001", "ART-004", "ART-010", "ART-060"},
                {"ART-001", "ART-010"},
                {"ART-020"},
        };
        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            String numero = (String) d[0];
            if (sessionInventaireRepo.findByNumero(numero).isPresent()) continue;
            String statut = (String) d[3];
            List<LigneInventaireJpaEntity> lignes = new ArrayList<>();
            for (String prod : invProducts[i]) {
                lignes.add(LigneInventaireJpaEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .codeProduit(prod)
                        .emplacement("Zone A")
                        .quantiteTheorique(500)
                        .quantiteComptee(statut.equals(StatutInventaire.VALIDE.name()) ? 498L : null)
                        .build());
            }
            SessionInventaireJpaEntity inv = SessionInventaireJpaEntity.builder()
                    .numero(numero)
                    .type((String) d[1])
                    .codeEntrepot((String) d[2])
                    .perimetre("Tous produits")
                    .statut(statut)
                    .lignes(lignes)
                    .matriculeInitiateur("GS-001")
                    .matriculeValidateurDG((String) d[4])
                    .justificationEcarts(statut.equals(StatutInventaire.VALIDE.name()) ? "Ecart mineur de 2 unites" : null)
                    .dateInventaire(LocalDate.now().minusDays(7))
                    .dateCreation(now)
                    .dateValidation(statut.equals(StatutInventaire.VALIDE.name()) ? now.minusDays(5) : null)
                    .build();
            for (LigneInventaireJpaEntity l : lignes) l.setSession(inv);
            sessionInventaireRepo.save(inv);
        }
    }
}
