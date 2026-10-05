package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa.LotJpaRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa.LotProduitFiniJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class LotRepositoryImpl implements LotRepository {

    private final LotJpaRepository jpaRepository;

    public LotRepositoryImpl(LotJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LotProduitFini sauvegarder(LotProduitFini lot) {
        LotProduitFiniJpaEntity entity = versEntity(lot);
        LotProduitFiniJpaEntity saved = jpaRepository.save(entity);
        return versDomaine(saved);
    }

    @Override
    public Optional<LotProduitFini> trouverParNumero(String numeroLot) {
        return jpaRepository.findByNumeroLot(numeroLot).map(this::versDomaine);
    }

    @Override
    public List<LotProduitFini> listerParStatut(StatutLot statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::versDomaine).toList();
    }

    @Override
    public List<LotProduitFini> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::versDomaine).toList();
    }

    @Override
    public List<LotProduitFini> listerParCodeProduit(String codeProduit) {
        return jpaRepository.findByProductSku(codeProduit).stream().map(this::versDomaine).toList();
    }

    @Override
    public List<LotProduitFini> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateProductionBetween(debut, fin).stream().map(this::versDomaine).toList();
    }

    @Override
    public long compterParDate(LocalDate date) {
        return jpaRepository.countByDateProduction(date);
    }

    private LotProduitFiniJpaEntity versEntity(LotProduitFini lot) {
        LotProduitFiniJpaEntity e = new LotProduitFiniJpaEntity();
        e.setId(lot.getId());
        e.setNumeroLot(lot.getNumeroLot());
        e.setReferencePPH(lot.getReferencePPH());
        e.setProductSku(lot.getProductSku());
        e.setProductName(lot.getProductName());
        e.setProductUnit(lot.getProductUnit());
        e.setDeclaredQuantityKg(lot.getDeclaredQuantityKg());
        e.setEquivalentUnits(lot.getEquivalentUnits());
        e.setDateProduction(lot.getDateProduction());
        e.setDlc(lot.getDlc());
        e.setMatriculeChefProduction(lot.getMatriculeChefProduction());
        e.setDeclaredBy(lot.getDeclaredBy());
        e.setStatut(lot.getStatut());
        e.setMatriculeGestionnaireValidation(lot.getMatriculeGestionnaireValidation());
        e.setMotifRejet(lot.getMotifRejet());
        e.setConditioningType(lot.getConditioningType());
        e.setConditioningQty(lot.getConditioningQty());
        e.setNotes(lot.getNotes());
        e.setDateCreation(lot.getDateCreation());
        e.setDateModification(lot.getDateModification());
        e.setValidatedAt(lot.getValidatedAt());
        e.setVersion(lot.getVersion());
        return e;
    }

    private LotProduitFini versDomaine(LotProduitFiniJpaEntity e) {
        return LotProduitFini.reconstituer(
            e.getId(), e.getNumeroLot(), e.getReferencePPH(),
            e.getProductSku(), e.getProductName(), e.getProductUnit(),
            e.getDeclaredQuantityKg(), e.getEquivalentUnits(),
            e.getDateProduction(), e.getDlc(),
            e.getMatriculeChefProduction(), e.getDeclaredBy(),
            e.getStatut(), e.getMatriculeGestionnaireValidation(),
            e.getMotifRejet(), e.getConditioningType(), e.getConditioningQty(),
            e.getNotes(), e.getDateCreation(), e.getDateModification(),
            e.getValidatedAt(), e.getVersion()
        );
    }
}
