package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.CodeObservation;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.CodeObservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CodeObservationService {

    private final CodeObservationRepository repository;

    public CodeObservationService(CodeObservationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CodeObservation> listerActifs() {
        return repository.listerActifs();
    }

    @Transactional(readOnly = true)
    public List<CodeObservation> listerTous() {
        return repository.listerTous();
    }

    @Transactional(readOnly = true)
    public CodeObservation parCode(String code) {
        return repository.parCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Code observation inconnu : " + code));
    }

    public boolean exigeQuantite(String code) {
        return repository.parCode(code)
            .map(CodeObservation::isRequireQuantity)
            .orElse(false);
    }

    public CodeObservation creer(String code, String libelle, boolean requireQuantity, int ordre) {
        if (repository.existeParCode(code)) {
            throw new IllegalArgumentException("Le code " + code + " existe déjà");
        }
        return repository.sauvegarder(CodeObservation.creer(code, libelle, requireQuantity, ordre));
    }

    public CodeObservation modifier(Long id, String code, String libelle, boolean requireQuantity, boolean actif, int ordre) {
        CodeObservation current = repository.parId(id)
            .orElseThrow(() -> new IllegalArgumentException("Code observation introuvable : id=" + id));
        CodeObservation existing = repository.parCode(code)
            .filter(c -> !c.getId().equals(id))
            .orElse(null);
        if (existing != null) {
            throw new IllegalArgumentException("Le code " + code + " est déjà utilisé");
        }
        current = new CodeObservation(id, code, libelle, requireQuantity, actif, ordre);
        return repository.sauvegarder(current);
    }

    public void supprimer(Long id) {
        repository.supprimer(id);
    }
}
