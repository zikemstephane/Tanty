package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.ParametreSystemeResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.parametres.ParametresSystemeJpaEntity;
import com.NTFOODS.Api_tanty.shared.infrastructure.parametres.ParametresSystemeJpaRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GererParametresSystemeUseCase {

    private final ParametresSystemeJpaRepository repository;

    public GererParametresSystemeUseCase(ParametresSystemeJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ParametreSystemeResponse> lister() {
        return repository.findAllByOrderByCleAsc().stream()
            .map(this::versResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ParametreSystemeResponse consulter(String cle) {
        return repository.findByCle(cle)
            .map(this::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("ParametreSysteme", cle));
    }

    public List<ParametreSystemeResponse> modifier(Map<String, Double> modifications, String matriculeDg) {
        modifications.forEach((cle, valeur) -> {
            ParametresSystemeJpaEntity p = repository.findByCle(cle)
                .orElseThrow(() -> EntiteNonTrouveeException.de("ParametreSysteme", cle));
            p.setValeurNumerique(valeur);
            p.setModifiePar(matriculeDg);
            p.setHorodatage(LocalDateTime.now());
            repository.save(p);
        });
        return lister();
    }

    private ParametreSystemeResponse versResponse(ParametresSystemeJpaEntity e) {
        return new ParametreSystemeResponse(
            e.getId(), e.getCle(), e.getValeurNumerique(),
            e.getDescription(), e.getModifiePar(), e.getHorodatage()
        );
    }
}
