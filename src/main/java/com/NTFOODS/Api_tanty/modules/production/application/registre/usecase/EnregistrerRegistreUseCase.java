package com.NTFOODS.Api_tanty.modules.production.application.registre.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request.EnregistrerRegistreRequest;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response.RegistreProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.registre.mapper.RegistreMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.repository.RegistreProductionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EnregistrerRegistreUseCase {

    private final RegistreProductionRepository repository;
    private final RegistreMapper mapper;

    public EnregistrerRegistreUseCase(RegistreProductionRepository repository, RegistreMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public RegistreProductionResponse executer(EnregistrerRegistreRequest req, Utilisateur utilisateur) {
        LocalDate date = req.date() != null ? req.date() : LocalDate.now();
        if (repository.existeParPPHEtDate(req.referencePPH(), date)) {
            throw new RegleMetierException("Un registre existe déjà pour le PPH " + req.referencePPH()
                + " à la date " + date);
        }
        List<LigneRegistreEmploye> lignes = req.lignes().stream()
            .map(l -> new LigneRegistreEmploye(l.matriculeEmploye(), l.nomEmploye(),
                l.poste(), l.present(), l.motifAbsence(),
                l.heureArrivee(), l.qteRealisee(), l.unite()))
            .toList();
        RegistreProduction registre = RegistreProduction.enregistrer(
            req.referencePPH(), date, utilisateur.getMatricule().getValeur(), lignes);
        return mapper.versResponse(repository.sauvegarder(registre));
    }
}
