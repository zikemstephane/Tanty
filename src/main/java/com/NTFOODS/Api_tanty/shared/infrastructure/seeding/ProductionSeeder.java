package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.DureeAffectation;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.StatutAffectation;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.TypeBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.AffectationJournaliereJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.AffectationJournaliereJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.LigneAffectationJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa.ExpressionBesoinJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa.ExpressionBesoinJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.SessionBroyageJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.SessionBroyageJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa.SessionDosageJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa.SessionDosageJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.FicheProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.FicheProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa.LigneFicheProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa.LotProduitFiniJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa.LotJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa.OFJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa.OFJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.LignePPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.LigneRegistreJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.RegistreJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.registre.jpa.RegistreJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Order(6)
public class ProductionSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(ProductionSeeder.class);

    private final PPHJpaRepository pphRepo;
    private final OFJpaRepository ofRepo;
    private final AffectationJournaliereJpaRepository affectationRepo;
    private final SessionBroyageJpaRepository broyageRepo;
    private final SessionDosageJpaRepository dosageRepo;
    private final LotJpaRepository lotRepo;
    private final ExpressionBesoinJpaRepository besoinRepo;
    private final FicheProduitJpaRepository ficheRepo;
    private final RegistreJpaRepository registreRepo;

    public ProductionSeeder(PPHJpaRepository pphRepo,
                            OFJpaRepository ofRepo,
                            AffectationJournaliereJpaRepository affectationRepo,
                            SessionBroyageJpaRepository broyageRepo,
                            SessionDosageJpaRepository dosageRepo,
                            LotJpaRepository lotRepo,
                            ExpressionBesoinJpaRepository besoinRepo,
                            FicheProduitJpaRepository ficheRepo,
                            RegistreJpaRepository registreRepo) {
        this.pphRepo = pphRepo;
        this.ofRepo = ofRepo;
        this.affectationRepo = affectationRepo;
        this.broyageRepo = broyageRepo;
        this.dosageRepo = dosageRepo;
        this.lotRepo = lotRepo;
        this.besoinRepo = besoinRepo;
        this.ficheRepo = ficheRepo;
        this.registreRepo = registreRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedPPH(now);
        seedOFs(now);
        seedAffectations(now);
        seedBroyages(now);
        seedDosages(now);
        seedLots(now);
        seedExpressionsBesoin(now);
        seedFichesProduit(now);
        seedRegistres(now);
        log.info("[ProductionSeeder] Done: {} PPH, {} OF, {} affectations, {} broyages, {} dosages, {} lots, {} besoins, {} fiches, {} registres",
                pphRepo.count(), ofRepo.count(), affectationRepo.count(), broyageRepo.count(),
                dosageRepo.count(), lotRepo.count(), besoinRepo.count(), ficheRepo.count(), registreRepo.count());
    }

    private void seedPPH(LocalDateTime now) {
        String pphRef = "PPH-2025-S01";
        if (pphRepo.findByReferencePPH(pphRef).isPresent()) return;
        List<LignePPHJpaEntity> lignes = new ArrayList<>();
        lignes.add(LignePPHJpaEntity.builder().codeProduit("ART-001").nomProduit("Farine de ble 1kg").objectifSemaine(2000).productionRealisee(1800).build());
        lignes.add(LignePPHJpaEntity.builder().codeProduit("ART-004").nomProduit("Farine de mais 1kg").objectifSemaine(1500).productionRealisee(1400).build());
        lignes.add(LignePPHJpaEntity.builder().codeProduit("ART-010").nomProduit("Eau minerale 50cl").objectifSemaine(5000).productionRealisee(4800).build());
        lignes.add(LignePPHJpaEntity.builder().codeProduit("ART-020").nomProduit("Jus d'orange 25cl").objectifSemaine(3000).productionRealisee(2500).build());

        pphRepo.save(PPHJpaEntity.builder()
                .referencePPH(pphRef)
                .semaine("S01")
                .dateDebut(LocalDate.now().minusDays(7))
                .dateFin(LocalDate.now().minusDays(1))
                .matriculeChefProduction("CP-001")
                .lignes(lignes)
                .statut(StatutPPH.CLOTURE)
                .dateCreation(now)
                .build());

        String pphRef2 = "PPH-2025-S02";
        if (pphRepo.findByReferencePPH(pphRef2).isPresent()) return;
        List<LignePPHJpaEntity> lignes2 = new ArrayList<>();
        lignes2.add(LignePPHJpaEntity.builder().codeProduit("ART-001").nomProduit("Farine de ble 1kg").objectifSemaine(2200).productionRealisee(0).build());
        lignes2.add(LignePPHJpaEntity.builder().codeProduit("ART-004").nomProduit("Farine de mais 1kg").objectifSemaine(1600).productionRealisee(0).build());
        lignes2.add(LignePPHJpaEntity.builder().codeProduit("ART-010").nomProduit("Eau minerale 50cl").objectifSemaine(5500).productionRealisee(0).build());

        pphRepo.save(PPHJpaEntity.builder()
                .referencePPH(pphRef2)
                .semaine("S02")
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(6))
                .matriculeChefProduction("CP-001")
                .lignes(lignes2)
                .statut(StatutPPH.EN_COURS)
                .dateCreation(now)
                .build());
    }

    private void seedOFs(LocalDateTime now) {
        Object[][] data = {
                {"OF-0001", "PPH-2025-S01", "ART-001", "Farine de ble 1kg", 2000, 1800, StatutOF.HONORE},
                {"OF-0002", "PPH-2025-S01", "ART-004", "Farine de mais 1kg", 1500, 1400, StatutOF.HONORE},
                {"OF-0003", "PPH-2025-S01", "ART-010", "Eau minerale 50cl", 5000, 4800, StatutOF.HONORE},
                {"OF-0004", "PPH-2025-S01", "ART-020", "Jus d'orange 25cl", 3000, 2500, StatutOF.HONORE},
                {"OF-0005", "PPH-2025-S02", "ART-001", "Farine de ble 1kg", 2200, 800, StatutOF.EN_COURS},
                {"OF-0006", "PPH-2025-S02", "ART-004", "Farine de mais 1kg", 1600, 0, StatutOF.PLANIFIE},
                {"OF-0007", "PPH-2025-S02", "ART-010", "Eau minerale 50cl", 5500, 1200, StatutOF.EN_COURS},
        };
        for (Object[] d : data) {
            String idOF = (String) d[0];
            if (ofRepo.findByIdOF(idOF).isPresent()) continue;
            ofRepo.save(OFJpaEntity.builder()
                    .idOF(idOF)
                    .referencePPH((String) d[1])
                    .codeProduit((String) d[2])
                    .designationProduit((String) d[3])
                    .qteDemandee((Integer) d[4])
                    .qteRealisee((Integer) d[5])
                    .dateButoir(LocalDate.now().plusDays(3))
                    .statut((StatutOF) d[6])
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedAffectations(LocalDateTime now) {
        String pphRef = "PPH-2025-S02";
        LocalDate today = LocalDate.now();
        if (affectationRepo.existsByReferencePPHAndDate(pphRef, today)) return;

        AffectationJournaliereJpaEntity aff = new AffectationJournaliereJpaEntity();
        aff.setReferencePPH(pphRef);
        aff.setDate(today);
        aff.setMatriculeAgent("AP-001");
        aff.setStatut(StatutAffectation.VALIDEE);
        aff.setDateCreation(now);

        List<LigneAffectationJpaEntity> lignes = new ArrayList<>();
        addLigneAffectation(lignes, aff, CodePoste.CARAMEL, DureeAffectation.JOURNEE_COMPLETE, 65.6, "AP-001");
        addLigneAffectation(lignes, aff, CodePoste.CROQUETTE, DureeAffectation.JOURNEE_COMPLETE, 28.8, "AP-001,MA-001");
        addLigneAffectation(lignes, aff, CodePoste.CHIPS_PLANTAIN, DureeAffectation.DEMI_JOURNEE, 30.0, "MA-002");
        addLigneAffectation(lignes, aff, CodePoste.CONDITIONNEMENT_RASS, DureeAffectation.JOURNEE_COMPLETE, 5000.0, "AP-001");

        aff.setLignes(lignes);
        affectationRepo.save(aff);
    }

    private void addLigneAffectation(List<LigneAffectationJpaEntity> lignes, AffectationJournaliereJpaEntity aff,
                                      CodePoste poste, DureeAffectation duree, double objectif, String matricules) {
        LigneAffectationJpaEntity ligne = new LigneAffectationJpaEntity();
        ligne.setAffectation(aff);
        ligne.setPoste(poste);
        ligne.setDuree(duree);
        ligne.setObjectifAjuste(objectif);
        ligne.setMatriculesEmployesJson(matricules);
        lignes.add(ligne);
    }

    private void seedBroyages(LocalDateTime now) {
        String pphRef = "PPH-2025-S02";
        LocalDate today = LocalDate.now();
        if (broyageRepo.existsByReferencePPHAndDateAndTypePoudre(pphRef, today, TypePoudre.MAIS)) return;

        SessionBroyageJpaEntity session = new SessionBroyageJpaEntity();
        session.setReferencePPH(pphRef);
        session.setDate(today);
        session.setTypePoudre(TypePoudre.MAIS);
        session.setObjectifJournalierKg(500.0);
        session.setMatriculeChefMachiniste("CM-001");
        session.setQuantiteNetteBroyeeKg(420.0);
        session.setPertesKg(15.0);
        session.setStatut(StatutSessionBroyage.CLOTUREE);
        session.setDateCreation(now);
        broyageRepo.save(session);

        SessionBroyageJpaEntity session2 = new SessionBroyageJpaEntity();
        session2.setReferencePPH(pphRef);
        session2.setDate(today);
        session2.setTypePoudre(TypePoudre.SOJA);
        session2.setObjectifJournalierKg(300.0);
        session2.setMatriculeChefMachiniste("CM-001");
        session2.setQuantiteNetteBroyeeKg(null);
        session2.setPertesKg(null);
        session2.setStatut(StatutSessionBroyage.OUVERTE);
        session2.setDateCreation(now);
        broyageRepo.save(session2);
    }

    private void seedDosages(LocalDateTime now) {
        String pphRef = "PPH-2025-S02";
        LocalDate today = LocalDate.now();
        if (dosageRepo.findByReferencePPHAndDate(pphRef, today).isPresent()) return;

        List<SessionDosageJpaEntity.MatierePremiereJpa> matieres = new ArrayList<>();
        matieres.add(SessionDosageJpaEntity.MatierePremiereJpa.builder().typeMatiere("Poudre de mais").quantiteKg(200.0).build());
        matieres.add(SessionDosageJpaEntity.MatierePremiereJpa.builder().typeMatiere("Sucre").quantiteKg(50.0).build());
        matieres.add(SessionDosageJpaEntity.MatierePremiereJpa.builder().typeMatiere("Sel").quantiteKg(5.0).build());

        dosageRepo.save(SessionDosageJpaEntity.builder()
                .referencePPH(pphRef)
                .date(today)
                .matriculeAgentDoseur("AD-001")
                .matieresUtilisees(matieres)
                .machinesMobilisees(List.of("MELANGEUR-01", "DOSEUR-02"))
                .nbFutsProduits(25)
                .justificationEcart(null)
                .validee(true)
                .dateCreation(now)
                .build());
    }

    private void seedLots(LocalDateTime now) {
        Object[][] data = {
                {"LOT-2025-001", "PPH-2025-S01", "ART-001", "Farine de ble 1kg", 1800.0, 150, StatutLot.VALIDATED_BY_STOCK, "CP-001", "GS-001"},
                {"LOT-2025-002", "PPH-2025-S01", "ART-004", "Farine de mais 1kg", 1400.0, 120, StatutLot.VALIDATED_BY_STOCK, "CP-001", "GS-001"},
                {"LOT-2025-003", "PPH-2025-S01", "ART-010", "Eau minerale 50cl", 4800.0, 400, StatutLot.RECEPTIONNE_STOCK, "CP-001", null},
                {"LOT-2025-004", "PPH-2025-S02", "ART-001", "Farine de ble 1kg", 800.0, 67, StatutLot.DECLARE, "CP-001", null},
                {"LOT-2025-005", "PPH-2025-S01", "ART-020", "Jus d'orange 25cl", 2400.0, 200, StatutLot.REJETE, "CP-001", null},
        };
        for (Object[] d : data) {
            String num = (String) d[0];
            if (lotRepo.findByNumeroLot(num).isPresent()) continue;
            LotProduitFiniJpaEntity lot = new LotProduitFiniJpaEntity();
            lot.setNumeroLot(num);
            lot.setReferencePPH((String) d[1]);
            lot.setCodeProduit((String) d[2]);
            lot.setDesignationProduit((String) d[3]);
            lot.setQuantiteKg((Double) d[4]);
            lot.setNbCartons((Integer) d[5]);
            lot.setDateProduction(LocalDate.now().minusDays(3));
            lot.setDlc(LocalDate.now().plusMonths(6));
            lot.setMatriculeChefProduction((String) d[7]);
            lot.setStatut((StatutLot) d[6]);
            lot.setMatriculeGestionnaireValidation((String) d[8]);
            lot.setMotifRejet(d[6] == StatutLot.REJETE ? "Non conformite controle qualite" : null);
            lot.setDateCreation(now);
            lotRepo.save(lot);
        }
    }

    private void seedExpressionsBesoin(LocalDateTime now) {
        Object[][] data = {
                {"EB-0001", "PPH-2025-S02", TypeBesoin.MATIERE_PREMIERE, "ART-060", "Grain de ble (sac 50kg)", 500.0, "KG", "CP-001", StatutBesoin.VALIDE, "GS-001", null},
                {"EB-0002", "PPH-2025-S02", TypeBesoin.MATIERE_PREMIERE, "ART-061", "Grain de mais (sac 50kg)", 300.0, "KG", "CP-001", StatutBesoin.VALIDE, "GS-001", null},
                {"EB-0003", "PPH-2025-S02", TypeBesoin.EMBALLAGE, "ART-062", "Sachet plastique 1kg (lot 1000)", 20.0, "LOT", "AP-001", StatutBesoin.EN_ATTENTE, null, null},
                {"EB-0004", "PPH-2025-S02", TypeBesoin.CONSOMMABLE, "ART-063", "Bouteille PET 50cl (lot 500)", 10.0, "LOT", "AP-001", StatutBesoin.REJETE, "GS-001", "Budget depasse"},
        };
        for (Object[] d : data) {
            String num = (String) d[0];
            if (besoinRepo.existsByNumero(num)) continue;
            besoinRepo.save(ExpressionBesoinJpaEntity.builder()
                    .numero(num)
                    .referencePPH((String) d[1])
                    .type((TypeBesoin) d[2])
                    .codeProduit((String) d[3])
                    .designationProduit((String) d[4])
                    .quantiteDemandee((Double) d[5])
                    .unite((String) d[6])
                    .matriculeDemandeur((String) d[7])
                    .dateBesoin(LocalDate.now().minusDays(2))
                    .motif("Besoin production hebdomadaire")
                    .statut((StatutBesoin) d[8])
                    .matriculeValidateur((String) d[9])
                    .motifRejet((String) d[10])
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedFichesProduit(LocalDateTime now) {
        String pphRef = "PPH-2025-S02";
        LocalDate today = LocalDate.now();
        if (ficheRepo.existsByReferencePPHAndDate(pphRef, today)) return;

        FicheProduitJpaEntity fiche = new FicheProduitJpaEntity();
        fiche.setReferencePPH(pphRef);
        fiche.setSemaine("S02");
        fiche.setDate(today);
        fiche.setMatriculeAgentProduction("AP-001");
        fiche.setProduitsRealises(Map.of("ART-001", 800, "ART-010", 1200));
        fiche.setPredictionDosage(Map.of("ART-001", 2200, "ART-010", 5500));
        fiche.setPredictionPostes(Map.of("ART-001", 2100, "ART-010", 5400));
        fiche.setJustificationEcart(null);
        fiche.setVerrouillee(false);
        fiche.setDateCreation(now);

        List<LigneFicheProduitJpaEntity> lignes = new ArrayList<>();
        LigneFicheProduitJpaEntity l1 = new LigneFicheProduitJpaEntity();
        l1.setCodeProduit("ART-001");
        l1.setNomProduit("Farine de ble 1kg");
        l1.setQuantiteProduite(800.0);
        lignes.add(l1);
        LigneFicheProduitJpaEntity l2 = new LigneFicheProduitJpaEntity();
        l2.setCodeProduit("ART-010");
        l2.setNomProduit("Eau minerale 50cl");
        l2.setQuantiteProduite(1200.0);
        lignes.add(l2);
        fiche.setLignes(lignes);

        ficheRepo.save(fiche);
    }

    private void seedRegistres(LocalDateTime now) {
        String pphRef = "PPH-2025-S02";
        LocalDate today = LocalDate.now();
        if (registreRepo.existsByReferencePPHAndDate(pphRef, today)) return;

        RegistreJpaEntity registre = RegistreJpaEntity.builder()
                .referencePPH(pphRef)
                .date(today)
                .matriculeChefProduction("CP-001")
                .lignesEmployes(new ArrayList<>())
                .dateCreation(now)
                .build();

        addLigneRegistre(registre.getLignesEmployes(), "AP-001", "Kamga Alice", true, null, "Agent Production", "07:30", 800.0, "KG");
        addLigneRegistre(registre.getLignesEmployes(), "CM-001", "Bello Joseph", true, null, "Chef Machiniste", "07:00", 420.0, "KG");
        addLigneRegistre(registre.getLignesEmployes(), "MA-001", "Djoumessi Samuel", true, null, "Machiniste", "07:15", 200.0, "KG");
        addLigneRegistre(registre.getLignesEmployes(), "MA-002", "Tagne Pierre", false, "Maladie", "Machiniste", null, 0.0, "KG");
        addLigneRegistre(registre.getLignesEmployes(), "AD-001", "Etoa David", true, null, "Agent Doseur", "07:30", 25.0, "FUT");

        registreRepo.save(registre);
    }

    private void addLigneRegistre(List<LigneRegistreJpaEntity> lignes, String matricule, String nom,
                                   boolean present, String motifAbsence, String poste, String heureArrivee,
                                   double qteRealisee, String unite) {
        lignes.add(LigneRegistreJpaEntity.builder()
                .matriculeEmploye(matricule)
                .nomEmploye(nom)
                .present(present)
                .motifAbsence(motifAbsence)
                .poste(poste)
                .heureArrivee(heureArrivee)
                .qteRealisee(qteRealisee)
                .unite(unite)
                .build());
    }
}
