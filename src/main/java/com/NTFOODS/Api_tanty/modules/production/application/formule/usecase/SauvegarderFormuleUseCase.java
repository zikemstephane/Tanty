package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request.SauvegarderFormuleRequest;
import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.response.ReferentielProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Use Case — Créer ou remplacer la formule de production d'un produit.
 * Les invariants (somme des proportions = base, matières distinctes) sont
 * portés par {@link FormuleProduction} ; une violation ressort en 400.
 * N'affecte pas les PPH déjà générés : un recalcul explicite reste nécessaire.
 */
@Service
@Transactional
public class SauvegarderFormuleUseCase {

    private final FormuleProductionRepository formuleRepository;
    private final ProduitRepository produitRepository;

    public SauvegarderFormuleUseCase(FormuleProductionRepository formuleRepository, ProduitRepository produitRepository) {
        this.formuleRepository = formuleRepository;
        this.produitRepository = produitRepository;
    }

    public ReferentielProductionResponse executer(String codeProduit, SauvegarderFormuleRequest request) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", codeProduit));
        String code = produit.getCode().getValeur();

        List<LigneFormule> lignes = request.lignes().stream()
            .map(l -> new LigneFormule(l.matierePremiere(), l.proportionKg()))
            .toList();

        Optional<FormuleProduction> existante = formuleRepository.trouverParCodeProduit(code);
        FormuleProduction formule = existante
            .map(e -> FormuleProduction.reconstituer(e.getId(), code, request.baseKg(), lignes,
                e.getDateCreation(), LocalDateTime.now(), e.getVersion()))
            .orElseGet(() -> FormuleProduction.creer(code, request.baseKg(), lignes));

        return ListerReferentielProductionUseCase.versResponse(produit, formuleRepository.sauvegarder(formule));
    }
}
