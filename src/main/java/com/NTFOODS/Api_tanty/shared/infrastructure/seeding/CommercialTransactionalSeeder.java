package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.CanalCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.SyncStatus;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa.CarburantJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa.CarburantJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.CommandeDistanceJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.CommandeDistanceJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.LigneCommandeDistanceJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.ArretFeuillRouteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.FeuillRouteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.FeuillRouteJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.LignePreCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.PreCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.PreCommandeJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa.RecouvrementJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa.RecouvrementJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.PrixExceptionnelJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa.PrixExceptionnelJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.FactureAvoirJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.FactureAvoirJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.LigneVenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(4)
public class CommercialTransactionalSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(CommercialTransactionalSeeder.class);

    private final VenteJpaRepository venteRepo;
    private final PreCommandeJpaRepository preCommandeRepo;
    private final CommandeDistanceJpaRepository commandeDistanceRepo;
    private final FeuillRouteJpaRepository feuillRouteRepo;
    private final CarburantJpaRepository carburantRepo;
    private final RecouvrementJpaRepository recouvrementRepo;
    private final FactureAvoirJpaRepository factureAvoirRepo;
    private final PrixExceptionnelJpaRepository prixExceptionnelRepo;

    public CommercialTransactionalSeeder(VenteJpaRepository venteRepo,
                                         PreCommandeJpaRepository preCommandeRepo,
                                         CommandeDistanceJpaRepository commandeDistanceRepo,
                                         FeuillRouteJpaRepository feuillRouteRepo,
                                         CarburantJpaRepository carburantRepo,
                                         RecouvrementJpaRepository recouvrementRepo,
                                         FactureAvoirJpaRepository factureAvoirRepo,
                                         PrixExceptionnelJpaRepository prixExceptionnelRepo) {
        this.venteRepo = venteRepo;
        this.preCommandeRepo = preCommandeRepo;
        this.commandeDistanceRepo = commandeDistanceRepo;
        this.feuillRouteRepo = feuillRouteRepo;
        this.carburantRepo = carburantRepo;
        this.recouvrementRepo = recouvrementRepo;
        this.factureAvoirRepo = factureAvoirRepo;
        this.prixExceptionnelRepo = prixExceptionnelRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedVentes(now);
        seedPreCommandes(now);
        seedCommandesDistance(now);
        seedFeuillesRoute(now);
        seedCarburants(now);
        seedRecouvrements(now);
        seedFacturesAvoir(now);
        seedPrixExceptionnels(now);
        log.info("[CommercialTransactionalSeeder] Done: {} ventes, {} precommandes, {} cmdDistance, {} feuillesRoute, {} carburants, {} recouvrements, {} avoirs, {} prixExceptionnels",
                venteRepo.count(), preCommandeRepo.count(), commandeDistanceRepo.count(),
                feuillRouteRepo.count(), carburantRepo.count(), recouvrementRepo.count(),
                factureAvoirRepo.count(), prixExceptionnelRepo.count());
    }

    private void seedVentes(LocalDateTime now) {
        String[][] clients = {
                {"CLI-001", "COM-001"}, {"CLI-002", "COM-001"}, {"CLI-006", "COM-001"},
                {"CLI-003", "COM-002"}, {"CLI-004", "COM-002"}, {"CLI-007", "COM-002"},
                {"CLI-005", "COM-003"}, {"CLI-008", "COM-003"},
                {"CLI-009", "COM-004"}, {"CLI-010", "COM-004"}, {"CLI-011", "COM-004"},
                {"CLI-013", "COM-005"}, {"CLI-014", "COM-005"},
        };
        String[][] products = {
                {"ART-001", "Farine de ble 1kg", "Sachet 1kg", "1200"},
                {"ART-004", "Farine de mais 1kg", "Sachet 1kg", "1000"},
                {"ART-010", "Eau minerale 50cl", "Bouteille 50cl", "300"},
                {"ART-011", "Eau minerale 1L", "Bouteille 1L", "500"},
                {"ART-020", "Jus d'orange 25cl", "Bouteille 25cl", "400"},
                {"ART-021", "Jus d'orange 1L", "Bouteille 1L", "1500"},
                {"ART-030", "Chips nature 50g", "Sachet 50g", "250"},
                {"ART-040", "Biscuits secs 200g", "Boite 200g", "800"},
        };
        StatutVente[] statuts = {StatutVente.FINALISEE, StatutVente.FINALISEE, StatutVente.FINALISEE,
                StatutVente.FINALISEE, StatutVente.EN_COURS, StatutVente.ANNULEE};
        ModeReglement[] modes = {ModeReglement.COMPTANT, ModeReglement.COMPTANT, ModeReglement.CREDIT,
                ModeReglement.MOBILE_MONEY, ModeReglement.CHEQUE, ModeReglement.NON_APPLICABLE};
        TypeVente[] types = {TypeVente.COMPTANT, TypeVente.COMPTANT, TypeVente.CREDIT,
                TypeVente.COMPTANT, TypeVente.COMPTANT, TypeVente.AVARIE};
        SyncStatus[] syncs = {SyncStatus.SYNCED, SyncStatus.SYNCED, SyncStatus.SYNCED,
                SyncStatus.SYNCED, SyncStatus.PENDING, SyncStatus.CONFLICT};

        int counter = 1;
        for (int day = 0; day < 7; day++) {
            LocalDate date = LocalDate.now().minusDays(day);
            for (String[] client : clients) {
                if (counter > 60) break;
                String numero = "VTE-" + String.format("%04d", counter);
                if (venteRepo.findByNumeroVente(numero).isPresent()) { counter++; continue; }

                int idx = (counter - 1) % statuts.length;
                List<LigneVenteJpaEntity> lignes = new ArrayList<>();
                int nbLignes = 1 + (counter % 3);
                for (int l = 0; l < nbLignes; l++) {
                    String[] prod = products[(counter + l) % products.length];
                    lignes.add(LigneVenteJpaEntity.builder()
                            .codeProduit(prod[0])
                            .designationProduit(prod[1])
                            .conditionnement(prod[2])
                            .quantite(5 + (counter * 3 + l * 7) % 50)
                            .prixUnitaire(Double.parseDouble(prod[3]))
                            .remise(0)
                            .build());
                }

                venteRepo.save(VenteJpaEntity.builder()
                        .numeroVente(numero)
                        .uuidVente(UUID.randomUUID().toString())
                        .matriculeCommercial(client[1])
                        .codeClient(client[0])
                        .date(date)
                        .lignes(lignes)
                        .modeReglement(modes[idx])
                        .statut(statuts[idx])
                        .typeVente(types[idx])
                        .syncStatus(syncs[idx])
                        .coordonneesGPS("4.0511,9.7679")
                        .signatureClient(statuts[idx] == StatutVente.FINALISEE)
                        .observations("")
                        .dateCreation(now)
                        .build());
                counter++;
            }
        }
    }

    private void seedPreCommandes(LocalDateTime now) {
        String[][] commercials = {{"COM-001"}, {"COM-002"}, {"COM-003"}, {"COM-004"}, {"COM-005"}};
        StatutPreCommande[] statuts = {
                StatutPreCommande.SOUMISE, StatutPreCommande.VALIDEE_SECRETAIRE,
                StatutPreCommande.VALIDEE_COMPTABLE, StatutPreCommande.LIVREE,
                StatutPreCommande.REFUSEE
        };
        String[][] products = {
                {"ART-001", "Farine de ble 1kg", "Sachet 1kg"},
                {"ART-004", "Farine de mais 1kg", "Sachet 1kg"},
                {"ART-010", "Eau minerale 50cl", "Bouteille 50cl"},
                {"ART-020", "Jus d'orange 25cl", "Bouteille 25cl"},
        };

        int counter = 1;
        for (int day = 0; day < 5; day++) {
            LocalDate dateSoumission = LocalDate.now().minusDays(day + 1);
            LocalDate dateEffet = dateSoumission.plusDays(1);
            for (String[] com : commercials) {
                if (counter > 20) break;
                String numero = "PRE-" + String.format("%04d", counter);
                if (preCommandeRepo.findByNumeroPreCommande(numero).isPresent()) { counter++; continue; }

                int idx = (counter - 1) % statuts.length;
                List<LignePreCommandeJpaEntity> lignes = new ArrayList<>();
                for (int l = 0; l < 2; l++) {
                    String[] prod = products[(counter + l) % products.length];
                    lignes.add(LignePreCommandeJpaEntity.builder()
                            .codeProduit(prod[0])
                            .designationProduit(prod[1])
                            .conditionnement(prod[2])
                            .quantiteDemandee(10 + (counter * 5) % 40)
                            .quantiteValidee(statuts[idx] == StatutPreCommande.LIVREE ||
                                    statuts[idx] == StatutPreCommande.VALIDEE_COMPTABLE
                                    ? 10 + (counter * 5) % 40 : 0)
                            .build());
                }

                preCommandeRepo.save(PreCommandeJpaEntity.builder()
                        .numeroPreCommande(numero)
                        .matriculeCommercial(com[0])
                        .dateSoumission(dateSoumission)
                        .dateEffet(dateEffet)
                        .lignes(lignes)
                        .statut(statuts[idx])
                        .matriculeValidateurSecretaire(
                                statuts[idx].ordinal() >= StatutPreCommande.VALIDEE_SECRETAIRE.ordinal()
                                        && statuts[idx] != StatutPreCommande.REFUSEE ? "SEC-001" : null)
                        .matriculeValidateurComptable(
                                statuts[idx].ordinal() >= StatutPreCommande.VALIDEE_COMPTABLE.ordinal()
                                        && statuts[idx] != StatutPreCommande.REFUSEE ? "CPT-001" : null)
                        .motifRefus(statuts[idx] == StatutPreCommande.REFUSEE ? "Stock insuffisant" : null)
                        .soumiseEnSoiree(true)
                        .dateCreation(now)
                        .build());
                counter++;
            }
        }
    }

    private void seedCommandesDistance(LocalDateTime now) {
        Object[][] data = {
                {"CLI-001", "COM-001", CanalCommande.APPEL_TELEPHONIQUE, StatutCommandeDistance.RECUE},
                {"CLI-003", "COM-002", CanalCommande.WHATSAPP, StatutCommandeDistance.VALIDEE_SECRETAIRE},
                {"CLI-009", "COM-004", CanalCommande.SMS, StatutCommandeDistance.VALIDEE_COMPTABLE},
                {"CLI-005", "COM-003", CanalCommande.EMAIL, StatutCommandeDistance.LIVREE},
                {"CLI-010", "COM-004", CanalCommande.WHATSAPP, StatutCommandeDistance.ANNULEE},
        };
        String[][] products = {
                {"ART-001", "Farine de ble 1kg", "Sachet 1kg", "1200"},
                {"ART-011", "Eau minerale 1L", "Bouteille 1L", "500"},
                {"ART-021", "Jus d'orange 1L", "Bouteille 1L", "1500"},
        };

        int counter = 1;
        for (Object[] d : data) {
            String numero = "CD-" + String.format("%04d", counter);
            if (commandeDistanceRepo.findByNumeroCommande(numero).isPresent()) { counter++; continue; }

            StatutCommandeDistance statut = (StatutCommandeDistance) d[3];
            List<LigneCommandeDistanceJpaEntity> lignes = new ArrayList<>();
            for (int l = 0; l < 2; l++) {
                String[] prod = products[(counter + l) % products.length];
                lignes.add(LigneCommandeDistanceJpaEntity.builder()
                        .codeProduit(prod[0])
                        .designation(prod[1])
                        .conditionnement(prod[2])
                        .quantite(5 + counter * 3)
                        .prixUnitaire(Double.parseDouble(prod[3]))
                        .build());
            }

            commandeDistanceRepo.save(CommandeDistanceJpaEntity.builder()
                    .numeroCommande(numero)
                    .codeClient((String) d[0])
                    .matriculeCommercial((String) d[1])
                    .canal((CanalCommande) d[2])
                    .dateCommande(LocalDate.now().minusDays(counter))
                    .lignes(lignes)
                    .statut(statut)
                    .matriculeValidateurSecretaire(
                            statut.ordinal() >= StatutCommandeDistance.VALIDEE_SECRETAIRE.ordinal()
                                    && statut != StatutCommandeDistance.ANNULEE ? "SEC-001" : null)
                    .matriculeValidateurComptable(
                            statut.ordinal() >= StatutCommandeDistance.VALIDEE_COMPTABLE.ordinal()
                                    && statut != StatutCommandeDistance.ANNULEE ? "CPT-001" : null)
                    .motifAnnulation(statut == StatutCommandeDistance.ANNULEE ? "Client annule" : null)
                    .observations("Commande a distance")
                    .dateCreation(now)
                    .build());
            counter++;
        }
    }

    private void seedFeuillesRoute(LocalDateTime now) {
        String[][] commercials = {{"COM-001"}, {"COM-002"}, {"COM-003"}, {"COM-004"}, {"COM-005"}};
        StatutFeuillRoute[] statuts = {StatutFeuillRoute.CLOTURE, StatutFeuillRoute.EN_COURS, StatutFeuillRoute.PLANIFIE, StatutFeuillRoute.VERROUILLE};
        String[][] allClients = {
                {"CLI-001"}, {"CLI-002"}, {"CLI-006"}, {"CLI-016"}, {"CLI-017"},
                {"CLI-003"}, {"CLI-004"}, {"CLI-007"},
                {"CLI-005"}, {"CLI-008"},
                {"CLI-009"}, {"CLI-010"}, {"CLI-011"}, {"CLI-018"}, {"CLI-019"},
                {"CLI-013"}, {"CLI-014"}, {"CLI-015"},
        };
        InfoClientEnum[] infoCodes = {InfoClientEnum.Ab, InfoClientEnum.NC, InfoClientEnum.PA, InfoClientEnum.VPC, InfoClientEnum.EV, InfoClientEnum.AV, null, null, null, null};
        StatutArret[] arretStatuts = {StatutArret.VISE, StatutArret.VISE, StatutArret.NON_VISE, StatutArret.PLANIFIE, StatutArret.ANNULE};

        int counter = 1;
        int clientIdx = 0;
        for (int day = 0; day < 3; day++) {
            LocalDate date = LocalDate.now().minusDays(day);
            for (String[] com : commercials) {
                if (counter > 15) break;
                String numero = "FR-" + String.format("%04d", counter);
                if (feuillRouteRepo.findByNumeroFeuillRoute(numero).isPresent()) { counter++; continue; }

                int idx = (counter - 1) % statuts.length;
                List<ArretFeuillRouteJpaEntity> arrets = new ArrayList<>();
                for (int a = 0; a < 4; a++) {
                    String clientCode = allClients[clientIdx % allClients.length][0];
                    clientIdx++;
                    InfoClientEnum infoCode = infoCodes[(counter + a) % infoCodes.length];
                    arrets.add(ArretFeuillRouteJpaEntity.builder()
                            .codeClient(clientCode)
                            .ordre(a + 1)
                            .statut(arretStatuts[(counter + a) % arretStatuts.length])
                            .codeInfoClient(infoCode)
                            .quantiteInfo(InfoClientEnum.requiresQuantity(infoCode) ? 5 : null)
                            .observationsArret(infoCode != null ? infoCode.getLibelle() : "")
                            .heurePlanifiee(date.atTime(8 + a, 0))
                            .heureEffective(idx == 0 ? date.atTime(8 + a, 15) : null)
                            .coordonneesGPS("4.0511,9.7679")
                            .build());
                }

                feuillRouteRepo.save(FeuillRouteJpaEntity.builder()
                        .numeroFeuillRoute(numero)
                        .matriculeCommercial(com[0])
                        .date(date)
                        .arrets(arrets)
                        .statut(statuts[idx])
                        .matriculeCloture(idx == 0 ? com[0] : null)
                        .heureCloture(idx == 0 ? date.atTime(17, 30) : null)
                        .dateCreation(now)
                        .build());
                counter++;
            }
        }
    }

    private void seedCarburants(LocalDateTime now) {
        Object[][] data = {
                {"CAR-0001", "COM-001", "CE 1234 AA", "Total Bonanjo", 12500, 20.0, 13000.0, StatutCarburant.VALIDEE},
                {"CAR-0002", "COM-002", "CE 5678 BB", "MRS Akwa", 18200, 30.0, 19500.0, StatutCarburant.VALIDEE},
                {"CAR-0003", "COM-003", "CE 9012 CC", "Total Garoua", 15000, 25.0, 16250.0, StatutCarburant.EN_ATTENTE_SIGNATURE},
                {"CAR-0004", "COM-004", "LT 3456 DD", "MRS Bastos", 8400, 12.0, 9000.0, StatutCarburant.VALIDEE},
                {"CAR-0005", "COM-005", "OU 7890 EE", "Total Bafoussam", 9600, 14.0, 10200.0, StatutCarburant.REFUSEE},
                {"CAR-0006", "COM-001", "CE 1234 AA", "Total Deido", 11200, 18.0, 11700.0, StatutCarburant.EN_ATTENTE_SIGNATURE},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (carburantRepo.findByReferenceCarburant(ref).isPresent()) continue;
            StatutCarburant statut = (StatutCarburant) d[7];
            carburantRepo.save(CarburantJpaEntity.builder()
                    .referenceCarburant(ref)
                    .matriculeCommercial((String) d[1])
                    .immatriculation((String) d[2])
                    .stationPartenaire((String) d[3])
                    .kilometrage((Integer) d[4])
                    .litresDemandes((Double) d[5])
                    .montant((Double) d[6])
                    .date(LocalDate.now().minusDays(3))
                    .statut(statut)
                    .signatureCommercial(statut == StatutCarburant.VALIDEE ? "data:signature" : null)
                    .signatureSuperviseur(statut == StatutCarburant.VALIDEE ? "data:signature" : null)
                    .motifRefus(statut == StatutCarburant.REFUSEE ? "Kilometrage incoherent" : null)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedRecouvrements(LocalDateTime now) {
        Object[][] data = {
                {"REC-0001", "CLI-002", "Supermarche Le Panier", 150000.0, 50000.0, StatutRecouvrement.REMBOURSE_PARTIEL, "COM-001"},
                {"REC-0002", "CLI-006", "Distribution Tropicale SARL", 320000.0, 0.0, StatutRecouvrement.EN_COURS, "COM-001"},
                {"REC-0003", "CLI-004", "Quincaillerie & Alim. La Source", 85000.0, 85000.0, StatutRecouvrement.REMBOURSE_TOTAL, "COM-002"},
                {"REC-0004", "CLI-008", "Grossiste Nord SARL", 220000.0, 0.0, StatutRecouvrement.CONTENTIEUX, "COM-003"},
                {"REC-0005", "CLI-010", "Supermarche Bon Prix", 95000.0, 30000.0, StatutRecouvrement.REMBOURSE_PARTIEL, "COM-004"},
                {"REC-0006", "CLI-015", "Grossiste Ouest SARL", 180000.0, 0.0, StatutRecouvrement.EN_COURS, "COM-005"},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (recouvrementRepo.findByReferenceRecouvrement(ref).isPresent()) continue;
            recouvrementRepo.save(RecouvrementJpaEntity.builder()
                    .referenceRecouvrement(ref)
                    .codeClient((String) d[1])
                    .nomClient((String) d[2])
                    .montantDu((Double) d[3])
                    .montantRembourse((Double) d[4])
                    .dateEcheance(LocalDate.now().plusDays(15))
                    .statut((StatutRecouvrement) d[5])
                    .matriculeCommercial((String) d[6])
                    .observations("Recouvrement credit commercial")
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedFacturesAvoir(LocalDateTime now) {
        Object[][] data = {
                {"AV-0001", "VTE-0006", "COM-001", "CLI-002", "CPT-001", 15000.0, 2850.0, 17850.0, "Produit endommage lors du transport"},
                {"AV-0002", "VTE-0012", "COM-002", "CLI-004", "CPT-001", 8000.0, 1520.0, 9520.0, "Erreur de quantite livree"},
        };
        for (Object[] d : data) {
            String num = (String) d[0];
            if (factureAvoirRepo.findByNumeroAvoir(num).isPresent()) continue;
            factureAvoirRepo.save(FactureAvoirJpaEntity.builder()
                    .numeroAvoir(num)
                    .numeroVenteOriginale((String) d[1])
                    .matriculeCommercial((String) d[2])
                    .codeClient((String) d[3])
                    .matriculeComptable((String) d[4])
                    .montantHT((Double) d[5])
                    .montantTVA((Double) d[6])
                    .montantTTC((Double) d[7])
                    .motif((String) d[8])
                    .dateAvoir(LocalDate.now().minusDays(2))
                    .statut(FactureAvoir.StatutAvoir.VALIDE)
                    .dateCreation(now)
                    .dateValidation(now)
                    .build());
        }
    }

    private void seedPrixExceptionnels(LocalDateTime now) {
        Object[][] data = {
                {"PX-0001", "ART-001", "CLI-002", 1200.0, 1000.0, "Remise strategique gros volume", "DG-001"},
                {"PX-0002", "ART-011", "CLI-006", 500.0, 400.0, "Promotion lancement zone", "DC-001"},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (prixExceptionnelRepo.findByReference(ref).isPresent()) continue;
            prixExceptionnelRepo.save(PrixExceptionnelJpaEntity.builder()
                    .reference(ref)
                    .codeProduit((String) d[1])
                    .codeClient((String) d[2])
                    .prixHomologueFCFA((Double) d[3])
                    .prixAppliqueFCFA((Double) d[4])
                    .motif((String) d[5])
                    .matriculeAutorisateur((String) d[6])
                    .dateAutorisation(now)
                    .dateCreation(now)
                    .build());
        }
    }
}
