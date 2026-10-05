package com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.RemiseGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service.TarificationGuardService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — Enregistrement d'une vente terrain.
 *
 * <p>Cette logique vivait dans {@code VenteController}. Elle en a été extraite
 * parce qu'un second appelant en a désormais besoin : la synchronisation, qui
 * rejoue au serveur les ventes saisies hors ligne. La réécrire côté synchro
 * aurait dupliqué les règles de tarification — et une copie finit toujours par
 * diverger de l'originale.</p>
 *
 * <p>Prix et remise sont l'un comme l'autre arrêtés par le serveur : le prix est
 * vérifié contre la grille homologuée, la remise est déduite des promotions en
 * vigueur. La valeur transmise par le client ne fait jamais foi, qu'elle arrive
 * en ligne ou depuis une file d'attente hors ligne.</p>
 */
@Service
@Transactional
public class EnregistrerVenteUseCase {

    private final VenteRepository venteRepository;
    private final TarificationGuardService tarificationGuardService;
    private final RemiseGuardService remiseGuardService;
    private final GenerateurNumeroMetier generateurNumero;

    public EnregistrerVenteUseCase(VenteRepository venteRepository,
                                    TarificationGuardService tarificationGuardService,
                                    RemiseGuardService remiseGuardService,
                                    GenerateurNumeroMetier generateurNumero) {
        this.venteRepository = venteRepository;
        this.tarificationGuardService = tarificationGuardService;
        this.remiseGuardService = remiseGuardService;
        this.generateurNumero = generateurNumero;
    }

    public Vente executer(CommandeVente commande) {
        if (commande.photoProduits() == null || commande.photoProduits().isBlank()) {
            throw new IllegalArgumentException("La photo des produits est obligatoire pour enregistrer une vente");
        }

        String numero = generateurNumero.genererNumero(FamilleNumero.VENTE, commande.date());

        List<LigneVente> lignes = commande.lignes().stream()
            .map(l -> {
                tarificationGuardService.validerPrix(commande.codeClient(), l.codeProduit(),
                    l.prixUnitaire(), l.referencePrixExceptionnel());

                double montantBrut = l.quantite() * l.prixUnitaire();
                double remiseRetenue = remiseGuardService.resoudreRemise(commande.codeClient(),
                    l.codeProduit(), montantBrut, l.remise());

                return new LigneVente(l.codeProduit(), l.designation(), l.conditionnement(),
                    l.quantite(), l.prixUnitaire(), remiseRetenue);
            })
            .toList();

        Vente vente = Vente.creer(numero, commande.uuidVente(), commande.matriculeCommercial(),
            commande.codeClient(), commande.date(), commande.modeReglement(), commande.typeVente(), lignes,
            commande.photoProduits());
        vente.enregistrerPosition(commande.coordonneesGPS());
        vente.rattacherATournee(commande.idFeuillRoute());
        if (commande.signatureClient()) {
            vente.signerClient();
        }
        return venteRepository.sauvegarder(vente);
    }

    public record CommandeVente(String matriculeCommercial, String codeClient, LocalDate date,
                                 ModeReglement modeReglement, TypeVente typeVente,
                                 String coordonneesGPS, String idFeuillRoute,
                                 String uuidVente, boolean signatureClient,
                                 String photoProduits,
                                 List<LigneCommande> lignes) {}

    public record LigneCommande(String codeProduit, String designation, String conditionnement,
                                 int quantite, double prixUnitaire, double remise,
                                 String referencePrixExceptionnel) {}
}
