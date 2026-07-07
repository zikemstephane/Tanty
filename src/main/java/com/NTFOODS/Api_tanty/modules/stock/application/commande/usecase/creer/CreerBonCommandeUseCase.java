package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request.CreerBonCommandeRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.modules.stock.domain.fournisseur.repository.FournisseurRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — créer un Bon de Commande (BROUILLON) avec ses lignes.
 * Numéro auto-généré : BC-{année}-{séquence sur 4 chiffres}.
 */
@Service
@Transactional
public class CreerBonCommandeUseCase {

    private final BonCommandeRepository bonCommandeRepository;
    private final FournisseurRepository fournisseurRepository;
    private final BonCommandeMapper bonCommandeMapper;

    public CreerBonCommandeUseCase(BonCommandeRepository bonCommandeRepository,
                                   FournisseurRepository fournisseurRepository,
                                   BonCommandeMapper bonCommandeMapper) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.bonCommandeMapper = bonCommandeMapper;
    }

    public BonCommandeResponse executer(CreerBonCommandeRequest request, Utilisateur acteur) {
        if (!BonCommandeSpecification.peutCreer(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas créer un BC");
        }
        String codeFournisseur = request.codeFournisseur().trim().toUpperCase();
        fournisseurRepository.trouverParCode(codeFournisseur)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Fournisseur", codeFournisseur));

        String numero = genererNumero(request.dateCommande());
        BonCommande bc = BonCommande.creer(numero, codeFournisseur, request.dateCommande(),
            request.dateLivraisonPrevue(), acteur.getMatricule().getValeur());

        for (CreerBonCommandeRequest.LigneRequest l : request.lignes()) {
            bc.ajouterLigne(l.codeProduit(), l.designation(), l.sku(), l.quantiteCommandee(), l.prixUnitaireHT());
        }

        return bonCommandeMapper.versResponse(bonCommandeRepository.sauvegarder(bc));
    }

    private String genererNumero(LocalDate dateCommande) {
        int annee = dateCommande != null ? dateCommande.getYear() : LocalDate.now().getYear();
        long sequence = bonCommandeRepository.compterParAnnee(annee) + 1;
        return String.format("BC-%d-%04d", annee, sequence);
    }
}
