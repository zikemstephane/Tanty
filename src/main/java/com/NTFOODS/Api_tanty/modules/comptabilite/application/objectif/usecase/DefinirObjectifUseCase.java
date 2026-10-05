package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.request.DefinirObjectifRequest;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response.ObjectifCommercialResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.mapper.ObjectifCommercialMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.service.ParametrageObjectifService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Définir ou réviser les objectifs hebdomadaires d'un commercial
 * (global + par gamme). Ce sont eux qui servent de dénominateur à la Règle d'or
 * des Primes (80/75).
 *
 * <p>Une redéfinition sur une semaine déjà pourvue est une révision : elle prend
 * effet immédiatement sur la prime en cours, et inscrit la valeur remplacée au
 * journal de l'objectif. Le motif devient alors obligatoire.</p>
 */
@Service
@Transactional
public class DefinirObjectifUseCase {

    private final ObjectifCommercialRepository repository;
    private final ObjectifCommercialMapper mapper;
    private final ParametrageObjectifService parametrageObjectifService;

    public DefinirObjectifUseCase(ObjectifCommercialRepository repository, ObjectifCommercialMapper mapper,
                                   ParametrageObjectifService parametrageObjectifService) {
        this.repository = repository;
        this.mapper = mapper;
        this.parametrageObjectifService = parametrageObjectifService;
    }

    public ObjectifCommercialResponse executer(DefinirObjectifRequest request, Utilisateur acteur) {
        // Le contrôle d'accès est porté par la permission COMPTABILITE_OBJECTIF_GERER
        // sur le contrôleur. Le doubler ici par une liste de rôles écrite en dur
        // excluait la Chargée RP, qui détient pourtant cette permission depuis
        // qu'elle assure le rôle fonctionnel de Directeur Commercial.
        String matriculeAuteur = acteur.getMatricule().getValeur();

        ObjectifCommercial objectif = repository.trouver(request.matriculeCommercial(), request.semaineDebut())
            .map(existant -> {
                existant.reviser(request.objectifGlobalFCFA(), request.objectifsParGammeFCFA(),
                    matriculeAuteur, request.motif());
                return existant;
            })
            .orElseGet(() -> {
                // Le nombre de jours ouvrés est figé sur l'objectif à sa création :
                // modifier le paramétrage ne redécoupe pas les objectifs déjà fixés.
                int joursOuvres = parametrageObjectifService
                    .parametrageApplicableA(request.semaineDebut()).getNombreJoursOuvres();
                return ObjectifCommercial.definir(request.matriculeCommercial(), request.semaineDebut(),
                    request.semaineFin(), request.objectifGlobalFCFA(), request.objectifsParGammeFCFA(),
                    joursOuvres, matriculeAuteur);
            });

        return mapper.versResponse(repository.sauvegarder(objectif));
    }
}
