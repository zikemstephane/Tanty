package com.NTFOODS.Api_tanty.modules.stock.application.entrepot.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.request.CreerEntrepotRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.response.EntrepotResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.mapper.EntrepotMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — créer un entrepôt (central, tampon ou mobile).
 * Accessible : Gestionnaire de Stock, DG, Admin.
 */
@Service
@Transactional
public class CreerEntrepotUseCase {

    private final EntrepotRepository entrepotRepository;
    private final EntrepotMapper entrepotMapper;

    public CreerEntrepotUseCase(EntrepotRepository entrepotRepository, EntrepotMapper entrepotMapper) {
        this.entrepotRepository = entrepotRepository;
        this.entrepotMapper = entrepotMapper;
    }

    public EntrepotResponse executer(CreerEntrepotRequest request, Utilisateur acteur) {
        Role role = acteur.getRole();
        if (role != Role.GESTIONNAIRE_STOCK && role != Role.DIRECTEUR_GENERAL && role != Role.ADMIN) {
            throw new AccesRefuseException("Le rôle '" + role.getLibelle() + "' ne peut pas créer un entrepôt");
        }

        TypeEntrepot type = parserType(request.type());
        Entrepot entrepot = switch (type) {
            case CENTRAL -> Entrepot.creerCentral(exigerCode(request), libelleOuDefaut(request, "Stock central"));
            case TAMPON -> Entrepot.creerTampon(exigerCode(request), libelleOuDefaut(request, "Stock tampon"));
            case MOBILE -> {
                if (request.matriculeCommercial() == null || request.matriculeCommercial().isBlank()) {
                    throw new IllegalArgumentException("Un entrepôt MOBILE requiert le matricule du commercial");
                }
                yield Entrepot.creerMobile(request.matriculeCommercial());
            }
        };

        if (entrepotRepository.existeParCode(entrepot.getCode())) {
            throw new RegleMetierException("Un entrepôt existe déjà avec le code : " + entrepot.getCode());
        }
        return entrepotMapper.versResponse(entrepotRepository.sauvegarder(entrepot));
    }

    private TypeEntrepot parserType(String valeur) {
        try {
            return TypeEntrepot.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type d'entrepôt invalide : '" + valeur + "'");
        }
    }

    private String exigerCode(CreerEntrepotRequest request) {
        if (request.code() == null || request.code().isBlank()) {
            throw new IllegalArgumentException("Le code est obligatoire pour un entrepôt central ou tampon");
        }
        return request.code().trim().toUpperCase();
    }

    private String libelleOuDefaut(CreerEntrepotRequest request, String defaut) {
        return (request.libelle() == null || request.libelle().isBlank()) ? defaut : request.libelle().trim();
    }
}
