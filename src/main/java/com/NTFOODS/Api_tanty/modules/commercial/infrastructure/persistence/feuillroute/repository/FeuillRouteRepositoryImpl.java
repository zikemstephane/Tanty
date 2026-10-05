package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.ArretFeuillRouteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.FeuillRouteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.FeuillRouteJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class FeuillRouteRepositoryImpl implements FeuillRouteRepository {

    private final FeuillRouteJpaRepository jpaRepository;

    public FeuillRouteRepositoryImpl(FeuillRouteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FeuillRoute sauvegarder(FeuillRoute feuillRoute) {
        return toDomain(jpaRepository.save(toEntity(feuillRoute)));
    }

    @Override
    public Optional<FeuillRoute> trouverParNumero(String numeroFeuillRoute) {
        return jpaRepository.findByNumeroFeuillRoute(numeroFeuillRoute).map(this::toDomain);
    }

    @Override
    public List<FeuillRoute> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<FeuillRoute> listerParCommercialEtDate(String matriculeCommercial, LocalDate date) {
        return jpaRepository.findByMatriculeCommercialAndDate(matriculeCommercial, date).stream().map(this::toDomain).toList();
    }

    @Override
    public List<FeuillRoute> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetween(debut, fin).stream().map(this::toDomain).toList();
    }

    private FeuillRouteJpaEntity toEntity(FeuillRoute fr) {
        List<ArretFeuillRouteJpaEntity> arrets = fr.getArrets().stream()
            .map(a -> ArretFeuillRouteJpaEntity.builder()
                .id(a.getId()).codeClient(a.getCodeClient()).ordre(a.getOrdre())
                .statut(a.getStatut()).numeroVente(a.getNumeroVente())
                .codeInfoClient(a.getSaisie() != null ? a.getSaisie().getCodeInfoClient() : null)
                .quantiteInfo(a.getSaisie() != null ? a.getSaisie().getQuantiteInfo() : null)
                .observationsArret(a.getSaisie() != null ? a.getSaisie().getObservations() : null)
                .heurePlanifiee(a.getHeurePlanifiee()).heureEffective(a.getHeureEffective())
                .build())
            .toList();
        return FeuillRouteJpaEntity.builder()
            .id(fr.getId()).numeroFeuillRoute(fr.getNumeroFeuillRoute())
            .numeroSession(fr.getNumeroSession())
            .matriculeCommercial(fr.getMatriculeCommercial()).date(fr.getDate())
            .arrets(arrets).dateCreation(fr.getDateCreation())
            .dateModification(fr.getDateModification()).version(fr.getVersion()).build();
    }

    private FeuillRoute toDomain(FeuillRouteJpaEntity e) {
        List<ArretFeuillRoute> arrets = e.getArrets().stream()
            .map(a -> {
                SaisieArret saisie = null;
                if (a.getCodeInfoClient() != null) {
                    saisie = new SaisieArret(a.getCodeClient(), a.getCodeInfoClient(),
                        a.getQuantiteInfo(), a.getObservationsArret());
                }
                return ArretFeuillRoute.reconstituer(a.getId(), a.getCodeClient(), a.getOrdre(),
                    a.getStatut(), a.getNumeroVente(), saisie, a.getHeurePlanifiee(),
                    a.getHeureEffective());
            })
            .toList();
        return FeuillRoute.reconstituer(e.getId(), e.getNumeroFeuillRoute(), e.getNumeroSession(),
            e.getMatriculeCommercial(), e.getDate(), arrets,
            e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
