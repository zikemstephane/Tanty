package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.LignePlanDetailJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.LignePPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.RepartitionJourJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PPHRepositoryImpl implements PPHRepository {

    private final PPHJpaRepository jpaRepository;

    public PPHRepositoryImpl(PPHJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PlanProductionHebdomadaire sauvegarder(PlanProductionHebdomadaire pph) {
        return toDomain(jpaRepository.save(toEntity(pph)));
    }

    @Override
    public Optional<PlanProductionHebdomadaire> trouverParReference(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).map(this::toDomain);
    }

    @Override
    public List<PlanProductionHebdomadaire> listerParStatut(StatutPPH statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<PlanProductionHebdomadaire> trouverEnCours() {
        return jpaRepository.findFirstByStatut(StatutPPH.EN_COURS).map(this::toDomain);
    }

    @Override
    public List<PlanProductionHebdomadaire> listerParBC(String referenceBC) {
        return jpaRepository.findByReferenceBC(referenceBC).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PlanProductionHebdomadaire> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private PPHJpaEntity toEntity(PlanProductionHebdomadaire pph) {
        Long pphId = pph.getId();
        List<LignePPHJpaEntity> lignes = pph.getLignes().stream()
            .map(l -> LignePPHJpaEntity.builder()
                .codeProduit(l.getCodeProduit()).nomProduit(l.getNomProduit())
                .objectifSemaine(l.getObjectifSemaine()).productionRealisee(l.getProductionRealisee())
                .build())
            .toList();
        List<LignePlanDetailJpaEntity> details = pph.getLignesDetaillees().stream()
            .map(l -> LignePlanDetailJpaEntity.builder()
                .pphId(pphId)
                .codeProduit(l.getCodeProduit()).nomProduit(l.getNomProduit())
                .categorieMetier(l.getCategorieMetier())
                .quantiteCartons(l.getQuantiteCartons()).nombreUnites(l.getNombreUnitesParConditionnement())
                .poidsUnitaireGrammes(l.getPoidsUnitaireGrammes())
                .recette(l.getRecette() != null ? l.getRecette().name() : null)
                .dateJour(l.getDateJour()).quantiteJour(l.getQuantiteJour())
                .poudreMaisKg(l.getPoudreMaisKg()).poudreSojaKg(l.getPoudreSojaKg())
                .poudreArachideKg(l.getPoudreArachideKg()).futsPrevus(l.getNombreFuts50kg())
                .sachetsPrevus(l.getTotalSachets()).couverclesPrevus(l.getTotalCouvercles())
                .scellesPrevus(l.getTotalScelles()).poidsTotalGrammes(l.getTotalPoidsGrammes())
                .build())
            .toList();
        List<RepartitionJourJpaEntity> repartition = pph.getRepartitionJours().stream()
            .map(r -> RepartitionJourJpaEntity.builder()
                .pphId(pphId)
                .dateJour(r.getDate())
                .poudreMaisKg(r.getPoudreMaisKg()).poudreSojaKg(r.getPoudreSojaKg())
                .poudreArachideKg(r.getPoudreArachideKg()).futsPrevus(r.getTotalFuts())
                .sachetsPrevus(r.getTotalSachets()).couverclesPrevus(r.getTotalCouvercles())
                .scellesPrevus(r.getTotalScelles())
                .build())
            .toList();
        return PPHJpaEntity.builder()
            .id(pphId).referencePPH(pph.getReferencePPH()).referenceBC(pph.getReferenceBC())
            .semaine(pph.getSemaine())
            .dateDebut(pph.getDateDebut()).dateFin(pph.getDateFin())
            .matriculeChefProduction(pph.getMatriculeChefProduction())
            .lignes(lignes).lignesDetaillees(details).repartitionJours(repartition)
            .dateSoumission(pph.getDateSoumission())
            .statut(pph.getStatut()).dateCreation(pph.getDateCreation())
            .dateModification(pph.getDateModification()).version(pph.getVersion()).build();
    }

    private PlanProductionHebdomadaire toDomain(PPHJpaEntity e) {
        List<LignePPHJpaEntity> lignesEntities = e.getLignes();
        if (lignesEntities == null) lignesEntities = List.of();
        List<LignePPH> lignes = lignesEntities.stream()
            .map(l -> {
                LignePPH ligne = new LignePPH(l.getCodeProduit(), l.getNomProduit(), l.getObjectifSemaine());
                if (l.getProductionRealisee() > 0) ligne.cumulerProduction(l.getProductionRealisee());
                return ligne;
            }).toList();
        List<LignePlanDetailJpaEntity> detailsEntities = e.getLignesDetaillees();
        if (detailsEntities == null) detailsEntities = List.of();
        List<LignePlanDetail> details = detailsEntities.stream()
            .map(l -> new LignePlanDetail(
                l.getCodeProduit(), l.getNomProduit(), l.getCategorieMetier(),
                l.getQuantiteCartons(), l.getNombreUnites(), l.getPoidsUnitaireGrammes(),
                parseRecette(l.getRecette()), l.getDateJour(), l.getQuantiteJour()
            )).toList();
        List<RepartitionJourJpaEntity> repartitionEntities = e.getRepartitionJours();
        if (repartitionEntities == null) repartitionEntities = List.of();
        List<RepartitionJournaliere> repartition = repartitionEntities.stream()
            .map(r -> new RepartitionJournaliere(
                r.getDateJour(), List.of(),
                r.getPoudreMaisKg(), r.getPoudreSojaKg(), r.getPoudreArachideKg(),
                r.getFutsPrevus(), r.getSachetsPrevus(), r.getCouverclesPrevus(), r.getScellesPrevus()
            )).toList();
        return PlanProductionHebdomadaire.reconstituerComplet(e.getId(), e.getReferencePPH(), e.getReferenceBC(),
            e.getSemaine(), e.getDateDebut(), e.getDateFin(), e.getMatriculeChefProduction(),
            lignes, details, repartition, e.getDateSoumission(), e.getStatut(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }

    private RecetteBouillie parseRecette(String valeur) {
        if (valeur == null || valeur.isBlank()) return null;
        try {
            return RecetteBouillie.valueOf(valeur);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
