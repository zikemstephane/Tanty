package com.NTFOODS.Api_tanty.modules.production.application.registre.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request.ModifierRegistreRequest;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response.RegistreProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.registre.mapper.RegistreMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.repository.RegistreProductionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Corrige le registre d'un PPH et d'une date : présence, poste, motif d'absence, heure d'arrivée et quantité
 * réalisée de chaque employé. Refusé une fois la journée décrite passée (le registre devient une preuve d'audit
 * définitive).
 */
@Service
@Transactional
public class ModifierRegistreUseCase {

    private final RegistreProductionRepository repository;
    private final RegistreMapper mapper;

    public ModifierRegistreUseCase(RegistreProductionRepository repository, RegistreMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public RegistreProductionResponse executer(String referencePPH, LocalDate date, ModifierRegistreRequest req) {
        RegistreProduction registre = repository.trouverParPPHEtDate(referencePPH, date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Registre", referencePPH + " / " + date));

        List<LigneRegistreEmploye> lignes = req.lignes().stream()
            .map(l -> new LigneRegistreEmploye(l.matriculeEmploye(), l.nomEmploye(), l.poste(),
                l.present(), l.motifAbsence(), l.heureArrivee(), l.qteRealisee(), l.unite()))
            .toList();

        registre.modifierLignes(lignes, LocalDate.now());
        return mapper.versResponse(repository.sauvegarder(registre));
    }
}
