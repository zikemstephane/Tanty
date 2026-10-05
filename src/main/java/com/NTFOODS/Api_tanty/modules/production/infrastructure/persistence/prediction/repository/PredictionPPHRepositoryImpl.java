package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.prediction.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.prediction.aggregate.PredictionPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.repository.PredictionPPHRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.prediction.jpa.PredictionPPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.prediction.jpa.PredictionPPHJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PredictionPPHRepositoryImpl implements PredictionPPHRepository {

    private final PredictionPPHJpaRepository jpa;

    public PredictionPPHRepositoryImpl(PredictionPPHJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public PredictionPPH enregistrer(PredictionPPH p) {
        return versDomaine(jpa.save(versEntite(p)));
    }

    @Override
    public Optional<PredictionPPH> trouverParReference(String referencePPH) {
        return jpa.findById(referencePPH).map(this::versDomaine);
    }

    @Override
    public List<PredictionPPH> listerToutes() {
        return jpa.findAll().stream().map(this::versDomaine).toList();
    }

    private PredictionPPHJpaEntity versEntite(PredictionPPH p) {
        PredictionPPHJpaEntity e = new PredictionPPHJpaEntity();
        e.setReferencePPH(p.referencePPH());
        e.setNbFuts(p.nbFuts());
        e.setPoudreKg(p.poudreKg());
        e.setDateCalcul(p.dateCalcul());
        e.setMatricule(p.matricule());
        List<PredictionPPHJpaEntity.LigneJpa> lignes = new ArrayList<>();
        for (RepartitionPoudre.Ligne l : p.lignes()) {
            lignes.add(new PredictionPPHJpaEntity.LigneJpa(l.codeProduit(), l.designation(), l.composition(),
                l.poudreAlloueeKg(), l.quantiteUnitesBase(), l.quantiteCartons(), l.poidsUnitaireGrammes(), l.unitesParCarton()));
        }
        e.setLignes(lignes);
        List<PredictionPPHJpaEntity.ExclusionJpa> exclusions = new ArrayList<>();
        for (RepartitionPoudre.Exclusion x : p.exclusions()) {
            exclusions.add(new PredictionPPHJpaEntity.ExclusionJpa(x.codeProduit(), x.motif()));
        }
        e.setExclusions(exclusions);
        return e;
    }

    private PredictionPPH versDomaine(PredictionPPHJpaEntity e) {
        List<RepartitionPoudre.Ligne> lignes = e.getLignes().stream()
            .map(l -> new RepartitionPoudre.Ligne(l.getCodeProduit(), l.getDesignation(), l.getComposition(),
                l.getPoudreAlloueeKg(), l.getQuantiteUnitesBase(), l.getQuantiteCartons(),
                l.getPoidsUnitaireGrammes(), l.getUnitesParCarton()))
            .toList();
        List<RepartitionPoudre.Exclusion> exclusions = e.getExclusions().stream()
            .map(x -> new RepartitionPoudre.Exclusion(x.getCodeProduit(), x.getMotif()))
            .toList();
        return new PredictionPPH(e.getReferencePPH(), e.getNbFuts(), e.getPoudreKg(), e.getDateCalcul(),
            e.getMatricule(), lignes, exclusions);
    }
}
