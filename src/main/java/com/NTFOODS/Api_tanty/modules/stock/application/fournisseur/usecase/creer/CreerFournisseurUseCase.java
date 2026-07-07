package com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.request.CreerFournisseurRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.response.FournisseurResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.mapper.FournisseurMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.model.Fournisseur;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.repository.FournisseurRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreerFournisseurUseCase {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    public CreerFournisseurUseCase(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }

    public FournisseurResponse executer(CreerFournisseurRequest request, Utilisateur acteur) {
        if (!BonCommandeSpecification.peutGerer(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas gérer les fournisseurs");
        }
        String code = request.code().trim().toUpperCase();
        if (fournisseurRepository.existeParCode(code)) {
            throw new RegleMetierException("Un fournisseur existe déjà avec le code : " + code);
        }
        Fournisseur f = Fournisseur.creer(code, request.raisonSociale(), request.telephone(),
            request.email(), request.adresse());
        return fournisseurMapper.versResponse(fournisseurRepository.sauvegarder(f));
    }
}
