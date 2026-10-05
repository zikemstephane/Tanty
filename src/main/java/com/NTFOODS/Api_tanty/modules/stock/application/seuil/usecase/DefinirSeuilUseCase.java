package com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.request.DefinirSeuilRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response.SeuilResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.HistoriqueSeuilJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.HistoriqueSeuilJpaRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Use Case — définir / mettre à jour un seuil de réapprovisionnement.
 *
 * GOUVERNANCE DES SEUILS : c'est le DG (Directeur Général) qui définit et
 * modifie TOUS les seuils du système. Aucun autre rôle métier ne peut
 * modifier un seuil (le rôle ADMIN reste habilité pour la maintenance
 * technique du système). Chaque changement est tracé dans un historique
 * append-only (qui, quand, ancienne valeur → nouvelle valeur).
 */
@Service
@Transactional
public class DefinirSeuilUseCase {

    private final SeuilStockRepository seuilRepository;
    private final HistoriqueSeuilJpaRepository historiqueRepository;

    public DefinirSeuilUseCase(SeuilStockRepository seuilRepository,
                                HistoriqueSeuilJpaRepository historiqueRepository) {
        this.seuilRepository = seuilRepository;
        this.historiqueRepository = historiqueRepository;
    }

    public SeuilResponse executer(DefinirSeuilRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.DIRECTEUR_GENERAL && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException(
                "Seul le Directeur Général peut définir ou modifier un seuil. Rôle '"
                + acteur.getRole().getLibelle() + "' non autorisé.");
        }
        TypeEntrepot type = TypeEntrepot.valueOf(request.typeEntrepot().trim().toUpperCase());
        String code = request.codeProduit().trim().toUpperCase();

        long ancienneValeur = seuilRepository.trouver(code, type)
            .map(SeuilStock::getSeuilReapprovisionnement)
            .orElse(0L);

        SeuilStock seuil = seuilRepository.trouver(code, type)
            .map(existant -> { existant.modifierSeuil(request.seuilReapprovisionnement()); return existant; })
            .orElseGet(() -> SeuilStock.definir(code, type, request.seuilReapprovisionnement()));

        SeuilStock sauvegarde = seuilRepository.sauvegarder(seuil);

        HistoriqueSeuilJpaEntity historique = new HistoriqueSeuilJpaEntity();
        historique.setCodeProduit(code);
        historique.setTypeEntrepot(type.name());
        historique.setAncienneValeur(ancienneValeur);
        historique.setNouvelleValeur(request.seuilReapprovisionnement());
        historique.setMatriculeAuteur(acteur.getMatricule().getValeur());
        historique.setDateModification(LocalDateTime.now());
        historiqueRepository.save(historique);

        return new SeuilResponse(sauvegarde.getId(), sauvegarde.getCodeProduit(),
            sauvegarde.getTypeEntrepot().name(), sauvegarde.getSeuilReapprovisionnement(), sauvegarde.isActif());
    }
}
