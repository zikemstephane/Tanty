package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/**
 * Type d'alerte stock, avec destinataire principal et action suggérée
 * (notification actionnable).
 */
public enum TypeAlerte {

    TAMPON_SOUS_SEUIL("Tampon sous seuil", Role.GESTIONNAIRE_STOCK, "Lancer un transfert central → tampon", PrioriteAlerte.HAUTE),
    CENTRAL_SOUS_SEUIL("Stock central sous seuil", Role.GESTIONNAIRE_STOCK, "Créer un bon de commande", PrioriteAlerte.HAUTE),
    CENTRAL_SURSTOCK("Surstock central", Role.GESTIONNAIRE_STOCK, "Lancer une promotion ou transfert", PrioriteAlerte.MOYENNE),
    PEREMPTION_IMMINENTE("Péremption imminente", Role.GESTIONNAIRE_STOCK, "Prioriser la sortie des produits concernés", PrioriteAlerte.CRITIQUE),
    BC_RETARD_LIVRAISON("Bon de commande en retard", Role.GESTIONNAIRE_STOCK, "Relancer le fournisseur", PrioriteAlerte.HAUTE),
    ECART_RECEPTION("Écart de réception important", Role.DIRECTEUR_GENERAL, "Consulter le détail de l'écart", PrioriteAlerte.MOYENNE),
    STOCK_MOBILE_ANORMAL("Stock mobile anormalement élevé", Role.GESTIONNAIRE_STOCK, "Voir le stock mobile du commercial", PrioriteAlerte.MOYENNE),
    STOCK_MOBILE_RUPTURE("Commercial en rupture de stock", Role.GESTIONNAIRE_STOCK, "Préparer une dotation urgente", PrioriteAlerte.CRITIQUE),
    ROTATION_LENTE("Rotation lente — stock dormant", Role.GESTIONNAIRE_STOCK, "Analyser les produits à rotation lente", PrioriteAlerte.BASSE),
    ANOMALIE_VENTES("Anomalie dans les ventes", Role.DIRECTEUR_COMMERCIAL, "Consulter l'historique des ventes", PrioriteAlerte.MOYENNE),
    NOUVELLE_DOTATION("Nouvelle demande de dotation", Role.GESTIONNAIRE_STOCK, "Consulter et approuver/modifier", PrioriteAlerte.MOYENNE);

    private final String libelle;
    private final Role destinataire;
    private final String actionSuggeree;
    private final PrioriteAlerte priorite;

    TypeAlerte(String libelle, Role destinataire, String actionSuggeree, PrioriteAlerte priorite) {
        this.libelle = libelle;
        this.destinataire = destinataire;
        this.actionSuggeree = actionSuggeree;
        this.priorite = priorite;
    }

    public String getLibelle() { return libelle; }
    public Role getDestinataire() { return destinataire; }
    public String getActionSuggeree() { return actionSuggeree; }
    public PrioriteAlerte getPriorite() { return priorite; }
}
