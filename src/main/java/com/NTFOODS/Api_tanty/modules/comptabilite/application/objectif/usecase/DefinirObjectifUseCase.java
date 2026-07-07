package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.request.DefinirObjectifRequest;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response.ObjectifCommercialResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.mapper.ObjectifCommercialMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Définir les objectifs hebdomadaires d'un commercial (global +
 * par gamme). Réservé au DG et au Contrôleur Général : ce sont les objectifs
 * qui serviront de dénominateur à la Règle d'or des Primes (80/75).
 */
@Service
@Transactional
public class DefinirObjectifUseCase {

    private final ObjectifCommercialRepository repository;
    private final ObjectifCommercialMapper mapper;

    public DefinirObjectifUseCase(ObjectifCommercialRepository repository, ObjectifCommercialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ObjectifCommercialResponse executer(DefinirObjectifRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.DIRECTEUR_GENERAL && acteur.getRole() != Role.CONTROLEUR_GENERAL
            && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException(
                "Seuls le Directeur Général et le Contrôleur Général peuvent fixer les objectifs commerciaux. Rôle '"
                + acteur.getRole().getLibelle() + "' non autorisé.");
        }
        String matriculeAuteur = acteur.getMatricule().getValeur();

        ObjectifCommercial objectif = repository.trouver(request.matriculeCommercial(), request.semaineDebut())
            .map(existant -> {
                existant.reviser(request.objectifGlobalFCFA(), request.objectifsParGammeFCFA(), matriculeAuteur);
                return existant;
            })
            .orElseGet(() -> ObjectifCommercial.definir(request.matriculeCommercial(), request.semaineDebut(),
                request.semaineFin(), request.objectifGlobalFCFA(), request.objectifsParGammeFCFA(), matriculeAuteur));

        return mapper.versResponse(repository.sauvegarder(objectif));
    }
}
