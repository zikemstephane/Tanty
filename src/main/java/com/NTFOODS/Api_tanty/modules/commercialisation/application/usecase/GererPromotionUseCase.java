package com.NTFOODS.Api_tanty.modules.commercialisation.application.usecase;

import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CreerPromotionRequest;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.PromotionResponse;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.Promotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GererPromotionUseCase {

    private final PromotionRepository repository;

    public GererPromotionUseCase(PromotionRepository repository) {
        this.repository = repository;
    }

    public PromotionResponse creer(CreerPromotionRequest request, String matriculeCreateur) {
        if (repository.trouverParCode(request.code()).isPresent()) {
            throw new IllegalArgumentException("Une promotion avec le code " + request.code() + " existe déjà");
        }
        Promotion p = Promotion.creer(
            request.code(), request.libelle(), request.description(), request.type(),
            request.valeurReduction(), request.codeProduit(), request.segmentCible(),
            request.dateDebut(), request.dateFin(), request.limiteUtilisations(), matriculeCreateur
        );
        return versResponse(repository.sauvegarder(p));
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> listerTous() {
        return repository.listerTous().stream().map(this::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> listerParStatut(StatutPromotion statut) {
        return repository.listerParStatut(statut).stream().map(this::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> listerActives() {
        return repository.listerActives().stream().map(this::versResponse).toList();
    }

    @Transactional(readOnly = true)
    public PromotionResponse consulter(String code) {
        return repository.trouverParCode(code)
            .map(this::versResponse)
            .orElseThrow(() -> new IllegalArgumentException("Promotion introuvable: " + code));
    }

    public PromotionResponse activer(String code) {
        Promotion p = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Promotion introuvable: " + code));
        p.activer();
        return versResponse(repository.sauvegarder(p));
    }

    public PromotionResponse suspendre(String code) {
        Promotion p = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Promotion introuvable: " + code));
        p.suspendre();
        return versResponse(repository.sauvegarder(p));
    }

    public PromotionResponse annuler(String code) {
        Promotion p = repository.trouverParCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Promotion introuvable: " + code));
        p.annuler();
        return versResponse(repository.sauvegarder(p));
    }

    private PromotionResponse versResponse(Promotion p) {
        return new PromotionResponse(
            p.getId(), p.getCode(), p.getLibelle(), p.getDescription(),
            p.getType(), p.getValeurReduction(), p.getCodeProduit(),
            p.getSegmentCible(), p.getDateDebut(), p.getDateFin(),
            p.getStatut(), p.getNombreUtilisations(), p.getLimiteUtilisations(),
            p.getMatriculeCreateur(), p.getDateCreation()
        );
    }
}
