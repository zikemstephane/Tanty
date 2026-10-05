package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.ClassementCommercialResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ObtenirClassementDgUseCase {

    private final VenteJpaRepository venteJpaRepository;
    private final UtilisateurJpaRepository utilisateurJpaRepository;

    public ObtenirClassementDgUseCase(VenteJpaRepository venteJpaRepository,
                                      UtilisateurJpaRepository utilisateurJpaRepository) {
        this.venteJpaRepository = venteJpaRepository;
        this.utilisateurJpaRepository = utilisateurJpaRepository;
    }

    public List<ClassementCommercialResponse> executer() {
        LocalDate debut = LocalDate.now().withDayOfMonth(1);
        LocalDate fin = LocalDate.now();

        List<VenteJpaEntity> ventes = venteJpaRepository.findByDateBetween(debut, fin);

        Map<String, Double> caParCommercial = new HashMap<>();
        Map<String, Long> nbVentesParCommercial = new HashMap<>();

        for (VenteJpaEntity vente : ventes) {
            String matricule = vente.getMatriculeCommercial();
            double ca = vente.getLignes().stream()
                .mapToDouble(l -> l.montantHT())
                .sum();
            caParCommercial.merge(matricule, ca, Double::sum);
            nbVentesParCommercial.merge(matricule, 1L, Long::sum);
        }

        Map<String, String> nomsCommerciaux = utilisateurJpaRepository.findByRole(Role.COMMERCIAL)
            .stream()
            .collect(Collectors.toMap(
                UtilisateurJpaEntity::getMatricule,
                u -> u.getPrenom() + " " + u.getNom()
            ));

        List<Map.Entry<String, Double>> classement = new ArrayList<>(caParCommercial.entrySet());
        classement.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        List<ClassementCommercialResponse> resultat = new ArrayList<>();
        for (int i = 0; i < classement.size(); i++) {
            String matricule = classement.get(i).getKey();
            resultat.add(new ClassementCommercialResponse(
                i + 1,
                matricule,
                nomsCommerciaux.getOrDefault(matricule, matricule),
                classement.get(i).getValue(),
                nbVentesParCommercial.getOrDefault(matricule, 0L)
            ));
        }
        return resultat;
    }
}
