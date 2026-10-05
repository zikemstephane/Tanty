package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.repository.LotStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.jpa.LotStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.mapper.LotStockPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class LotStockRepositoryImpl implements LotStockRepository {

    private final LotStockJpaRepository jpa;
    private final LotStockPersistenceMapper mapper;

    public LotStockRepositoryImpl(LotStockJpaRepository jpa, LotStockPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public LotStock sauvegarder(LotStock lot) {
        return mapper.versDomaine(jpa.save(mapper.versEntite(lot)));
    }

    @Override
    public Optional<LotStock> trouverParId(Long id) {
        return jpa.findById(id).map(mapper::versDomaine);
    }

    @Override
    public Optional<LotStock> trouverParNumero(String numeroLot) {
        return jpa.findByNumeroLot(numeroLot).map(mapper::versDomaine);
    }

    @Override
    public boolean existeParNumero(String numeroLot) {
        return jpa.existsByNumeroLot(numeroLot);
    }

    @Override
    public List<LotStock> trouverTous() {
        return jpa.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverParProduit(String codeProduit) {
        return jpa.findByCodeProduitOrderByDatePeremptionAsc(codeProduit).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverParSku(String sku) {
        return jpa.findBySkuOrderByDatePeremptionAsc(sku).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverParEntrepot(String codeEntrepot) {
        return jpa.findByCodeEntrepotOrderByDatePeremptionAsc(codeEntrepot).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverConsommables(String sku, String codeEntrepot) {
        return jpa.findConsommables(sku, codeEntrepot).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverPerimantAvant(LocalDate limite) {
        return jpa.findPerimantAvant(limite, LocalDate.now()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<LotStock> trouverPerimes() {
        return jpa.findPerimes(LocalDate.now()).stream().map(mapper::versDomaine).toList();
    }
}
