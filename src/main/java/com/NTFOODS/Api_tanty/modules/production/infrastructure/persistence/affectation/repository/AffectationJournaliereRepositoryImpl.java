package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.DetailsRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.ProductionIndividuelle;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class AffectationJournaliereRepositoryImpl implements AffectationJournaliereRepository {

    private final AffectationJournaliereJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public AffectationJournaliereRepositoryImpl(AffectationJournaliereJpaRepository jpaRepository,
                                                ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AffectationJournaliere sauvegarder(AffectationJournaliere affectation) {
        return toDomain(jpaRepository.save(toEntity(affectation)));
    }

    @Override
    public Optional<AffectationJournaliere> trouverParId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<AffectationJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.findByReferencePPHAndDate(referencePPH, date).map(this::toDomain);
    }

    @Override
    public List<AffectationJournaliere> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPHOrderByDateDesc(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    public List<AffectationJournaliere> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetweenOrderByDateDesc(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeParPPHEtDate(String referencePPH, LocalDate date) {
        return jpaRepository.existsByReferencePPHAndDate(referencePPH, date);
    }

    private AffectationJournaliereJpaEntity toEntity(AffectationJournaliere a) {
        AffectationJournaliereJpaEntity e = new AffectationJournaliereJpaEntity();
        e.setId(a.getId());
        e.setReferencePPH(a.getReferencePPH());
        e.setDate(a.getDate());
        e.setMatriculeAgent(a.getMatriculeAgent());
        e.setStatut(a.getStatut());
        e.setDateCreation(a.getDateCreation());
        e.setDateModification(a.getDateModification());
        e.setVersion(a.getVersion());

        e.getLignes().clear();
        a.getLignes().forEach(l -> {
            LigneAffectationJpaEntity le = new LigneAffectationJpaEntity();
            le.setAffectation(e);
            le.setPoste(l.getPoste());
            le.setDuree(l.getDuree());
            le.setObjectifAjuste(l.getObjectifAjuste());
            le.setMatriculesEmployesJson(String.join(",", l.getMatriculesEmployes()));
            e.getLignes().add(le);
        });

        e.getSaisies().clear();
        a.getSaisies().forEach(s -> {
            SaisieRealisationJpaEntity se = new SaisieRealisationJpaEntity();
            se.setAffectation(e);
            se.setPoste(s.getPoste());
            se.setQuantiteRealisee(s.getQuantiteRealisee());
            se.setObjectifAjuste(s.getObjectifAjuste());
            se.setDateEnregistrement(s.getDateEnregistrement());
            DetailsRealisationPoste d = s.getDetails();
            se.setNbRegimesEpluches(d.getNbRegimesEpluches());
            se.setNbRegimesDecoupes(d.getNbRegimesDecoupes());
            se.setNbSceauxEtiquetes(d.getNbSceauxEtiquetes());
            se.setNbEtiquettesDatees(d.getNbEtiquettesDatees());
            se.setNbSachetsDates(d.getNbSachetsDates());
            se.setNbCartonsProduits(d.getNbCartonsProduits());
            se.setKgChipsProduits(d.getKgChipsProduits());
            se.setWarningsJson(toWarningsJson(s.getWarnings()));
            s.getProductionsIndividuelles().forEach(p -> {
                ProductionIndividuelleJpaEntity pe = new ProductionIndividuelleJpaEntity();
                pe.setSaisie(se);
                pe.setMatriculeEmploye(p.getMatriculeEmploye());
                pe.setNomEmploye(p.getNomEmploye());
                pe.setPoste(p.getPoste());
                pe.setQuantiteRealisee(p.getQuantiteRealisee());
                se.getProductionsIndividuelles().add(pe);
            });
            e.getSaisies().add(se);
        });
        return e;
    }

    private AffectationJournaliere toDomain(AffectationJournaliereJpaEntity e) {
        List<LigneAffectationPoste> lignes = e.getLignes().stream()
            .map(l -> new LigneAffectationPoste(l.getPoste(),
                Arrays.asList(l.getMatriculesEmployesJson().split(",")), l.getDuree(),
                l.getObjectifAjuste()))
            .toList();

        List<SaisieRealisationPoste> saisies = e.getSaisies().stream()
            .map(s -> {
                List<ProductionIndividuelle> prods = s.getProductionsIndividuelles().stream()
                    .map(p -> new ProductionIndividuelle(p.getMatriculeEmploye(), p.getNomEmploye(),
                        p.getPoste(), p.getQuantiteRealisee()))
                    .toList();
                DetailsRealisationPoste d = new DetailsRealisationPoste(
                    s.getNbRegimesEpluches(), s.getNbRegimesDecoupes(), s.getNbSceauxEtiquetes(),
                    s.getNbEtiquettesDatees(), s.getNbSachetsDates(), s.getNbCartonsProduits(),
                    s.getKgChipsProduits());
                return new SaisieRealisationPoste(s.getPoste(), s.getPoste(), "",
                    s.getQuantiteRealisee(), s.getObjectifAjuste(), prods, d,
                    fromWarningsJson(s.getWarningsJson()));
            })
            .toList();

        return AffectationJournaliere.reconstituer(e.getId(), e.getReferencePPH(), e.getDate(),
            e.getMatriculeAgent(), lignes, saisies, e.getStatut(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }

    private String toWarningsJson(List<String> warnings) {
        if (warnings == null || warnings.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(warnings);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Erreur de sérialisation des warnings", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> fromWarningsJson(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, List.class);
        } catch (JsonProcessingException ex) {
            return Collections.emptyList();
        }
    }
}
