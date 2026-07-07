package com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate.CommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommandeDistanceRepository {
    CommandeDistance sauvegarder(CommandeDistance commande);
    Optional<CommandeDistance> trouverParNumero(String numero);
    List<CommandeDistance> listerParCommercial(String matriculeCommercial);
    List<CommandeDistance> listerParStatut(StatutCommandeDistance statut);
    List<CommandeDistance> listerParPeriode(LocalDate debut, LocalDate fin);
}
