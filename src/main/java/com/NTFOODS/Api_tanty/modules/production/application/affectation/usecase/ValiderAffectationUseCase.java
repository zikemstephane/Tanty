package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper.AffectationMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Valider une affectation journalière ("Lock Numérique", Pilier 1).
 *
 * Une fois validée, plus aucune saisie de réalisation n'est possible sur
 * cette affectation ({@link AffectationJournaliere#saisirRealisation}
 * refuse tout appel après validation) — les chiffres deviennent une preuve
 * numérique immuable pour les audits.
 */
@Service
@Transactional
public class ValiderAffectationUseCase {

    private final AffectationJournaliereRepository repository;
    private final AffectationMapper mapper;

    public ValiderAffectationUseCase(AffectationJournaliereRepository repository, AffectationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AffectationJournaliereResponse executer(Long affectationId, Utilisateur acteur) {
        if (!peutValider(acteur)) {
            throw new AccesRefuseException(
                "Seul le Chef de Production peut valider une affectation journalière");
        }
        AffectationJournaliere affectation = repository.trouverParId(affectationId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Affectation", String.valueOf(affectationId)));

        affectation.valider();

        return mapper.versResponse(repository.sauvegarder(affectation));
    }

    private boolean peutValider(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("CHEF_PRODUCTION") || role.equals("ADMIN");
    }
}
