package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.SyncStatus;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.LigneVenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class VenteRepositoryImpl implements VenteRepository {

    private final VenteJpaRepository jpaRepository;

    public VenteRepositoryImpl(VenteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vente sauvegarder(Vente vente) {
        return toDomain(jpaRepository.save(toEntity(vente)));
    }

    @Override
    public Optional<Vente> trouverParNumero(String numeroVente) {
        return jpaRepository.findByNumeroVente(numeroVente).map(this::toDomain);
    }

    @Override
    public List<Vente> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vente> listerParCommercialEtDate(String matriculeCommercial, LocalDate date) {
        return jpaRepository.findByMatriculeCommercialAndDate(matriculeCommercial, date)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vente> listerParStatut(StatutVente statut) {
        return jpaRepository.findByStatut(statut).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vente> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public double totalVentesParCommercialEtPeriode(String matriculeCommercial, LocalDate debut, LocalDate fin) {
        return jpaRepository.findByMatriculeCommercialAndDateBetween(matriculeCommercial, debut, fin)
            .stream().mapToDouble(e -> toDomain(e).montantTotal()).sum();
    }

    @Override
    public Page<Vente> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable) {
        return jpaRepository.findByDateBetweenOrderByDateDesc(debut, fin, pageable).map(this::toDomain);
    }

    @Override
    public Page<Vente> listerParCommercialPagine(String matriculeCommercial, Pageable pageable) {
        return jpaRepository.findByMatriculeCommercialOrderByDateDesc(matriculeCommercial, pageable).map(this::toDomain);
    }

    public String genererNumero(LocalDate date) {
        long count = jpaRepository.count() + 1;
        return "VTE-" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%03d", count);
    }

    private VenteJpaEntity toEntity(Vente v) {
        List<LigneVenteJpaEntity> lignes = v.getLignes().stream()
            .map(l -> LigneVenteJpaEntity.builder()
                .codeProduit(l.getCodeProduit()).designationProduit(l.getDesignationProduit())
                .conditionnement(l.getConditionnement()).quantite(l.getQuantite())
                .prixUnitaire(l.getPrixUnitaire()).remise(l.getRemise()).build())
            .toList();
        return VenteJpaEntity.builder()
            .id(v.getId()).numeroVente(v.getNumeroVente()).uuidVente(v.getUuidVente())
            .matriculeCommercial(v.getMatriculeCommercial())
            .codeClient(v.getCodeClient()).date(v.getDate()).lignes(lignes)
            .modeReglement(v.getModeReglement()).typeVente(v.getTypeVente()).statut(v.getStatut())
            .syncStatus(v.getSyncStatus()).coordonneesGPS(v.getCoordonneesGPS()).idFeuillRoute(v.getIdFeuillRoute())
            .observations(v.getObservations()).signatureClient(v.isSignatureClient())
            .dateCreation(v.getDateCreation()).dateModification(v.getDateModification())
            .version(v.getVersion()).build();
    }

    private Vente toDomain(VenteJpaEntity e) {
        List<LigneVente> lignes = e.getLignes().stream()
            .map(l -> new LigneVente(l.getCodeProduit(), l.getDesignationProduit(),
                l.getConditionnement(), l.getQuantite(), l.getPrixUnitaire(), l.getRemise()))
            .toList();
        return Vente.reconstituer(e.getId(), e.getNumeroVente(), e.getUuidVente(), e.getMatriculeCommercial(),
            e.getCodeClient(), e.getDate(), lignes, e.getModeReglement(), e.getTypeVente(), e.getStatut(),
            e.getSyncStatus(), e.getCoordonneesGPS(), e.getIdFeuillRoute(),
            e.getObservations(), e.isSignatureClient(), e.getDateCreation(),
            e.getDateModification(), e.getVersion());
    }
}
