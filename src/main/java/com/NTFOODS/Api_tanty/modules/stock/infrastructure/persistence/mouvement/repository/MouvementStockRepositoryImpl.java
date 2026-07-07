package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.event.MouvementEnregistreEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.mapper.MouvementStockPersistenceMapper;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * MouvementStock est un agrégat purement append-only (aucun invariant complexe,
 * pas de cycle de vie à protéger) : chaque enregistrement représente un fait déjà
 * consommé physiquement. Contrairement aux autres agrégats du projet, il ne
 * porte pas son propre mécanisme de domain events. Publier
 * {@link MouvementEnregistreEvent} directement ici, au point d'appel UNIQUE de
 * tous les mouvements de stock (réception, transfert, dotation, retour,
 * inventaire), honore le contrat "stock.mouvement.enregistre → Comptabilité"
 * sans avoir à retoucher chacun de ces use cases individuellement.
 */
@Component
public class MouvementStockRepositoryImpl implements MouvementStockRepository {

    private final MouvementStockJpaRepository jpaRepository;
    private final MouvementStockPersistenceMapper mapper;
    private final EventPublisher eventPublisher;

    public MouvementStockRepositoryImpl(MouvementStockJpaRepository jpaRepository,
                                        MouvementStockPersistenceMapper mapper,
                                        EventPublisher eventPublisher) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public MouvementStock enregistrer(MouvementStock mouvement) {
        MouvementStock sauvegarde = mapper.versDomaine(jpaRepository.save(mapper.versEntite(mouvement)));
        eventPublisher.publish(new MouvementEnregistreEvent(sauvegarde.getReference(), sauvegarde.getType(),
            sauvegarde.getCodeProduit(), sauvegarde.getSku(), sauvegarde.getQuantiteEnBase(),
            sauvegarde.getCodeEntrepotSource(), sauvegarde.getCodeEntrepotCible(), sauvegarde.getMatriculeActeur()));
        return sauvegarde;
    }

    @Override
    public List<MouvementStock> trouverParProduit(String codeProduit) {
        return jpaRepository.findByCodeProduitOrderByHorodatageDesc(codeProduit)
            .stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<MouvementStock> trouverParEntrepot(String codeEntrepot) {
        return jpaRepository.findByCodeEntrepotSourceOrCodeEntrepotCibleOrderByHorodatageDesc(codeEntrepot, codeEntrepot)
            .stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<MouvementStock> trouverParType(TypeMouvement type) {
        return jpaRepository.findByTypeOrderByHorodatageDesc(type.name())
            .stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<MouvementStock> trouverParPeriode(LocalDate debut, LocalDate fin) {
        return jpaRepository.findByHorodatageBetweenOrderByHorodatageDesc(
                debut.atStartOfDay(), fin.atTime(LocalTime.MAX))
            .stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<MouvementStock> trouverParDocument(String referenceDocument) {
        return jpaRepository.findByReferenceDocument(referenceDocument)
            .stream().map(mapper::versDomaine).toList();
    }

    @Override
    public long compter() {
        return jpaRepository.count();
    }

    @Override
    public Page<MouvementStock> trouverParProduitPagine(String codeProduit, Pageable pageable) {
        return jpaRepository.findByCodeProduitOrderByHorodatageDesc(codeProduit, pageable).map(mapper::versDomaine);
    }

    @Override
    public Page<MouvementStock> trouverParEntrepotPagine(String codeEntrepot, Pageable pageable) {
        return jpaRepository.findByCodeEntrepotSourceOrCodeEntrepotCibleOrderByHorodatageDesc(codeEntrepot, codeEntrepot, pageable).map(mapper::versDomaine);
    }

    @Override
    public Page<MouvementStock> trouverParTypePagine(TypeMouvement type, Pageable pageable) {
        return jpaRepository.findByTypeOrderByHorodatageDesc(type.name(), pageable).map(mapper::versDomaine);
    }

    @Override
    public Page<MouvementStock> trouverParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable) {
        return jpaRepository.findByHorodatageBetweenOrderByHorodatageDesc(
                debut.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable).map(mapper::versDomaine);
    }

    @Override
    public Page<MouvementStock> trouverTousPagine(Pageable pageable) {
        return jpaRepository.findAllByOrderByHorodatageDesc(pageable).map(mapper::versDomaine);
    }
}
