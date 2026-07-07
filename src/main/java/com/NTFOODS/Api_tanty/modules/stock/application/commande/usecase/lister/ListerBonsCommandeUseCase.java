package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerBonsCommandeUseCase {

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;

    public ListerBonsCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
    }

    public List<BonCommandeResponse> executer(String statut, String fournisseur, boolean enRetard) {
        List<com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande> bcs;
        if (enRetard) {
            bcs = bonCommandeRepository.trouverEnRetard();
        } else if (statut != null && !statut.isBlank()) {
            bcs = bonCommandeRepository.trouverParStatut(StatutBonCommande.valueOf(statut.trim().toUpperCase()));
        } else if (fournisseur != null && !fournisseur.isBlank()) {
            bcs = bonCommandeRepository.trouverParFournisseur(fournisseur.trim().toUpperCase());
        } else {
            bcs = bonCommandeRepository.trouverTous();
        }
        return bcs.stream().map(bonCommandeMapper::versResponse).toList();
    }
}
