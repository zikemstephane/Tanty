package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa.EntrepotJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.mapper.EntrepotPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EntrepotRepositoryImpl implements EntrepotRepository {

    private final EntrepotJpaRepository jpaRepository;
    private final EntrepotPersistenceMapper mapper;

    public EntrepotRepositoryImpl(EntrepotJpaRepository jpaRepository, EntrepotPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Entrepot sauvegarder(Entrepot entrepot) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(entrepot)));
    }

    @Override
    public Optional<Entrepot> trouverParCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::versDomaine);
    }

    /**
     * Entrepôt mobile d'un commercial, résolu de façon déterministe.
     *
     * <p>Le {@code findFirst()} précédent rendait le résultat dépendant de l'ordre
     * de lecture. Ce n'était pas théorique : deux entrepôts mobiles coexistaient
     * pour un même commercial — {@code ENT-MOB-COM1} et {@code MOBILE-COM-001} —
     * et son stock s'est réparti sur les deux. Une vente pouvait décrémenter l'un
     * pendant qu'une dotation créditait l'autre.</p>
     *
     * <p>La priorité va au code canonique, celui qu'engendre
     * {@link Entrepot#creerMobile(String)}. Un entrepôt hérité ne l'emporte
     * jamais sur lui. La migration V6 a consolidé les doublons existants et posé
     * un index unique ; ce tri reste le garde-fou si un jeu de données antérieur
     * ressurgit.</p>
     */
    @Override
    public Optional<Entrepot> trouverMobileParCommercial(String matriculeCommercial) {
        String codeCanonique = "MOBILE-" + matriculeCommercial;
        List<Entrepot> mobiles = jpaRepository.findByMatriculeProprietaire(matriculeCommercial)
            .stream()
            .map(mapper::versDomaine)
            .filter(e -> e.getType() == TypeEntrepot.MOBILE)
            .toList();

        return mobiles.stream()
            .filter(e -> codeCanonique.equals(e.getCode()))
            .findFirst()
            .or(() -> mobiles.stream().findFirst());
    }

    @Override
    public Optional<Entrepot> trouverUniqueParType(TypeEntrepot type) {
        return jpaRepository.findByType(type.name()).stream().findFirst().map(mapper::versDomaine);
    }

    @Override
    public boolean existeParCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public List<Entrepot> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Entrepot> trouverParType(TypeEntrepot type) {
        return jpaRepository.findByType(type.name()).stream().map(mapper::versDomaine).toList();
    }
}
