package com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request.ModifierFicheRequest;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper.FicheMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.specification.FicheSpecification;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — Corriger les chiffres d'une fiche de production journalière.
 *
 * "LOCK NUMÉRIQUE" (Pilier 1) : refusé si la fiche est déjà verrouillée
 * (le lot de produit fini correspondant a déjà été validé physiquement par
 * le Gestionnaire de Stock).
 */
@Service
@Transactional
public class ModifierFicheUseCase {

    private final FicheProduitRepository ficheRepository;
    private final PPHRepository pphRepository;
    private final FicheMapper ficheMapper;

    public ModifierFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                 FicheMapper ficheMapper) {
        this.ficheRepository = ficheRepository;
        this.pphRepository = pphRepository;
        this.ficheMapper = ficheMapper;
    }

    public FicheResponse executer(String referencePPH, LocalDate date, ModifierFicheRequest request,
                                   Utilisateur acteur) {
        if (!FicheSpecification.peutEnregistrer(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas modifier une fiche");
        }

        FicheProduitJournaliere fiche = ficheRepository.trouverParPPHEtDate(referencePPH, date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Fiche de production", referencePPH + " / " + date));

        if (fiche.isVerrouillee()) {
            throw new RegleMetierException(
                "Fiche verrouillée : le lot de produit fini correspondant a déjà été validé par le stock. " +
                "Aucune correction n'est plus possible.");
        }

        java.util.List<LigneFicheProduit> nouvellesLignes = request.lignes().stream()
            .map(l -> new LigneFicheProduit(l.codeProduit(), l.nomProduit(), l.quantiteProduite()))
            .toList();

        fiche.modifierLignes(nouvellesLignes);

        // NOTE : le cumul du PPH (PlanProductionHebdomadaire.productionRealisee) n'est PAS
        // recalculé ici. LignePPH.cumulerProduction() refuse les deltas négatifs, ce qui
        // empêcherait de corriger une quantité à la baisse. Une correction propre nécessite
        // une méthode dédiée côté PPH (ex: ajusterProduction(codeProduit, delta)) — à ajouter
        // si des corrections de fiches avant verrouillage s'avèrent fréquentes en pratique.

        return ficheMapper.versResponse(ficheRepository.sauvegarder(fiche));
    }
}
