package com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate.Facture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FactureRepository {
    Facture sauvegarder(Facture facture);
    Optional<Facture> trouverParNumero(String numeroFacture);
    List<Facture> listerParClient(String codeClient);
    List<Facture> listerParPeriode(LocalDate debut, LocalDate fin);
    List<Facture> listerParStatut(StatutFacture statut);
    List<Facture> listerParType(TypeFacture typeFacture);
    String genererNumero(TypeFacture typeFacture, LocalDate date);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<Facture> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable);
    Page<Facture> listerParStatutPagine(StatutFacture statut, Pageable pageable);
    Page<Facture> listerParClientPagine(String codeClient, Pageable pageable);
}
