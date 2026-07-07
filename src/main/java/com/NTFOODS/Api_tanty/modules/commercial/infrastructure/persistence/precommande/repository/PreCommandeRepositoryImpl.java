package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.LignePreCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.PreCommandeJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa.PreCommandeJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class PreCommandeRepositoryImpl implements PreCommandeRepository {

    private final PreCommandeJpaRepository jpaRepository;

    public PreCommandeRepositoryImpl(PreCommandeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PreCommande sauvegarder(PreCommande pc) {
        return toDomain(jpaRepository.save(toEntity(pc)));
    }

    @Override
    public Optional<PreCommande> trouverParNumero(String numero) {
        return jpaRepository.findByNumeroPreCommande(numero).map(this::toDomain);
    }

    @Override
    public List<PreCommande> listerParCommercial(String matricule) {
        return jpaRepository.findByMatriculeCommercial(matricule).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PreCommande> listerParDateEffet(LocalDate dateEffet) {
        return jpaRepository.findByDateEffet(dateEffet).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PreCommande> listerParStatut(StatutPreCommande statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PreCommande> listerTous() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Page<PreCommande> listerParStatutPagine(StatutPreCommande statut, Pageable pageable) {
        return jpaRepository.findByStatutOrderByDateSoumissionDesc(statut, pageable).map(this::toDomain);
    }

    @Override
    public Page<PreCommande> listerParDateEffetPagine(LocalDate dateEffet, Pageable pageable) {
        return jpaRepository.findByDateEffetOrderByDateSoumissionDesc(dateEffet, pageable).map(this::toDomain);
    }

    public String genererNumero(String matriculeCommercial, LocalDate date) {
        long count = jpaRepository.countByDateSoumissionAndMatriculeCommercial(date, matriculeCommercial) + 1;
        return "PC-" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"
            + matriculeCommercial.substring(0, Math.min(3, matriculeCommercial.length()))
            + "-" + String.format("%03d", count);
    }

    private PreCommandeJpaEntity toEntity(PreCommande pc) {
        List<LignePreCommandeJpaEntity> lignes = pc.getLignes().stream()
            .map(l -> LignePreCommandeJpaEntity.builder()
                .codeProduit(l.getCodeProduit()).designationProduit(l.getDesignationProduit())
                .conditionnement(l.getConditionnement()).quantiteDemandee(l.getQuantiteDemandee())
                .quantiteValidee(l.getQuantiteValidee()).build())
            .toList();
        return PreCommandeJpaEntity.builder()
            .id(pc.getId()).numeroPreCommande(pc.getNumeroPreCommande())
            .matriculeCommercial(pc.getMatriculeCommercial()).dateSoumission(pc.getDateSoumission())
            .dateEffet(pc.getDateEffet()).lignes(lignes).statut(pc.getStatut())
            .matriculeValidateurSecretaire(pc.getMatriculeValidateurSecretaire())
            .matriculeValidateurComptable(pc.getMatriculeValidateurComptable())
            .motifRefus(pc.getMotifRefus()).soumiseEnSoiree(pc.isSoumiseEnSoiree())
            .dateCreation(pc.getDateCreation()).dateModification(pc.getDateModification())
            .version(pc.getVersion()).build();
    }

    private PreCommande toDomain(PreCommandeJpaEntity e) {
        List<LignePreCommande> lignes = e.getLignes().stream()
            .map(l -> {
                LignePreCommande ligne = new LignePreCommande(l.getCodeProduit(), l.getDesignationProduit(),
                    l.getConditionnement(), l.getQuantiteDemandee());
                if (l.getQuantiteValidee() != l.getQuantiteDemandee()) {
                    ligne.ajusterQuantiteValidee(l.getQuantiteValidee());
                }
                return ligne;
            }).toList();
        return PreCommande.reconstituer(e.getId(), e.getNumeroPreCommande(), e.getMatriculeCommercial(),
            e.getDateSoumission(), e.getDateEffet(), lignes, e.getStatut(),
            e.getMatriculeValidateurSecretaire(), e.getMatriculeValidateurComptable(),
            e.getMotifRefus(), e.isSoumiseEnSoiree(), e.getDateCreation(), e.getDateModification(),
            e.getVersion());
    }
}
