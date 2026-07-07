package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.ObjectifMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.ObjectifMachinisteJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.RealisationMachinisteJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.SessionBroyageJpaEntity;
import com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa.SessionBroyageJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionBroyageRepositoryImpl implements SessionBroyageRepository {

    private final SessionBroyageJpaRepository jpaRepository;

    public SessionBroyageRepositoryImpl(SessionBroyageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SessionBroyage sauvegarder(SessionBroyage session) {
        return toDomain(jpaRepository.save(toEntity(session)));
    }

    @Override
    public Optional<SessionBroyage> trouverParId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<SessionBroyage> trouverParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre) {
        return jpaRepository.findByReferencePPHAndDateAndTypePoudre(referencePPH, date, typePoudre).map(this::toDomain);
    }

    @Override
    public List<SessionBroyage> listerParPPH(String referencePPH) {
        return jpaRepository.findByReferencePPHOrderByDateDesc(referencePPH).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SessionBroyage> listerParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByDateBetweenOrderByDateDesc(debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SessionBroyage> listerParMachinisteEtPeriode(String machinisteMatricule, LocalDate debut, LocalDate fin) {
        return jpaRepository.findByMachinisteEtPeriode(machinisteMatricule, debut, fin).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existeParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre) {
        return jpaRepository.existsByReferencePPHAndDateAndTypePoudre(referencePPH, date, typePoudre);
    }

    private SessionBroyageJpaEntity toEntity(SessionBroyage s) {
        SessionBroyageJpaEntity e = new SessionBroyageJpaEntity();
        e.setId(s.getId());
        e.setReferencePPH(s.getReferencePPH());
        e.setDate(s.getDate());
        e.setTypePoudre(s.getTypePoudre());
        e.setObjectifJournalierKg(s.getObjectifJournalierKg());
        e.setMatriculeChefMachiniste(s.getMatriculeChefMachiniste());
        e.setQuantiteNetteBroyeeKg(s.getQuantiteNetteBroyeeKg());
        e.setPertesKg(s.getPertesKg());
        e.setDepassementAutoriseKg(s.getDepassementAutoriseKg());
        e.setJustificatifNonValidation(s.getJustificatifNonValidation());
        e.setStatut(s.getStatut());
        e.setDateCreation(s.getDateCreation());
        e.setDateModification(s.getDateModification());
        e.setVersion(s.getVersion());

        e.getObjectifsMachinistes().clear();
        s.getObjectifsMachinistes().forEach(o -> {
            ObjectifMachinisteJpaEntity oe = new ObjectifMachinisteJpaEntity();
            oe.setSession(e);
            oe.setMachinisteMatricule(o.getMachinisteMatricule());
            oe.setNomMachiniste(o.getNomMachiniste());
            oe.setOperation(o.getOperation());
            oe.setTypePoudre(o.getTypePoudre());
            oe.setObjectifKg(o.getObjectifKg());
            e.getObjectifsMachinistes().add(oe);
        });

        e.getRealisations().clear();
        s.getRealisations().forEach(r -> {
            RealisationMachinisteJpaEntity re = new RealisationMachinisteJpaEntity();
            re.setSession(e);
            re.setMachinisteMatricule(r.getMachinisteMatricule());
            re.setNomMachiniste(r.getNomMachiniste());
            re.setOperation(r.getOperation());
            re.setTypePoudre(r.getTypePoudre());
            re.setObjectifKg(r.getObjectifKg());
            re.setRealiseKg(r.getRealiseKg());
            e.getRealisations().add(re);
        });
        return e;
    }

    private SessionBroyage toDomain(SessionBroyageJpaEntity e) {
        List<ObjectifMachiniste> objectifs = e.getObjectifsMachinistes().stream()
            .map(o -> new ObjectifMachiniste(o.getMachinisteMatricule(), o.getNomMachiniste(),
                o.getOperation(), o.getTypePoudre(), o.getObjectifKg()))
            .toList();

        List<RealisationMachiniste> realisations = e.getRealisations().stream()
            .map(r -> new RealisationMachiniste(r.getMachinisteMatricule(), r.getNomMachiniste(),
                r.getOperation(), r.getTypePoudre(), r.getObjectifKg(), r.getRealiseKg()))
            .toList();

        return SessionBroyage.reconstituer(e.getId(), e.getReferencePPH(), e.getDate(),
            e.getTypePoudre(), e.getObjectifJournalierKg(), e.getMatriculeChefMachiniste(),
            objectifs, realisations, e.getQuantiteNetteBroyeeKg(), e.getPertesKg(),
            e.getDepassementAutoriseKg(), e.getJustificatifNonValidation(),
            e.getStatut(), e.getDateCreation(), e.getDateModification(), e.getVersion());
    }
}
