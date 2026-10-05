package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.RevisionObjectif;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaEntity;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.ObjectifCommercialJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa.RevisionObjectifJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ObjectifCommercialRepositoryImpl implements ObjectifCommercialRepository {

    private final ObjectifCommercialJpaRepository jpaRepository;

    public ObjectifCommercialRepositoryImpl(ObjectifCommercialJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public ObjectifCommercial sauvegarder(ObjectifCommercial objectif) {
        return toDomain(jpaRepository.save(toEntity(objectif)));
    }

    @Override
    public Optional<ObjectifCommercial> trouver(String matriculeCommercial, LocalDate semaineDebut) {
        return jpaRepository.findByMatriculeCommercialAndSemaineDebut(matriculeCommercial, semaineDebut).map(this::toDomain);
    }

    @Override
    public List<ObjectifCommercial> listerParCommercial(String matriculeCommercial) {
        return jpaRepository.findByMatriculeCommercial(matriculeCommercial).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ObjectifCommercial> listerParSemaine(LocalDate semaineDebut) {
        return jpaRepository.findBySemaineDebut(semaineDebut).stream().map(this::toDomain).toList();
    }

    private ObjectifCommercialJpaEntity toEntity(ObjectifCommercial o) {
        ObjectifCommercialJpaEntity e = new ObjectifCommercialJpaEntity();
        e.setId(o.getId());
        e.setMatriculeCommercial(o.getMatriculeCommercial());
        e.setSemaineDebut(o.getSemaineDebut());
        e.setSemaineFin(o.getSemaineFin());
        e.setObjectifGlobalFCFA(o.getObjectifGlobalFCFA());
        e.setObjectifsParGammeFCFA(new HashMap<>(o.getObjectifsParGammeFCFA()));
        e.setNombreJoursOuvres(o.getNombreJoursOuvres());
        e.setMatriculeAuteur(o.getMatriculeAuteur());
        e.setDateCreation(o.getDateCreation());
        e.setDateModification(o.getDateModification());
        e.setVersion(o.getVersion());

        // Le journal ne se réécrit pas : seules les révisions nouvelles, qui n'ont
        // pas encore d'identifiant, sont ajoutées à celles déjà persistées.
        List<RevisionObjectifJpaEntity> journal = new ArrayList<>();
        for (RevisionObjectif r : o.getRevisions()) {
            RevisionObjectifJpaEntity re = new RevisionObjectifJpaEntity();
            re.setId(r.getId());
            re.setObjectif(e);
            re.setObjectifGlobalAvantFCFA(r.getObjectifGlobalAvantFCFA());
            re.setObjectifsParGammeAvantFCFA(new HashMap<>(r.getObjectifsParGammeAvantFCFA()));
            re.setObjectifGlobalApresFCFA(r.getObjectifGlobalApresFCFA());
            re.setMatriculeAuteur(r.getMatriculeAuteur());
            re.setMotif(r.getMotif());
            re.setDateRevision(r.getDateRevision());
            journal.add(re);
        }
        e.getRevisions().addAll(journal);
        return e;
    }

    private ObjectifCommercial toDomain(ObjectifCommercialJpaEntity e) {
        // Fallback à 6 jours ouvrés pour les lignes créées avant l'introduction du seuil journalier.
        int joursOuvres = e.getNombreJoursOuvres() != null ? e.getNombreJoursOuvres() : 6;
        List<RevisionObjectif> revisions = e.getRevisions().stream()
            .map(r -> RevisionObjectif.reconstituer(r.getId(), r.getObjectifGlobalAvantFCFA(),
                new HashMap<>(r.getObjectifsParGammeAvantFCFA()), r.getObjectifGlobalApresFCFA(),
                r.getMatriculeAuteur(), r.getMotif(), r.getDateRevision()))
            .toList();
        return ObjectifCommercial.reconstituer(e.getId(), e.getMatriculeCommercial(), e.getSemaineDebut(),
            e.getSemaineFin(), e.getObjectifGlobalFCFA(), new HashMap<>(e.getObjectifsParGammeFCFA()), joursOuvres,
            e.getMatriculeAuteur(), e.getDateCreation(), e.getDateModification(), revisions, e.getVersion());
    }
}
