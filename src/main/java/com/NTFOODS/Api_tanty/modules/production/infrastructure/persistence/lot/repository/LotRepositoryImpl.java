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
        return toDomain(jpaRepository.save(toEntity(lot)));
    }

    @Override
    public Optional<LotProduitFini> trouverParNumero(String numeroLot) {
        return jpaRepository.findByNumeroLot(numeroLot).map(this::toDomain);
    }

    @Override
    public List<LotProduitFini> listerParStatut(StatutLot statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<LotProduitFini> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPH(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    public List<LotProduitFini> listerParCodeProduit(String codeProduit) {
        return jpaRepository.findByCodeProduit(codeProduit).stream().map(this::toDomain).toList();
    }

    @Override
    public List<LotProduitFini> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateProductionBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public long compterParStatutEtDate(StatutLot statut, LocalDate date) {
        return jpaRepository.countByStatutAndDateProduction(statut, date);
    }

    private LotProduitFiniJpaEntity toEntity(LotProduitFini lot) {
        LotProduitFiniJpaEntity e = new LotProduitFiniJpaEntity();
        e.setId(lot.getId());
        e.setNumeroLot(lot.getNumeroLot());
        e.setReferencePPH(lot.getReferencePPH());
        e.setCodeProduit(lot.getCodeProduit());
        e.setDesignationProduit(lot.getDesignationProduit());
        e.setQuantiteKg(lot.getQuantiteKg());
        e.setNbCartons(lot.getNbCartons());
        e.setDateProduction(lot.getDateProduction());
        e.setDlc(lot.getDlc());
        e.setMatriculeChefProduction(lot.getMatriculeChefProduction());
        e.setStatut(lot.getStatut());
        e.setMatriculeGestionnaireValidation(lot.getMatriculeGestionnaireValidation());
        e.setMotifRejet(lot.getMotifRejet());
        e.setDateCreation(lot.getDateCreation());
        e.setDateModification(lot.getDateModification());
        e.setVersion(lot.getVersion());
        return e;
    }

    private LotProduitFini toDomain(LotProduitFiniJpaEntity e) {
        return LotProduitFini.reconstituer(e.getId(), e.getNumeroLot(), e.getReferencePPH(),
            e.getCodeProduit(), e.getDesignationProduit(), e.getQuantiteKg(), e.getNbCartons(),
            e.getDateProduction(), e.getDlc(), e.getMatriculeChefProduction(), e.getStatut(),
            e.getMatriculeGestionnaireValidation(), e.getMotifRejet(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
