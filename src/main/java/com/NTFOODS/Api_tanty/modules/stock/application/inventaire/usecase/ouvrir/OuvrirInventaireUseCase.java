package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.ouvrir;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request.OuvrirInventaireRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.mapper.InventaireMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.TypeInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — ouvrir une session d'inventaire. Les lignes de comptage sont
 * pré-remplies depuis le stock théorique courant du périmètre choisi.
 */
@Service
@Transactional
public class OuvrirInventaireUseCase {

    private final InventaireRepository inventaireRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final ProduitRepository produitRepository;
    private final InventaireMapper inventaireMapper;

    public OuvrirInventaireUseCase(InventaireRepository inventaireRepository,
                                   NiveauStockRepository niveauStockRepository,
                                   ProduitRepository produitRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.produitRepository = produitRepository;
        this.inventaireMapper = inventaireMapper;
    }

    public InventaireResponse executer(OuvrirInventaireRequest request, Utilisateur acteur) {
        TypeInventaire type = parserType(request.type());
        String entrepot = request.codeEntrepot().trim().toUpperCase();

        SessionInventaire session = SessionInventaire.ouvrir(genererNumero(), type, entrepot,
            perimetre(request), acteur.getMatricule().getValeur(), acteur.getRole());

        List<NiveauStock> niveaux = niveauStockRepository.trouverParEntrepot(entrepot);
        for (NiveauStock niveau : niveaux) {
            if (!correspondAuPerimetre(niveau.getCodeProduit(), request)) continue;
            session.ajouterLigne(niveau.getCodeProduit(), entrepot, niveau.getQuantiteEnBase());
        }

        return inventaireMapper.versResponse(inventaireRepository.sauvegarder(session));
    }

    private boolean correspondAuPerimetre(String codeProduit, OuvrirInventaireRequest request) {
        if (request.perimetreProduit() != null && !request.perimetreProduit().isBlank()) {
            return codeProduit.equalsIgnoreCase(request.perimetreProduit().trim());
        }
        if (request.perimetreGamme() != null && !request.perimetreGamme().isBlank()) {
            return produitRepository.trouverParCode(CodeProduit.de(codeProduit))
                .map(p -> p.getGamme().equalsIgnoreCase(request.perimetreGamme().trim()))
                .orElse(false);
        }
        return true;
    }

    private String perimetre(OuvrirInventaireRequest request) {
        if (request.perimetreProduit() != null && !request.perimetreProduit().isBlank()) return "Produit: " + request.perimetreProduit();
        if (request.perimetreGamme() != null && !request.perimetreGamme().isBlank()) return "Gamme: " + request.perimetreGamme();
        return "Tout l'entrepôt";
    }

    private TypeInventaire parserType(String valeur) {
        try {
            return TypeInventaire.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type d'inventaire invalide : '" + valeur + "'");
        }
    }

    private String genererNumero() {
        int annee = LocalDate.now().getYear();
        long sequence = inventaireRepository.compterParAnnee(annee) + 1;
        return String.format("INV-%d-%04d", annee, sequence);
    }
}
