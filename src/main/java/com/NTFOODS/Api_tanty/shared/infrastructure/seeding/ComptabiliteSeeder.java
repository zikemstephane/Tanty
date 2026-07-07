package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.enums.TypeMouvementCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.JournalCaisseJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.JournalCaisseJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa.LigneCaisseJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa.DecaissementJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa.DecaissementJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.FactureJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.FactureJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa.LigneFactureJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.PrimeJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa.PrimeJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa.VersementJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa.VersementJpaRepository;
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
@Order(7)
public class ComptabiliteSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(ComptabiliteSeeder.class);

    private final FactureJpaRepository factureRepo;
    private final JournalCaisseJpaRepository caisseRepo;
    private final DecaissementJpaRepository decaissementRepo;
    private final ObjectifCommercialJpaRepository objectifRepo;
    private final PrimeJpaRepository primeRepo;
    private final VersementJpaRepository versementRepo;

    public ComptabiliteSeeder(FactureJpaRepository factureRepo,
                              JournalCaisseJpaRepository caisseRepo,
                              DecaissementJpaRepository decaissementRepo,
                              ObjectifCommercialJpaRepository objectifRepo,
                              PrimeJpaRepository primeRepo,
                              VersementJpaRepository versementRepo) {
        this.factureRepo = factureRepo;
        this.caisseRepo = caisseRepo;
        this.decaissementRepo = decaissementRepo;
        this.objectifRepo = objectifRepo;
        this.primeRepo = primeRepo;
        this.versementRepo = versementRepo;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();
        seedFactures(now);
        seedJournalCaisse(now);
        seedDecaissements(now);
        seedObjectifs(now);
        seedPrimes(now);
        seedVersements(now);
        log.info("[ComptabiliteSeeder] Done: {} factures, {} caisses, {} decaissements, {} objectifs, {} primes, {} versements",
                factureRepo.count(), caisseRepo.count(), decaissementRepo.count(),
                objectifRepo.count(), primeRepo.count(), versementRepo.count());
    }

    private void seedFactures(LocalDateTime now) {
        Object[][] data = {
                {"FA-202501-0001", "CLI-001", TypeFacture.FACTURE, null, StatutFacture.PAYEE, "COM-001"},
                {"FA-202501-0002", "CLI-002", TypeFacture.FACTURE, null, StatutFacture.EMISE, "COM-002"},
                {"FA-202501-0003", "CLI-003", TypeFacture.FACTURE, null, StatutFacture.PAYEE, "COM-003"},
                {"FA-202501-0004", "CLI-001", TypeFacture.FACTURE, null, StatutFacture.EMISE, "COM-001"},
                {"AV-202501-0001", "CLI-002", TypeFacture.AVOIR, "FA-202501-0002", StatutFacture.EMISE, "COM-002"},
        };
        String[][][] lignesData = {
                {{"ART-001", "Farine de ble 1kg", "50", "500", "18", "0"}, {"ART-010", "Eau minerale 50cl", "100", "300", "18", "0"}},
                {{"ART-004", "Farine de mais 1kg", "30", "450", "18", "0"}, {"ART-020", "Jus d'orange 25cl", "50", "250", "18", "5"}},
                {{"ART-010", "Eau minerale 50cl", "200", "300", "18", "0"}, {"ART-011", "Eau minerale 1L", "100", "500", "18", "0"}},
                {{"ART-001", "Farine de ble 1kg", "80", "500", "18", "0"}},
                {{"ART-004", "Farine de mais 1kg", "10", "450", "18", "0"}},
        };
        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            String num = (String) d[0];
            if (factureRepo.findByNumeroFacture(num).isPresent()) continue;
            List<LigneFactureJpaEntity> lignes = new ArrayList<>();
            for (String[] ld : lignesData[i]) {
                lignes.add(LigneFactureJpaEntity.builder()
                        .referenceArticle(ld[0])
                        .designation(ld[1])
                        .quantite(Integer.parseInt(ld[2]))
                        .prixUnitaireHT(Double.parseDouble(ld[3]))
                        .tauxTVA(Double.parseDouble(ld[4]))
                        .remise(Double.parseDouble(ld[5]))
                        .build());
            }
            factureRepo.save(FactureJpaEntity.builder()
                    .numeroFacture(num)
                    .codeClient((String) d[1])
                    .typeFacture((TypeFacture) d[2])
                    .referenceFactureOrigine((String) d[3])
                    .dateEmission(LocalDate.now().minusDays(5))
                    .lignes(lignes)
                    .statut((StatutFacture) d[4])
                    .matriculeEmetteur((String) d[5])
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedJournalCaisse(LocalDateTime now) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        if (!caisseRepo.existsByDate(yesterday)) {
            List<LigneCaisseJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneCaisseJpaEntity.builder()
                    .referenceOperation("VRS-0001")
                    .typeMouvement(TypeMouvementCaisse.ENTREE)
                    .montant(150000)
                    .motif("Versement commercial COM-001")
                    .dateOperation(yesterday.atTime(10, 0))
                    .matriculeOperateur("SEC-001")
                    .build());
            lignes.add(LigneCaisseJpaEntity.builder()
                    .referenceOperation("VRS-0002")
                    .typeMouvement(TypeMouvementCaisse.ENTREE)
                    .montant(80000)
                    .motif("Versement commercial COM-002")
                    .dateOperation(yesterday.atTime(14, 0))
                    .matriculeOperateur("SEC-001")
                    .build());
            lignes.add(LigneCaisseJpaEntity.builder()
                    .referenceOperation("DEC-0001")
                    .typeMouvement(TypeMouvementCaisse.SORTIE)
                    .montant(50000)
                    .motif("Carburant vehicule")
                    .dateOperation(yesterday.atTime(16, 0))
                    .matriculeOperateur("SEC-001")
                    .build());
            caisseRepo.save(JournalCaisseJpaEntity.builder()
                    .date(yesterday)
                    .soldeCourant(180000)
                    .seuilSecurisation(100000)
                    .lignes(lignes)
                    .dateCreation(now)
                    .build());
        }
        if (!caisseRepo.existsByDate(today)) {
            List<LigneCaisseJpaEntity> lignes = new ArrayList<>();
            lignes.add(LigneCaisseJpaEntity.builder()
                    .referenceOperation("VRS-0003")
                    .typeMouvement(TypeMouvementCaisse.ENTREE)
                    .montant(120000)
                    .motif("Versement commercial COM-003")
                    .dateOperation(today.atTime(9, 0))
                    .matriculeOperateur("SEC-001")
                    .build());
            caisseRepo.save(JournalCaisseJpaEntity.builder()
                    .date(today)
                    .soldeCourant(300000)
                    .seuilSecurisation(100000)
                    .lignes(lignes)
                    .dateCreation(now)
                    .build());
        }
    }

    private void seedDecaissements(LocalDateTime now) {
        Object[][] data = {
                {"DEC-0001", TypeDepense.CARBURANT, "CG-001", "DG-001", "DG-001", "SEC-001", 50000, "Station Total", StatutDecaissement.EXECUTE, "Carburant vehicules commerciaux"},
                {"DEC-0002", TypeDepense.ACHAT_MATIERE_PREMIERE, "CG-001", "DG-001", null, null, 500000, "FOUR-001", StatutDecaissement.VALIDEE_DG, "Achat grains de ble"},
                {"DEC-0003", TypeDepense.SALAIRES, "CG-001", null, null, null, 1200000, "Personnel", StatutDecaissement.VALIDEE_CG, "Salaires mensuels"},
                {"DEC-0004", TypeDepense.MAINTENANCE_EQUIPEMENT, "CG-001", null, null, null, 75000, "Technicien externe", StatutDecaissement.PROPOSE, "Reparation broyeur"},
                {"DEC-0005", TypeDepense.TRANSPORT, "CG-001", "DG-001", null, null, 30000, "Transporteur X", StatutDecaissement.ANNULE, "Annule - budget depasse"},
        };
        for (Object[] d : data) {
            String num = (String) d[0];
            if (decaissementRepo.findByNumeroDecaissement(num).isPresent()) continue;
            StatutDecaissement statut = (StatutDecaissement) d[8];
            decaissementRepo.save(DecaissementJpaEntity.builder()
                    .numeroDecaissement(num)
                    .typeDepense((TypeDepense) d[1])
                    .matriculeProposant((String) d[2])
                    .matriculeValidateurCG((String) d[3])
                    .matriculeValidateurDG((String) d[4])
                    .matriculeExecuteur((String) d[5])
                    .montantFCFA((Double) d[6])
                    .beneficiaire((String) d[7])
                    .dateDepense(LocalDate.now().minusDays(3))
                    .motif((String) d[9])
                    .motifAnnulation(statut == StatutDecaissement.ANNULE ? "Budget depasse" : null)
                    .statut(statut)
                    .dateCreation(now)
                    .dateModification(statut != StatutDecaissement.PROPOSE ? now.minusHours(2) : null)
                    .build());
        }
    }

    private void seedObjectifs(LocalDateTime now) {
        LocalDate semaineDebut = LocalDate.now().minusDays(7);
        LocalDate semaineFin = semaineDebut.plusDays(6);
        String[] commercials = {"COM-001", "COM-002", "COM-003", "COM-004", "COM-005"};
        double[] globals = {500000, 400000, 350000, 300000, 250000};
        for (int i = 0; i < commercials.length; i++) {
            String mat = commercials[i];
            if (objectifRepo.findByMatriculeCommercialAndSemaineDebut(mat, semaineDebut).isPresent()) continue;
            ObjectifCommercialJpaEntity obj = new ObjectifCommercialJpaEntity();
            obj.setMatriculeCommercial(mat);
            obj.setSemaineDebut(semaineDebut);
            obj.setSemaineFin(semaineFin);
            obj.setObjectifGlobalFCFA(globals[i]);
            obj.setObjectifsParGammeFCFA(Map.of("Farines", globals[i] * 0.4, "Boissons", globals[i] * 0.3, "Autres", globals[i] * 0.3));
            obj.setMatriculeAuteur("DG-001");
            obj.setDateCreation(now);
            objectifRepo.save(obj);
        }
    }

    private void seedPrimes(LocalDateTime now) {
        LocalDate semaineDebut = LocalDate.now().minusDays(7);
        LocalDate semaineFin = semaineDebut.plusDays(6);
        Object[][] data = {
                {"PRM-COM-001-001", "COM-001", 500000, 480000, 24000, StatutPrime.VALIDEE, "DG-001"},
                {"PRM-COM-002-001", "COM-002", 400000, 350000, 17500, StatutPrime.CALCULEE, null},
                {"PRM-COM-003-001", "COM-003", 350000, 360000, 18000, StatutPrime.VERSEE, "DG-001"},
                {"PRM-COM-004-001", "COM-004", 300000, 250000, 12500, StatutPrime.CALCULEE, null},
                {"PRM-COM-005-001", "COM-005", 250000, 200000, 10000, StatutPrime.VALIDEE, "DG-001"},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (primeRepo.findByReferencePrime(ref).isPresent()) continue;
            primeRepo.save(PrimeJpaEntity.builder()
                    .referencePrime(ref)
                    .matriculeCommercial((String) d[1])
                    .semaineDebut(semaineDebut)
                    .semaineFin(semaineFin)
                    .objectifGlobalFCFA((Double) d[2])
                    .totalVentesGlobalFCFA((Double) d[3])
                    .objectifsParGammeFCFA(Map.of("Farines", (Double) d[2] * 0.4, "Boissons", (Double) d[2] * 0.3, "Autres", (Double) d[2] * 0.3))
                    .realisationsParGammeFCFA(Map.of("Farines", (Double) d[3] * 0.4, "Boissons", (Double) d[3] * 0.3, "Autres", (Double) d[3] * 0.3))
                    .montantPrimeFCFA((Double) d[4])
                    .statut((StatutPrime) d[5])
                    .matriculeValidateur((String) d[6])
                    .dateCreation(now)
                    .dateModification(d[5] != StatutPrime.CALCULEE ? now.minusHours(1) : null)
                    .build());
        }
    }

    private void seedVersements(LocalDateTime now) {
        Object[][] data = {
                {"VRS-0001", "COM-001", 150000, 150000, TypeVersement.ESPECES, StatutVersement.VALIDE, null, "SEC-001"},
                {"VRS-0002", "COM-002", 80000, 75000, TypeVersement.ESPECES, StatutVersement.VALIDE, null, "SEC-001"},
                {"VRS-0003", "COM-003", 120000, 115000, TypeVersement.MOBILE_MONEY, StatutVersement.VALIDE, null, "SEC-001"},
                {"VRS-0004", "COM-004", 60000, 45000, TypeVersement.ESPECES, StatutVersement.ALERTE_ROUGE, null, null},
                {"VRS-0005", "COM-005", 90000, 90000, TypeVersement.CHEQUE, StatutVersement.EN_ATTENTE, null, null},
                {"VRS-0006", "COM-001", 100000, 95000, TypeVersement.ESPECES, StatutVersement.JUSTIFIE, "Client en retard de paiement", null},
        };
        for (Object[] d : data) {
            String ref = (String) d[0];
            if (versementRepo.findByReferenceVersement(ref).isPresent()) continue;
            double montantAttendu = (Double) d[2];
            double cashVerse = (Double) d[3];
            double ecart = cashVerse - montantAttendu;
            boolean alerte = Math.abs(ecart) > 5000 && cashVerse < montantAttendu;
            versementRepo.save(VersementJpaEntity.builder()
                    .referenceVersement(ref)
                    .matriculeCommercial((String) d[1])
                    .montantAttendu(montantAttendu)
                    .cashVerse(cashVerse)
                    .ecart(ecart)
                    .alerteRouge(alerte)
                    .typeVersement((TypeVersement) d[4])
                    .date(LocalDate.now().minusDays(1))
                    .statut((StatutVersement) d[5])
                    .justificationEcart((String) d[6])
                    .matriculeValidateur((String) d[7])
                    .dateCreation(now)
                    .dateModification(d[5] != StatutVersement.EN_ATTENTE && d[5] != StatutVersement.ALERTE_ROUGE ? now.minusHours(1) : null)
                    .build());
        }
    }
}
