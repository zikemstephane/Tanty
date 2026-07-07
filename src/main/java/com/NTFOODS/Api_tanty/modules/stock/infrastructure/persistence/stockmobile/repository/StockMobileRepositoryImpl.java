package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.LigneStockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository.StockMobileRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa.LigneStockMobileJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa.StockMobileJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa.StockMobileJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class StockMobileRepositoryImpl implements StockMobileRepository {

    private final StockMobileJpaRepository jpaRepository;

    public StockMobileRepositoryImpl(StockMobileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public StockMobile sauvegarder(StockMobile stockMobile) {
        return toDomain(jpaRepository.save(toEntity(stockMobile)));
    }

    @Override
    public Optional<StockMobile> trouverParCommercialEtDate(String matriculeCommercial, LocalDate date) {
        return jpaRepository.findByMatriculeCommercialAndDateSession(matriculeCommercial, date).map(this::toDomain);
    }

    @Override
    public Optional<StockMobile> trouverDernierParCommercial(String matriculeCommercial) {
        return jpaRepository.findFirstByMatriculeCommercialOrderByDateSessionDesc(matriculeCommercial).map(this::toDomain);
    }

    @Override
    public List<StockMobile> listerParDate(LocalDate date) {
        return jpaRepository.findByDateSession(date).stream().map(this::toDomain).toList();
    }

    @Override
    public List<StockMobile> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<StockMobile> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private StockMobileJpaEntity toEntity(StockMobile sm) {
        List<LigneStockMobileJpaEntity> lignes = sm.getLignes().stream()
            .map(l -> LigneStockMobileJpaEntity.builder()
                .id(l.getId()).codeProduit(l.getCodeProduit()).designation(l.getDesignation())
                .conditionnement(l.getConditionnement()).quantiteDisponible(l.getQuantiteDisponible())
                .quantiteInitiale(l.getQuantiteInitiale()).quantiteVendue(l.getQuantiteVendue())
                .quantiteAvariee(l.getQuantiteAvariee()).quantiteDonnee(l.getQuantiteDonnee())
                .dateDernierMouvement(l.getDateDernierMouvement()).build())
            .toList();
        return StockMobileJpaEntity.builder()
            .id(sm.getId()).matriculeCommercial(sm.getMatriculeCommercial()).dateSession(sm.getDateSession())
            .lignes(lignes).dateCreation(sm.getDateCreation()).dateModification(sm.getDateModification())
            .version(sm.getVersion()).build();
    }

    private StockMobile toDomain(StockMobileJpaEntity e) {
        List<LigneStockMobile> lignes = e.getLignes().stream()
            .map(l -> LigneStockMobile.reconstituer(l.getId(), l.getCodeProduit(), l.getDesignation(),
                l.getConditionnement(), l.getQuantiteDisponible(), l.getQuantiteInitiale(),
                l.getQuantiteVendue(), l.getQuantiteAvariee(), l.getQuantiteDonnee(),
                l.getDateDernierMouvement()))
            .toList();
        return StockMobile.reconstituer(e.getId(), e.getMatriculeCommercial(), e.getDateSession(),
            lignes, e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
