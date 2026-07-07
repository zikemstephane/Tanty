package com.NTFOODS.Api_tanty.modules.secretaire.application;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.secretaire.application.dto.SecretaireDashboardResponse;
import com.NTFOODS.Api_tanty.modules.secretaire.application.dto.SecretaireDashboardResponse.*;
import com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa.DocumentRecuJpaRepository;
import com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa.MicroStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa.PetiteCaisseJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SecretaireDashboardService {

    private static final double SEUIL_SECURISATION = 500_000.0;

    private final VersementRepository versementRepository;
    private final PetiteCaisseJpaRepository petiteCaisseJpaRepository;
    private final MicroStockJpaRepository microStockJpaRepository;
    private final DocumentRecuJpaRepository documentRecuJpaRepository;

    public SecretaireDashboardService(VersementRepository versementRepository,
                                       PetiteCaisseJpaRepository petiteCaisseJpaRepository,
                                       MicroStockJpaRepository microStockJpaRepository,
                                       DocumentRecuJpaRepository documentRecuJpaRepository) {
        this.versementRepository = versementRepository;
        this.petiteCaisseJpaRepository = petiteCaisseJpaRepository;
        this.microStockJpaRepository = microStockJpaRepository;
        this.documentRecuJpaRepository = documentRecuJpaRepository;
    }

    public SecretaireDashboardResponse dashboard() {
        var versements = versementRepository.listerParDate(LocalDate.now());
        int versementsEnAttente = (int) versements.stream()
            .filter(v -> v.getStatut() == StatutVersement.EN_ATTENTE).count();
        int alertesRouges = (int) versements.stream()
            .filter(Versement::isAlerteRouge).count();
        double totalVerse = versements.stream().mapToDouble(Versement::getCashVerse).sum();

        var petiteCaisse = petiteCaisse();
        double totalEntrees = petiteCaisse.stream()
            .filter(p -> p.type().equals("ENTREE")).mapToDouble(PetiteCaisseResponse::montant).sum();
        double totalSorties = petiteCaisse.stream()
            .filter(p -> p.type().equals("SORTIE")).mapToDouble(PetiteCaisseResponse::montant).sum();
        double soldeCaisse = totalEntrees - totalSorties;
        boolean seuilDepasse = soldeCaisse > SEUIL_SECURISATION;

        var microStock = microStock();
        int microStockBas = (int) microStock.stream().filter(m -> m.qte() <= m.seuil()).count();
        double valeurMicroStock = microStock.stream().mapToDouble(m -> m.qte() * m.prixUnite()).sum();

        var documents = documents();
        int docsIncomplets = (int) documents.stream()
            .filter(d -> !d.feuilleRoute() || !d.bonCommande() || !d.factures()).count();

        return new SecretaireDashboardResponse(
            versementsEnAttente, 0, alertesRouges, totalVerse,
            totalEntrees, totalSorties, soldeCaisse, seuilDepasse,
            microStockBas, valeurMicroStock, docsIncomplets,
            petiteCaisse, microStock, documents
        );
    }

    public List<PetiteCaisseResponse> petiteCaisse() {
        return petiteCaisseJpaRepository.findAll().stream()
            .map(p -> new PetiteCaisseResponse(
                p.getId(), p.getLibelle(), p.getType(), p.getMontant(),
                p.getHeure(), p.getCategorie(), p.getDateOperation()))
            .collect(Collectors.toList());
    }

    public List<MicroStockResponse> microStock() {
        return microStockJpaRepository.findAll().stream()
            .map(m -> new MicroStockResponse(
                m.getId(), m.getCode(), m.getDesignation(), m.getQte(), m.getPrixUnite(), m.getSeuil()))
            .collect(Collectors.toList());
    }

    public List<DocumentRecuResponse> documents() {
        return documentRecuJpaRepository.findAll().stream()
            .map(d -> new DocumentRecuResponse(
                d.getId(), d.getMatriculeCommercial(), d.getNom(),
                d.isFeuilleRoute(), d.isBonCommande(), d.isFactures()))
            .collect(Collectors.toList());
    }
}
