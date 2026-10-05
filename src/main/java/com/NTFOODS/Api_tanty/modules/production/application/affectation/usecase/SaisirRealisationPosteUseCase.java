package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.DetailsRealisationPosteRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.SaisirRealisationPosteRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper.AffectationMapper;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.DetailsRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.ProductionIndividuelle;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaisirRealisationPosteUseCase {

    private final AffectationJournaliereRepository repository;
    private final AffectationMapper mapper;
    private final PosteProductionService posteService;

    public SaisirRealisationPosteUseCase(AffectationJournaliereRepository repository,
                                          AffectationMapper mapper,
                                          PosteProductionService posteService) {
        this.repository = repository;
        this.mapper = mapper;
        this.posteService = posteService;
    }

    public AffectationJournaliereResponse executer(SaisirRealisationPosteRequest req, Utilisateur acteur) {
        if (!peutGererAffectation(acteur)) {
            throw new AccesRefuseException("Seul l'responsable de salle peut saisir les réalisations");
        }
        AffectationJournaliere affectation = repository.trouverParId(req.affectationId())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Affectation", String.valueOf(req.affectationId())));

        var poste = posteService.parCode(req.poste());
        LigneAffectationPoste ligne = affectation.getLignePourPoste(req.poste())
            .orElseThrow(() -> new RegleMetierException(
                "Le poste " + poste.getLibelle() + " n'est pas affecté pour cette journée"));

        List<String> warnings = new ArrayList<>();
        if ("CHIPS_PLANTAIN".equals(req.poste())) {
            verifierCoherenceChipsPlantain(req, affectation, warnings);
        }

        List<ProductionIndividuelle> prods = (req.productionsIndividuelles() != null)
            ? req.productionsIndividuelles().stream()
                .map(p -> new ProductionIndividuelle(p.matriculeEmploye(), p.nomEmploye(), p.poste(), p.quantiteRealisee()))
                .toList()
            : List.of();

        DetailsRealisationPoste details = mapDetails(req.details());
        SaisieRealisationPoste saisie = new SaisieRealisationPoste(
            req.poste(), poste.getLibelle(), poste.getUnite(), req.quantiteRealisee(),
            ligne.getObjectifAjuste(), prods, details, warnings);

        affectation.saisirRealisation(saisie);
        return mapper.versResponse(repository.sauvegarder(affectation));
    }

    /**
     * Règle de cohérence Chips Plantain : avertissante.
     * Le nombre de régimes découpés doit être inférieur ou égal au nombre de régimes épluchés
     * enregistré pour ce poste, le même jour. La saisie est acceptée mais un warning est retourné.
     */
    private void verifierCoherenceChipsPlantain(SaisirRealisationPosteRequest req,
                                                AffectationJournaliere affectation,
                                                List<String> warnings) {
        DetailsRealisationPosteRequest details = req.details();
        if (details == null || details.nbRegimesDecoupes() == null) return;

        int nbDecoupes = details.nbRegimesDecoupes();
        int nbEpluches = Optional.ofNullable(details.nbRegimesEpluches()).orElse(0);

        Optional<SaisieRealisationPoste> saisieEpluchage = affectation.getSaisies().stream()
            .filter(s -> "CHIPS_PLANTAIN".equals(s.getPoste()))
            .filter(s -> s.getDetails() != null && s.getDetails().getNbRegimesEpluches() != null)
            .findFirst();

        if (saisieEpluchage.isPresent()) {
            nbEpluches = saisieEpluchage.get().getDetails().getNbRegimesEpluches();
        }

        if (nbDecoupes > nbEpluches) {
            warnings.add("Chips Plantain : le nombre de régimes découpés (" + nbDecoupes
                + ") dépasse le nombre de régimes épluchés (" + nbEpluches + ")");
        }
    }

    private DetailsRealisationPoste mapDetails(DetailsRealisationPosteRequest d) {
        if (d == null) return DetailsRealisationPoste.vide();
        return new DetailsRealisationPoste(
            d.nbRegimesEpluches(), d.nbRegimesDecoupes(),
            d.nbSceauxEtiquetes(), d.nbEtiquettesDatees(),
            d.nbSachetsDates(), d.nbCartonsProduits(),
            d.kgChipsProduits());
    }

    private boolean peutGererAffectation(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("RESPONSABLE_SALLE") || role.equals("CHEF_PRODUCTION") || role.equals("ADMIN");
    }
}
