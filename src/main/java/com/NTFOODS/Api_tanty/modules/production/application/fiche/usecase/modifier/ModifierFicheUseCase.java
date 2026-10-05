package com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request.ModifierFicheRequest;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper.FicheMapper;
import com.NTFOODS.Api_tanty.modules.production.application.lot.service.DeclarationLotDepuisFicheService;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.service.CalculateurPredictionsFiche;
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
    private final CalculateurPredictionsFiche calculateurPredictions;
    private final DeclarationLotDepuisFicheService declarationLotService;

    public ModifierFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                 FicheMapper ficheMapper, CalculateurPredictionsFiche calculateurPredictions,
                                 DeclarationLotDepuisFicheService declarationLotService) {
        this.ficheRepository = ficheRepository;
        this.pphRepository = pphRepository;
        this.ficheMapper = ficheMapper;
        this.calculateurPredictions = calculateurPredictions;
        this.declarationLotService = declarationLotService;
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

        java.util.Map<String, Integer> anciennesQuantites = new java.util.HashMap<>(fiche.getProduitsRealises());

        java.util.List<LigneFicheProduit> nouvellesLignes = request.lignes().stream()
            .map(l -> new LigneFicheProduit(l.codeProduit(), l.nomProduit(), l.quantiteProduite()))
            .toList();

        fiche.modifierLignes(nouvellesLignes);

        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));
        var predictions = calculateurPredictions.calculer(fiche, pph);
        fiche.definirPredictions(predictions.predictionDosage(), predictions.predictionPostes());

        for (LigneFicheProduit ligne : nouvellesLignes) {
            try {
                int nouvelle = (int) Math.round(ligne.getQuantiteProduite());
                int ancienne = anciennesQuantites.getOrDefault(ligne.getCodeProduit(), 0);
                int delta = nouvelle - ancienne;
                if (delta != 0) {
                    pph.ajusterProductionLigne(ligne.getCodeProduit(), delta);
                }
            } catch (IllegalArgumentException | IllegalStateException ignored) {
                // Produit de la fiche non présent dans le PPH ou PPH non EN_COURS — toléré
            }
        }
        pphRepository.sauvegarder(pph);

        FicheProduitJournaliere saved = ficheRepository.sauvegarder(fiche);
        declarationLotService.declarerLots(saved, acteur.getMatricule().getValeur());

        return ficheMapper.versResponse(saved);
    }
}
