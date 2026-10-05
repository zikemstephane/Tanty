package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PreCommandeRepository {
    PreCommande sauvegarder(PreCommande preCommande);
    Optional<PreCommande> trouverParNumero(String numeroPreCommande);
    List<PreCommande> listerParCommercial(String matriculeCommercial);
    List<PreCommande> listerParStatut(StatutPreCommande statut);
    List<PreCommande> listerParDateEffet(LocalDate dateEffet);
    List<PreCommande> listerTous();

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<PreCommande> listerParStatutPagine(StatutPreCommande statut, Pageable pageable);
    Page<PreCommande> listerParDateEffetPagine(LocalDate dateEffet, Pageable pageable);
}
