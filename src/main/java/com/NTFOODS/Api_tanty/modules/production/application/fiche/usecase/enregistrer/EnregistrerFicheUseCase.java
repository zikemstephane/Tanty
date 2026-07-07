package com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.enregistrer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request.EnregistrerFicheRequest;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper.FicheMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity.LigneFicheProduit;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.specification.FicheSpecification;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — enregistrer une fiche produit journalière et cumuler la production dans le PPH.
 */
@Service
@Transactional
public class EnregistrerFicheUseCase {

    private final FicheProduitRepository ficheRepository;
    private final PPHRepository pphRepository;
    private final FicheMapper ficheMapper;
    private final EventPublisher eventPublisher;

    public EnregistrerFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                   FicheMapper ficheMapper, EventPublisher eventPublisher) {
        this.ficheRepository = ficheRepository;
        this.pphRepository = pphRepository;
        this.ficheMapper = ficheMapper;
        this.eventPublisher = eventPublisher;
    }

    public FicheResponse executer(EnregistrerFicheRequest req, Utilisateur acteur) {
        if (!FicheSpecification.peutEnregistrer(acteur.getRole())) {
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle() + "' ne peut pas enregistrer une fiche");
        }
        if (ficheRepository.existeParPPHEtDate(req.referencePPH(), req.date())) {
            throw new RegleMetierException("Une fiche existe déjà pour le PPH " + req.referencePPH() + " à la date " + req.date());
        }

        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(req.referencePPH())
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", req.referencePPH()));

        List<LigneFicheProduit> lignes = req.lignes().stream()
            .map(l -> new LigneFicheProduit(l.codeProduit(), l.nomProduit(), l.quantiteProduite()))
            .toList();

        FicheProduitJournaliere fiche = FicheProduitJournaliere.enregistrer(
            req.referencePPH(), req.semaine(), req.date(), req.matriculeAgentProduction(), lignes,
            req.predictionDosage(), req.predictionPostes());

        for (LigneFicheProduit ligne : lignes) {
            try {
                pph.cumulerProductionLigne(ligne.getCodeProduit(), (int) Math.round(ligne.getQuantiteProduite()));
            } catch (IllegalArgumentException ignored) {
                // Produit de la fiche non présent dans le PPH — toléré
            }
        }
        pphRepository.sauvegarder(pph);

        FicheProduitJournaliere saved = ficheRepository.sauvegarder(fiche);
        fiche.getDomainEvents().forEach(eventPublisher::publish);
        fiche.clearDomainEvents();
        return ficheMapper.versResponse(saved);
    }
}
