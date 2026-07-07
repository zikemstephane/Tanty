package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommandeDistanceJpaRepository extends JpaRepository<CommandeDistanceJpaEntity, Long> {
    Optional<CommandeDistanceJpaEntity> findByNumeroCommande(String numero);
    List<CommandeDistanceJpaEntity> findByMatriculeCommercial(String matricule);
    List<CommandeDistanceJpaEntity> findByStatut(StatutCommandeDistance statut);
    List<CommandeDistanceJpaEntity> findByDateCommandeBetween(LocalDate debut, LocalDate fin);
}
