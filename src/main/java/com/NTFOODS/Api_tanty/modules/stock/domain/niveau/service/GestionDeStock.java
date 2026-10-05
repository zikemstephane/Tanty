package com.NTFOODS.Api_tanty.modules.stock.domain.niveau.service;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;

/**
 * Service de domaine — applique les variations de stock de façon cohérente.
 *
 * Mute les agrégats {@link NiveauStock} passés en argument et produit le
 * {@link MouvementStock} correspondant (registre). Pur (sans dépendance
 * d'infrastructure) : la persistance et la génération de référence sont
 * orchestrées par la couche application.
 */
public final class GestionDeStock {

    private GestionDeStock() {}

    /** Entrée pure dans un entrepôt (réception, retour, ajustement positif). */
    public static MouvementStock appliquerEntree(NiveauStock cible, TypeMouvement type, String reference,
                                                 String sku, long quantiteEnBase, String matriculeActeur,
                                                 String referenceDocument, String motif) {
        cible.entrer(quantiteEnBase);
        return MouvementStock.enregistrer(reference, type, cible.getCodeProduit(), sku, quantiteEnBase,
            null, cible.getCodeEntrepot(), matriculeActeur, referenceDocument, motif);
    }

    /** Sortie pure d'un entrepôt (dotation depuis le tampon). */
    public static MouvementStock appliquerSortie(NiveauStock source, TypeMouvement type, String reference,
                                                 String sku, long quantiteEnBase, String matriculeActeur,
                                                 String referenceDocument, String motif) {
        source.sortir(quantiteEnBase);
        return MouvementStock.enregistrer(reference, type, source.getCodeProduit(), sku, quantiteEnBase,
            source.getCodeEntrepot(), null, matriculeActeur, referenceDocument, motif);
    }

    /** Transfert entre deux entrepôts (central → tampon) en une seule écriture de registre. */
    public static MouvementStock appliquerTransfert(NiveauStock source, NiveauStock cible, String reference,
                                                    String sku, long quantiteEnBase, String matriculeActeur,
                                                    String referenceDocument, String motif) {
        if (!source.getCodeProduit().equals(cible.getCodeProduit())) {
            throw new IllegalArgumentException("Transfert impossible : produits source et cible différents");
        }
        source.sortir(quantiteEnBase);
        cible.entrer(quantiteEnBase);
        return MouvementStock.enregistrer(reference, TypeMouvement.TRANSFERT_CENTRAL_VERS_TAMPON,
            source.getCodeProduit(), sku, quantiteEnBase, source.getCodeEntrepot(), cible.getCodeEntrepot(),
            matriculeActeur, referenceDocument, motif);
    }
}
