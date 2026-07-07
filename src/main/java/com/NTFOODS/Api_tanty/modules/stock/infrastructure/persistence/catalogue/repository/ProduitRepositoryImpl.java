package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.mapper.ProduitPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptateur — Implémentation JPA du port ProduitRepository. */
@Component
public class ProduitRepositoryImpl implements ProduitRepository {

    private final ProduitJpaRepository jpaRepository;
    private final ProduitPersistenceMapper mapper;

    public ProduitRepositoryImpl(ProduitJpaRepository jpaRepository, ProduitPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Produit sauvegarder(Produit produit) {
        ProduitJpaEntity entite = mapper.versEntite(produit);
        return mapper.versDomaine(jpaRepository.save(entite));
    }

    @Override
    public Optional<Produit> trouverParCode(CodeProduit code) {
        return jpaRepository.findByCode(code.getValeur()).map(mapper::versDomaine);
    }

    @Override
    public Optional<Produit> trouverParId(Long id) {
        return jpaRepository.findById(id).map(mapper::versDomaine);
    }

    @Override
    public Optional<Produit> trouverParSku(String sku) {
        return jpaRepository.findByConditionnementSku(sku).map(mapper::versDomaine);
    }

    @Override
    public boolean existeParCode(CodeProduit code) {
        return jpaRepository.existsByCode(code.getValeur());
    }

    @Override
    public List<Produit> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Produit> trouverParCategorie(CategorieProduit categorie) {
        return jpaRepository.findByCategorie(categorie.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Produit> trouverParCategorieMetier(CategorieMetier categorieMetier) {
        return jpaRepository.findByCategorieMetier(categorieMetier.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Produit> rechercher(String marque, String gamme, String variete, Boolean actif) {
        return jpaRepository.rechercher(marque, gamme, variete, actif).stream()
            .map(mapper::versDomaine).toList();
    }

    @Override
    public void supprimer(CodeProduit code) {
        jpaRepository.deleteByCode(code.getValeur());
    }
}
