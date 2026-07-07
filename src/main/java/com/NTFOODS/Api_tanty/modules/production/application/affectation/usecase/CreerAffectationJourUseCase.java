package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.CreerAffectationJourRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper.AffectationMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreerAffectationJourUseCase {

    private final AffectationJournaliereRepository repository;
    private final AffectationMapper mapper;

    public CreerAffectationJourUseCase(AffectationJournaliereRepository repository, AffectationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AffectationJournaliereResponse executer(CreerAffectationJourRequest req, Utilisateur acteur) {
        if (!peutGererAffectation(acteur)) {
            throw new AccesRefuseException("Seul l'responsable de salle peut créer une affectation journalière");
        }
        if (repository.existeParPPHEtDate(req.referencePPH(), req.date())) {
            throw new RegleMetierException("Une affectation existe déjà pour le PPH " +
                req.referencePPH() + " le " + req.date());
        }

        List<LigneAffectationPoste> lignes = req.lignes().stream()
            .map(l -> new LigneAffectationPoste(l.poste(), l.matriculesEmployes(), l.duree()))
            .toList();

        AffectationJournaliere affectation = AffectationJournaliere.creer(
            req.referencePPH(), req.date(), acteur.getMatricule().getValeur(), lignes);

        return mapper.versResponse(repository.sauvegarder(affectation));
    }

    private boolean peutGererAffectation(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("RESPONSABLE_SALLE") || role.equals("CHEF_PRODUCTION") || role.equals("ADMIN");
    }
}
