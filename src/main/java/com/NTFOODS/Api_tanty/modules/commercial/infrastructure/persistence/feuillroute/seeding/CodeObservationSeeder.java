package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.CodeObservationJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa.CodeObservationJpaRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.seeding.DataSeeder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class CodeObservationSeeder implements DataSeeder {

    private final CodeObservationJpaRepository repository;

    public CodeObservationSeeder(CodeObservationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        seedIfAbsent("Ab", "Absence responsable", false, 1);
        seedIfAbsent("NC", "Nouveau client", false, 2);
        seedIfAbsent("PA", "Pas d'argent", false, 3);
        seedIfAbsent("VPC", "Vend produits concurrents", false, 4);
        seedIfAbsent("NVP", "Ne vend plus", false, 5);
        seedIfAbsent("M", "Mévente", false, 6);
        seedIfAbsent("PR", "Promotion", false, 7);
        seedIfAbsent("F", "Fermé", false, 8);
        seedIfAbsent("S", "Seau", false, 9);
        seedIfAbsent("EV", "Emballage vide", true, 10);
        seedIfAbsent("AV", "Avaries", true, 11);
        seedIfAbsent("AF", "Affiche", true, 12);
    }

    private void seedIfAbsent(String code, String libelle, boolean requireQuantity, int ordre) {
        if (repository.findByCode(code).isEmpty()) {
            CodeObservationJpaEntity e = new CodeObservationJpaEntity();
            e.setCode(code);
            e.setLibelle(libelle);
            e.setRequireQuantity(requireQuantity);
            e.setActif(true);
            e.setOrdre(ordre);
            repository.save(e);
        }
    }
}
