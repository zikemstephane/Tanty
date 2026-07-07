package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request.DefinirGrilleTarifaireRequest;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.GrilleTarifaireResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper.GrilleTarifaireMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Définir / réviser un prix homologué (Pilier 3 : Tarification
 * Verrouillée). Réservé au DG et au Directeur Commercial : ce sont les deux
 * seuls rôles habilités à fixer les tarifs appliqués sur le terrain.
 */
@Service
@Transactional
public class DefinirGrilleTarifaireUseCase {

    private final GrilleTarifaireRepository repository;
    private final GrilleTarifaireMapper mapper;

    public DefinirGrilleTarifaireUseCase(GrilleTarifaireRepository repository, GrilleTarifaireMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public GrilleTarifaireResponse executer(DefinirGrilleTarifaireRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.DIRECTEUR_GENERAL && acteur.getRole() != Role.DIRECTEUR_COMMERCIAL
            && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException(
                "Seuls le Directeur Général et le Directeur Commercial peuvent fixer un prix homologué. Rôle '"
                + acteur.getRole().getLibelle() + "' non autorisé.");
        }
        String matricule = acteur.getMatricule().getValeur();

        GrilleTarifaire grille = repository.trouver(request.codeProduit(), request.segment())
            .map(existante -> { existante.reviser(request.prixHomologueFCFA(), matricule); return existante; })
            .orElseGet(() -> GrilleTarifaire.definir(request.codeProduit(), request.segment(),
                request.prixHomologueFCFA(), matricule));

        return mapper.versResponse(repository.sauvegarder(grille));
    }
}
