package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate.CommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.entity.LigneCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.repository.CommandeDistanceRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.CommandeDistanceJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.CommandeDistanceJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa.LigneCommandeDistanceJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CommandeDistanceRepositoryImpl implements CommandeDistanceRepository {

    private final CommandeDistanceJpaRepository jpaRepository;

    public CommandeDistanceRepositoryImpl(CommandeDistanceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CommandeDistance sauvegarder(CommandeDistance commande) {
        return toDomain(jpaRepository.save(toEntity(commande)));
    }

    @Override
    public Optional<CommandeDistance> trouverParNumero(String numero) {
        return jpaRepository.findByNumeroCommande(numero).map(this::toDomain);
    }

    @Override
    public List<CommandeDistance> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<CommandeDistance> listerParStatut(StatutCommandeDistance statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<CommandeDistance> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateCommandeBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    private CommandeDistanceJpaEntity toEntity(CommandeDistance cd) {
        List<LigneCommandeDistanceJpaEntity> lignes = cd.getLignes().stream()
            .map(l -> LigneCommandeDistanceJpaEntity.builder()
                .codeProduit(l.getCodeProduit()).designation(l.getDesignation())
                .conditionnement(l.getConditionnement()).quantite(l.getQuantite())
                .prixUnitaire(l.getPrixUnitaire()).build())
            .toList();
        return CommandeDistanceJpaEntity.builder()
            .id(cd.getId()).numeroCommande(cd.getNumeroCommande()).codeClient(cd.getCodeClient())
            .matriculeCommercial(cd.getMatriculeCommercial()).canal(cd.getCanal())
            .dateCommande(cd.getDateCommande()).lignes(lignes).statut(cd.getStatut())
            .matriculeValidateurSecretaire(cd.getMatriculeValidateurSecretaire())
            .matriculeValidateurComptable(cd.getMatriculeValidateurComptable())
            .motifAnnulation(cd.getMotifAnnulation()).observations(cd.getObservations())
            .dateCreation(cd.getDateCreation()).dateModification(cd.getDateModification())
            .version(cd.getVersion()).build();
    }

    private CommandeDistance toDomain(CommandeDistanceJpaEntity e) {
        List<LigneCommandeDistance> lignes = e.getLignes().stream()
            .map(l -> new LigneCommandeDistance(l.getCodeProduit(), l.getDesignation(),
                l.getConditionnement(), l.getQuantite(), l.getPrixUnitaire()))
            .toList();
        return CommandeDistance.reconstituer(e.getId(), e.getNumeroCommande(), e.getCodeClient(),
            e.getMatriculeCommercial(), e.getCanal(), e.getDateCommande(), lignes, e.getStatut(),
            e.getMatriculeValidateurSecretaire(), e.getMatriculeValidateurComptable(),
            e.getMotifAnnulation(), e.getObservations(),
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
