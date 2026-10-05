package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request.DefinirGrilleTarifaireRequest;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.GrilleTarifaireResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper.GrilleTarifaireMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Définir / réviser un prix homologué (Pilier 3 : Tarification
 * Verrouillée). Réservé au DG, au Directeur Commercial, à la Chargée RP &amp;
 * Commercial et au Comptable : ce sont les rôles habilités à fixer les
 * tarifs appliqués sur le terrain.
 */
@Service
@Transactional
public class DefinirGrilleTarifaireUseCase {

    private final GrilleTarifaireRepository repository;
    private final GrilleTarifaireMapper mapper;
    private final ProduitRepository produitRepository;

    public DefinirGrilleTarifaireUseCase(GrilleTarifaireRepository repository, GrilleTarifaireMapper mapper,
                                         ProduitRepository produitRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.produitRepository = produitRepository;
    }

    public GrilleTarifaireResponse executer(DefinirGrilleTarifaireRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.DIRECTEUR_GENERAL && acteur.getRole() != Role.DIRECTEUR_COMMERCIAL
            && acteur.getRole() != Role.CHARGEE_RP && acteur.getRole() != Role.COMPTABLE
            && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException(
                "Seuls le Directeur Général, le Directeur Commercial, la Chargée RP & Commercial et le "
                + "Comptable peuvent fixer un prix homologué. Rôle '"
                + acteur.getRole().getLibelle() + "' non autorisé.");
        }
        // Le produit doit exister et être actif. Sans ce contrôle, une faute de
        // frappe dans le code créait un prix orphelin — accepté, enregistré,
        // jamais appliqué — pendant que le vrai produit restait sans prix
        // homologué. Le terrain vendait alors hors grille sans que rien ne le
        // signale.
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(request.codeProduit()))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", request.codeProduit()));
        if (!produit.isActif()) {
            throw new RegleMetierException(
                "Le produit " + request.codeProduit() + " est désactivé : il ne peut pas recevoir de prix homologué");
        }

        String matricule = acteur.getMatricule().getValeur();

        GrilleTarifaire grille = repository.trouver(request.codeProduit(), request.segment())
            .map(existante -> { existante.reviser(request.prixHomologueFCFA(), matricule); return existante; })
            .orElseGet(() -> GrilleTarifaire.definir(request.codeProduit(), request.segment(),
                request.prixHomologueFCFA(), matricule));

        return mapper.versResponse(repository.sauvegarder(grille));
    }
}
