package com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.response.FournisseurResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.mapper.FournisseurMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.repository.FournisseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerFournisseursUseCase {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    public ListerFournisseursUseCase(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }

    public List<FournisseurResponse> executer(String recherche) {
        var fournisseurs = (recherche == null || recherche.isBlank())
            ? fournisseurRepository.trouverTous()
            : fournisseurRepository.rechercherParRaisonSociale(recherche);
        return fournisseurs.stream().map(fournisseurMapper::versResponse).toList();
    }
}
