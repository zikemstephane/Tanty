package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa.BonCommandeJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.mapper.BonCommandePersistenceMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BonCommandeRepositoryImpl implements BonCommandeRepository {

    private final BonCommandeJpaRepository jpaRepository;
    private final BonCommandePersistenceMapper mapper;

    public BonCommandeRepositoryImpl(BonCommandeJpaRepository jpaRepository, BonCommandePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BonCommande sauvegarder(BonCommande bonCommande) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(bonCommande)));
    }

    @Override
    public Optional<BonCommande> trouverParNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::versDomaine);
    }

    @Override
    public List<BonCommande> trouverParStatut(StatutBonCommande statut) {
        return jpaRepository.findByStatut(statut.name()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<BonCommande> trouverParFournisseur(String codeFournisseur) {
        return jpaRepository.findByCodeFournisseur(codeFournisseur).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<BonCommande> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<BonCommande> trouverEnRetard() {
        return jpaRepository.findEnRetard(LocalDate.now()).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compterParAnnee(int annee) {
        return jpaRepository.compterParAnnee(annee);
    }

    @Override
    public void supprimer(BonCommande bonCommande) {
        jpaRepository.deleteById(bonCommande.getId());
    }
}
