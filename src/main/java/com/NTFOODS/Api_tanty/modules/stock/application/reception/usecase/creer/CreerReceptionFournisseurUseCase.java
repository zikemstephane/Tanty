package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionFournisseurRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — créer une réception fournisseur (MP / consommables), liée à un BC.
 * Le Gestionnaire saisit les quantités attendues et réellement reçues (écart auto).
 */
@Service
@Transactional
public class CreerReceptionFournisseurUseCase {

    @Value("${app.stock.seuil-ecart-reception-pourcentage:10.0}")
    private double seuilEcart;

    private final ReceptionRepository receptionRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final EntrepotRepository entrepotRepository;
    private final ReceptionMapper receptionMapper;

    public CreerReceptionFournisseurUseCase(ReceptionRepository receptionRepository,
                                            BonCommandeRepository bonCommandeRepository,
                                            EntrepotRepository entrepotRepository,
                                            ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.entrepotRepository = entrepotRepository;
        this.receptionMapper = receptionMapper;
    }

    public ReceptionResponse executer(CreerReceptionFournisseurRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.GESTIONNAIRE_STOCK && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Gestionnaire de Stock peut saisir une réception");
        }
        CategorieProduit categorie = parserCategorie(request.categorie());
        String numeroBC = request.numeroBonCommande();
        bonCommandeRepository.trouverParNumero(numeroBC)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Bon de commande", numeroBC));

        String entrepotCible = resoudreEntrepotCentral(request.codeEntrepotCible());
        String numero = genererNumero();

        Reception reception = Reception.creerFournisseur(numero, categorie, request.codeFournisseur().trim().toUpperCase(),
            numeroBC, entrepotCible, seuilEcart, LocalDate.now());

        for (var l : request.lignes()) {
            reception.ajouterLigne(l.codeProduit(), l.sku(), l.quantiteAttendue(), l.quantiteRecue(), l.idLigneBC());
        }
        return receptionMapper.versResponse(receptionRepository.sauvegarder(reception));
    }

    private CategorieProduit parserCategorie(String valeur) {
        CategorieProduit categorie;
        try {
            categorie = CategorieProduit.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Catégorie invalide : '" + valeur + "'");
        }
        return categorie;
    }

    private String resoudreEntrepotCentral(String codeFourni) {
        if (codeFourni != null && !codeFourni.isBlank()) return codeFourni.trim().toUpperCase();
        return entrepotRepository.trouverUniqueParType(TypeEntrepot.CENTRAL)
            .map(e -> e.getCode())
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt central configuré"));
    }

    private String genererNumero() {
        int annee = LocalDate.now().getYear();
        long sequence = receptionRepository.compterParAnnee(annee) + 1;
        return String.format("BR-%d-%04d", annee, sequence);
    }
}
