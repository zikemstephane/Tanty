package com.NTFOODS.Api_tanty.modules.production.application.affectation.service;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.UniteProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.PosteProductionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PosteProductionService {

    private final PosteProductionRepository repository;

    public PosteProductionService(PosteProductionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PosteProduction> listerActifs() {
        return repository.listerActifs();
    }

    @Transactional(readOnly = true)
    public List<PosteProduction> listerTous() {
        return repository.listerTous();
    }

    @Transactional(readOnly = true)
    public PosteProduction parCode(String code) {
        return repository.parCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Poste de production inconnu : " + code));
    }

    public PosteProduction creer(String code, String libelle, int effectifReference,
                                  double productionParHeure, double productionJournaliere,
                                  String unite, int ordre, Set<String> categoriesMetier) {
        if (repository.existeParCode(code)) {
            throw new IllegalArgumentException("Le poste " + code + " existe déjà");
        }
        return repository.sauvegarder(PosteProduction.creer(code, libelle, effectifReference,
            productionParHeure, productionJournaliere, unite, ordre, categoriesMetier));
    }

    public PosteProduction modifier(Long id, String code, String libelle, int effectifReference,
                                     double productionParHeure, double productionJournaliere,
                                     String unite, boolean actif, int ordre, Set<String> categoriesMetier) {
        PosteProduction current = repository.parId(id)
            .orElseThrow(() -> new IllegalArgumentException("Poste introuvable : id=" + id));
        if (!current.getCode().equals(code)) {
            repository.parCode(code).filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new IllegalArgumentException("Le code " + code + " est déjà utilisé"); });
        }
        current = new PosteProduction(id, code, libelle, effectifReference, productionParHeure,
            productionJournaliere, unite, actif, ordre, categoriesMetier);
        return repository.sauvegarder(current);
    }

    public void supprimer(Long id) {
        repository.supprimer(id);
    }
}
