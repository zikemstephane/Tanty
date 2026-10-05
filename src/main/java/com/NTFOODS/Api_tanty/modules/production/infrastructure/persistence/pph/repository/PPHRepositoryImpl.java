package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.BesoinEmballageJourJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.BesoinEmballageLigneJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.BesoinMatierePremiereJourJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.BesoinMatierePremiereLigneJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.LignePlanDetailJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.LignePPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.PPHJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa.RepartitionJourJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.BesoinEmballage;
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
            .map(l -> {
                List<BesoinMatierePremiereLigneJpaEntity> besoinsMatieres = l.getBesoinsMatieresPremiere().stream()
                    .map(b -> BesoinMatierePremiereLigneJpaEntity.builder()
                        .matierePremiere(b.matierePremiere()).quantiteKg(b.quantiteKg()).build())
                    .toList();
                List<BesoinEmballageLigneJpaEntity> besoinsEmballage = l.getBesoinsEmballage().stream()
                    .map(b -> BesoinEmballageLigneJpaEntity.builder()
                        .typeComposant(b.type().name()).quantite(b.quantite()).build())
                    .toList();
                LignePlanDetailJpaEntity entite = LignePlanDetailJpaEntity.builder()
                    .pphId(pphId)
                    .codeProduit(l.getCodeProduit()).nomProduit(l.getNomProduit())
                    .categorieMetier(l.getCategorieMetier())
                    .quantiteCartons(l.getQuantiteCartons()).nombreUnites(l.getNombreUnitesParConditionnement())
                    .poidsUnitaireGrammes(l.getPoidsUnitaireGrammes())
                    .dateJour(l.getDateJour()).quantiteJour(l.getQuantiteJour())
                    .besoinsMatieresPremiere(besoinsMatieres).formuleManquante(l.isFormuleManquante())
                    .besoinsEmballage(besoinsEmballage).compositionEmballageManquante(l.isCompositionEmballageManquante())
                    .futsPrevus(l.getNombreFuts50kg()).poidsTotalGrammes(l.getTotalPoidsGrammes())
                    .build();
                entite.getBesoinsMatieresPremiere().forEach(b -> b.setLigneDetail(entite));
                entite.getBesoinsEmballage().forEach(b -> b.setLigneDetail(entite));
                return entite;
            })
            .toList();
        List<RepartitionJourJpaEntity> repartition = pph.getRepartitionJours().stream()
            .map(r -> {
                List<BesoinMatierePremiereJourJpaEntity> besoinsMatieres = r.getBesoinsMatieresPremiere().stream()
                    .map(b -> BesoinMatierePremiereJourJpaEntity.builder()
                        .matierePremiere(b.matierePremiere()).quantiteKg(b.quantiteKg()).build())
                    .toList();
                List<BesoinEmballageJourJpaEntity> besoinsEmballage = r.getBesoinsEmballage().stream()
                    .map(b -> BesoinEmballageJourJpaEntity.builder()
                        .typeComposant(b.type().name()).quantite(b.quantite()).build())
                    .toList();
                RepartitionJourJpaEntity entite = RepartitionJourJpaEntity.builder()
                    .pphId(pphId)
                    .dateJour(r.getDate())
                    .besoinsMatieresPremiere(besoinsMatieres)
                    .besoinsEmballage(besoinsEmballage)
                    .futsPrevus(r.getTotalFuts())
                    .build();
                entite.getBesoinsMatieresPremiere().forEach(b -> b.setRepartitionJour(entite));
                entite.getBesoinsEmballage().forEach(b -> b.setRepartitionJour(entite));
                return entite;
            })
            .toList();
        return PPHJpaEntity.builder()
            .id(pphId).referencePPH(pph.getReferencePPH()).referenceBC(pph.getReferenceBC())
            .semaine(pph.getSemaine())
            .dateDebut(pph.getDateDebut()).dateFin(pph.getDateFin())
            .matriculeChefProduction(pph.getMatriculeChefProduction())
            .lignes(lignes).lignesDetaillees(details).repartitionJours(repartition)
            .dateSoumission(pph.getDateSoumission())
            .statut(pph.getStatut()).repartitionAjusteeManuellement(pph.isRepartitionAjusteeManuellement())
            .dateCreation(pph.getDateCreation())
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
            .map(l -> {
                List<BesoinMatierePremiere> besoinsMatieres = (l.getBesoinsMatieresPremiere() == null
                    ? List.<BesoinMatierePremiereLigneJpaEntity>of() : l.getBesoinsMatieresPremiere()).stream()
                    .map(b -> new BesoinMatierePremiere(b.getMatierePremiere(), b.getQuantiteKg()))
                    .toList();
                List<BesoinEmballage> besoinsEmballage = (l.getBesoinsEmballage() == null
                    ? List.<BesoinEmballageLigneJpaEntity>of() : l.getBesoinsEmballage()).stream()
                    .map(b -> new BesoinEmballage(TypeEmballage.valueOf(b.getTypeComposant()), b.getQuantite()))
                    .toList();
                return new LignePlanDetail(
                    l.getCodeProduit(), l.getNomProduit(), l.getCategorieMetier(),
                    l.getQuantiteCartons(), l.getNombreUnites(), l.getPoidsUnitaireGrammes(),
                    besoinsMatieres, l.isFormuleManquante(),
                    besoinsEmballage, l.isCompositionEmballageManquante(),
                    l.getDateJour(), l.getQuantiteJour()
                );
            }).toList();
        List<RepartitionJourJpaEntity> repartitionEntities = e.getRepartitionJours();
        if (repartitionEntities == null) repartitionEntities = List.of();
        List<RepartitionJournaliere> repartition = repartitionEntities.stream()
            .map(r -> {
                List<BesoinMatierePremiere> besoinsMatieres = (r.getBesoinsMatieresPremiere() == null
                    ? List.<BesoinMatierePremiereJourJpaEntity>of() : r.getBesoinsMatieresPremiere()).stream()
                    .map(b -> new BesoinMatierePremiere(b.getMatierePremiere(), b.getQuantiteKg()))
                    .toList();
                List<BesoinEmballage> besoinsEmballage = (r.getBesoinsEmballage() == null
                    ? List.<BesoinEmballageJourJpaEntity>of() : r.getBesoinsEmballage()).stream()
                    .map(b -> new BesoinEmballage(TypeEmballage.valueOf(b.getTypeComposant()), b.getQuantite()))
                    .toList();
                return new RepartitionJournaliere(
                    r.getDateJour(), List.of(), besoinsMatieres, besoinsEmballage, r.getFutsPrevus()
                );
            }).toList();
        return PlanProductionHebdomadaire.reconstituerComplet(e.getId(), e.getReferencePPH(), e.getReferenceBC(),
            e.getSemaine(), e.getDateDebut(), e.getDateFin(), e.getMatriculeChefProduction(),
            lignes, details, repartition, e.getDateSoumission(), e.getStatut(), e.isRepartitionAjusteeManuellement(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
