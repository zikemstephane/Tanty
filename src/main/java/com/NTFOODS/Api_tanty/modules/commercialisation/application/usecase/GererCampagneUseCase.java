package com.NTFOODS.Api_tanty.modules.commercialisation.application.usecase;

import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CampagneResponse;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CreerCampagneRequest;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.CampagneMarketing;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.CampagneMarketingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class GererCampagneUseCase {

    private final CampagneMarketingRepository repository;

    public GererCampagneUseCase(CampagneMarketingRepository repository) {
        this.repository = repository;
    }

    public CampagneResponse creer(CreerCampagneRequest request, String matriculeCreateur) {
        if (repository.trouverParCode(request.code()).isPresent()) {
            throw new IllegalArgumentException("Une campagne avec le code " + request.code() + " existe déjà");
        }
        CampagneMarketing c = CampagneMarketing.creer(
            request.code(), request.libelle(), request.description(), request.type(),
            request.dateDebut(), request.dateFin(), request.zoneCible(),
            request.budgetPrevuFCFA(), matriculeCreateur
        );
        return versResponse(repository.sauvegarder(c));
    }

    @Transactional(readOnly = true)
    public List<CampagneResponse> listerTous() {
        return repository.listerTous().stream().map(this::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CampagneResponse> listerParStatut(StatutCampagne statut) {
        return repository.listerParStatut(statut).stream().map(this::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public CampagneResponse consulter(String code) {
        return repository.trouverParCode(code)
            .map(this::versResponse)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
    }

    public CampagneResponse demarrer(String code) {
        CampagneMarketing c = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
        c.demarrer();
        return versResponse(repository.sauvegarder(c));
    }

    public CampagneResponse terminer(String code) {
        CampagneMarketing c = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
        c.terminer();
        return versResponse(repository.sauvegarder(c));
    }

    public CampagneResponse annuler(String code) {
        CampagneMarketing c = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
        c.annuler();
        return versResponse(repository.sauvegarder(c));
    }

    public CampagneResponse enregistrerDepense(String code, double montantFCFA) {
        CampagneMarketing c = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
        c.enregistrerDepense(montantFCFA);
        return versResponse(repository.sauvegarder(c));
    }

    public CampagneResponse lierPromotion(String code, String codePromotion) {
        CampagneMarketing c = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Campagne introuvable: " + code));
        c.lierPromotion(codePromotion);
        return versResponse(repository.sauvegarder(c));
    }

    @Transactional(readOnly = true)
    public List<CampagneResponse> listerParPeriode(LocalDate debut, LocalDate fin) {
        return repository.listerParPeriode(debut, fin).stream().map(this::versResponse).toList();
    }

    private CampagneResponse versResponse(CampagneMarketing c) {
        return new CampagneResponse(
            c.getId(), c.getCode(), c.getLibelle(), c.getDescription(),
            c.getType(), c.getDateDebut(), c.getDateFin(), c.getZoneCible(),
            c.getBudgetPrevuFCFA(), c.getBudgetDepenseFCFA(), c.getTauxConsommationBudget(),
            c.getStatut(), c.getMatriculeCreateur(), c.getDateCreation(),
            c.getPromotionsLiees()
        );
    }
}
