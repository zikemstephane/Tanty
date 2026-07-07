package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VenteRepository {
    Vente sauvegarder(Vente vente);
    Optional<Vente> trouverParNumero(String numeroVente);
    List<Vente> listerParCommercial(String matriculeCommercial);
    List<Vente> listerParCommercialEtDate(String matriculeCommercial, LocalDate date);
    List<Vente> listerParStatut(StatutVente statut);
    List<Vente> listerParPeriode(LocalDate debut, LocalDate fin);
    double totalVentesParCommercialEtPeriode(String matriculeCommercial, LocalDate debut, LocalDate fin);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<Vente> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable);
    Page<Vente> listerParCommercialPagine(String matriculeCommercial, Pageable pageable);
}
