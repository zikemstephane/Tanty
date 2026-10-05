package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums;

/**
 * Enum — Permissions granulaires du système NTFOODS TANTY.
 *
 * Organisées par module avec un préfixe : STOCK_, PRODUCTION_, COMMERCIAL_,
 * COMPTABILITE_, COMMERCIALISATION_, AUTH_, RH_.
 *
 * Chaque permission représente une action métier précise. Les rôles
 * reçoivent un ensemble de permissions via la {@code RolePermissionMatrix},
 * et des overrides individuels peuvent être appliqués par utilisateur en DB.
 */
public enum Permission {

    // ── Stock ──────────────────────────────────────────────────────────────
    STOCK_PRODUIT_CREER("Créer un produit"),
    STOCK_PRODUIT_MODIFIER("Modifier un produit"),
    STOCK_PRODUIT_CONSULTER("Consulter les produits"),
    STOCK_PRODUIT_DESACTIVER("Désactiver un produit"),
    STOCK_CONDITIONNEMENT_GERER("Gérer les conditionnements (CRUD)"),
    STOCK_FOURNISSEUR_GERER("Gérer les fournisseurs"),
    STOCK_ENTREPOT_GERER("Gérer les entrepôts"),
    STOCK_BON_COMMANDE_CREER("Créer un bon de commande"),
    STOCK_BON_COMMANDE_VALIDER("Valider un bon de commande"),
    STOCK_RECEPTION_GERER("Gérer les réceptions"),
    // Contrôle de second niveau sur une réception — validation finale ou rejet.
    // Volontairement distincte de STOCK_RECEPTION_GERER : celle-ci ouvre la
    // création et l'enregistrement physique, actes du dépôt ; celle-là est le
    // contrôle exercé sur ce que le dépôt a enregistré. Les confondre revenait
    // à demander au dépôt de se contrôler lui-même — et rendait la 2ᵉ validation
    // impossible, le domaine la réservant au Comptable ou au Contrôleur Général,
    // que STOCK_RECEPTION_GERER n'atteint pas.
    STOCK_RECEPTION_VALIDER_ETAPE2("Valider ou rejeter une réception au second niveau"),
    // Traçabilité par lot et suivi des péremptions. Distincte de
    // STOCK_NIVEAU_CONSULTER : consulter les lots est ouvert à tout utilisateur
    // authentifié — savoir qu'un produit périme la semaine prochaine concerne
    // le commercial comme le magasinier ; les déclarer et les prélever revient
    // à qui tient l'entrepôt.
    STOCK_LOT_GERER("Déclarer, prélever et retirer des lots de stock"),
    STOCK_TRANSFERT_GERER("Gérer les bons de transfert"),
    // Soumettre sa dotation est l'acte du commercial : il demande ce qu'il
    // emportera. Volontairement distincte de STOCK_DOTATION_GERER, qui est
    // l'acte du dépôt — approuver, refuser, ajuster une ligne. Les confondre
    // rendait la soumission impossible : le domaine la réserve au commercial,
    // que STOCK_DOTATION_GERER n'atteint pas, et l'ouvrait à ceux que le
    // domaine refuse ensuite.
    STOCK_DOTATION_SOUMETTRE("Soumettre sa dotation de tournée"),
    STOCK_DOTATION_GERER("Gérer les dotations"),
    STOCK_RETOUR_DECLARER("Déclarer ses invendus de fin de tournée"),
    STOCK_RETOUR_GERER("Compter les invendus déclarés"),
    STOCK_NIVEAU_CONSULTER("Consulter les niveaux de stock"),
    STOCK_MOUVEMENT_CONSULTER("Consulter les mouvements de stock"),
    STOCK_ALERTE_GERER("Gérer les alertes de stock"),
    STOCK_INVENTAIRE_GERER("Gérer les inventaires"),
    STOCK_SEUIL_GERER("Gérer les seuils de stock"),
    STOCK_RAPPORT_CONSULTER("Consulter les rapports et tableaux de bord stock"),

    // ── Production ─────────────────────────────────────────────────────────
    PRODUCTION_PPH_GERER("Gérer les plans de production hebdomadaires"),
    PRODUCTION_OF_CREER("Créer un ordre de fabrication"),
    PRODUCTION_OF_VALIDER("Valider un ordre de fabrication"),
    PRODUCTION_OF_CONSULTER("Consulter les ordres de fabrication"),
    PRODUCTION_LOT_GERER("Gérer les lots de production"),
    PRODUCTION_LOT_VALIDER_STOCK("Valider un lot en stock"),
    PRODUCTION_EXPRESSION_BESOIN_GERER("Gérer les expressions de besoin"),
    PRODUCTION_FICHE_PRODUIT_GERER("Gérer les fiches de production"),
    PRODUCTION_AFFECTATION_GERER("Gérer les affectations journalières"),
    PRODUCTION_BROYAGE_GERER("Gérer les séances de broyage"),
    PRODUCTION_DOSAGE_GERER("Gérer les séances de dosage"),
    PRODUCTION_REGISTRE_CONSULTER("Consulter les registres journaliers"),
    PRODUCTION_FORMULE_GERER("Gérer les formules de production et compositions d'emballage"),

    // ── Commercial ─────────────────────────────────────────────────────────
    COMMERCIAL_VENTE_CREER("Créer une vente"),
    COMMERCIAL_VENTE_CONSULTER("Consulter les ventes"),
    COMMERCIAL_VENTE_FINALISER("Finaliser une vente terrain"),
    COMMERCIAL_VENTE_VALIDER("Valider une vente"),
    COMMERCIAL_PRECOMMANDE_GERER("Gérer les précommandes"),
    COMMERCIAL_PRECOMMANDE_ARBITRER("Arbitrer les quantités d'une précommande"),
    COMPTABILITE_BAREME_PRIME_GERER("Définir le barème de calcul des primes"),
    COMPTABILITE_PARAMETRAGE_CAISSE_GERER("Définir les seuils de caisse et de versement"),
    COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER("Définir les jours ouvrés des objectifs"),
    COMMERCIAL_REGLES_AVARIE_GERER("Définir les règles de déclaration d'avarie"),
    // Ouvrir sa journée est l'acte du commercial, symétrique de la déclaration
    // de fin de tournée. L'endpoint exigeait STOCK_DOTATION_GERER — la
    // permission du dépôt — que le commercial ne détient pas : il ne pouvait
    // donc pas ouvrir la journée que l'écran lui demandait d'ouvrir.
    STOCK_SESSION_OUVRIR("Ouvrir sa journée de tournée"),
    STOCK_SESSION_DECLARER_FIN_TOURNEE("Déclarer la fin de sa tournée"),
    STOCK_SESSION_VALIDER_SECRETAIRE("Rapprocher et valider une session (secrétaire)"),
    STOCK_SESSION_VALIDER_COMPTABLE("Valider une session (comptable)"),
    COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE("Valider une précommande (secrétaire)"),
    COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE("Valider une précommande (comptable)"),
    COMMERCIAL_COMMANDE_DISTANCE_GERER("Gérer les commandes à distance"),
    COMMERCIAL_FEUILLE_ROUTE_GERER("Gérer les feuilles de route"),
    COMMERCIAL_CARBURANT_GERER("Gérer les demandes de carburant"),
    COMMERCIAL_RECOUVREMENT_GERER("Gérer les recouvrements"),
    COMMERCIAL_FACTURE_AVOIR_GERER("Gérer les factures d'avoir"),
    COMMERCIAL_PRIX_EXCEPTIONNEL_GERER("Gérer les prix exceptionnels"),
    COMMERCIAL_CLIENT_GERER("Gérer les clients"),
    COMMERCIAL_GRILLE_TARIFAIRE_GERER("Gérer la grille tarifaire"),
    COMMERCIAL_ZONE_VENTE_GERER("Gérer les zones de vente"),
    COMMERCIAL_VEHICULE_GERER("Gérer les véhicules"),
    COMMERCIAL_TERMINAL_GERER("Gérer les terminaux de vente"),

    // ── Comptabilité ───────────────────────────────────────────────────────
    COMPTABILITE_FACTURE_GERER("Gérer les factures"),
    COMPTABILITE_CAISSE_GERER("Gérer le journal de caisse"),
    COMPTABILITE_DECAISSEMENT_PROPOSER("Proposer un décaissement"),
    COMPTABILITE_DECAISSEMENT_VALIDER_CG("Valider un décaissement (CG)"),
    COMPTABILITE_DECAISSEMENT_VALIDER_DG("Valider un décaissement (DG)"),
    COMPTABILITE_DECAISSEMENT_EXECUTER("Exécuter un décaissement"),
    COMPTABILITE_OBJECTIF_GERER("Gérer les objectifs commerciaux"),
    COMPTABILITE_PRIME_GERER("Gérer les primes"),
    COMPTABILITE_VERSEMENT_GERER("Gérer les versements"),
    COMPTABILITE_VERSEMENT_VALIDER("Valider un versement"),
    COMPTABILITE_CAISSE_CONSULTER("Consulter le journal de caisse"),
    COMPTABILITE_DASHBOARD_CONSULTER("Consulter le dashboard comptabilité"),
    COMPTABILITE_DECAISSEMENT_CONSULTER("Consulter les décaissements"),
    COMPTABILITE_FACTURE_CONSULTER("Consulter les factures"),
    COMPTABILITE_OBJECTIF_CONSULTER("Consulter les objectifs commerciaux"),
    COMPTABILITE_PRIME_CONSULTER("Consulter les primes commerciales"),
    COMPTABILITE_RAPPORT_CONSULTER("Consulter les rapports comptables"),
    COMPTABILITE_VERSEMENT_CONSULTER("Consulter les versements"),

    // ── Commercialisation ──────────────────────────────────────────────────
    COMMERCIALISATION_PROMOTION_GERER("Gérer les promotions"),

    // ── Contrôle de Gestion ────────────────────────────────────────────────
    CONTROLE_DASHBOARD_CONSULTER("Consulter le dashboard contrôle de gestion"),

    // ── Pilotage DG ────────────────────────────────────────────────────────
    DG_DASHBOARD_CONSULTER("Consulter les tableaux de bord DG"),
    DG_PARAMETRES_GERER("Modifier les paramètres système (DG)"),
    DG_VALIDATION_RESOUDRE("Résoudre une validation en attente (DG)"),

    // ── Intelligence Artificielle ──────────────────────────────────────────
    IA_ASSISTANT_UTILISER("Utiliser l'assistant IA"),

    // ── RP & Marketing ─────────────────────────────────────────────────────
    RP_DASHBOARD_CONSULTER("Consulter le dashboard RP"),

    // ── Secrétariat ────────────────────────────────────────────────────────
    SECRETARIAT_DASHBOARD_CONSULTER("Consulter le dashboard secrétariat"),

    // ── Synchronisation Offline ────────────────────────────────────────────
    SYNC_PUSH_PULL("Effectuer une synchronisation offline (push/pull)"),
    SYNC_HISTORIQUE_CONSULTER("Consulter l'historique des synchronisations"),

    // ── Auth / Administration ──────────────────────────────────────────────
    AUTH_USER_CREER("Créer un utilisateur"),
    AUTH_USER_MODIFIER("Modifier un utilisateur"),
    AUTH_USER_CONSULTER("Consulter les utilisateurs"),
    AUTH_USER_DESACTIVER("Désactiver/réactiver un utilisateur"),
    AUTH_USER_DEBLOQUER("Débloquer un compte utilisateur"),
    AUTH_USER_CHANGER_ROLE("Changer le rôle d'un utilisateur"),
    AUTH_USER_RESET_PASSWORD("Réinitialiser un mot de passe"),
    AUTH_PERMISSION_GERER("Gérer les permissions individuelles"),
    AUTH_PERMISSION_CONSULTER("Consulter la matrice des permissions"),

    // ── RH ─────────────────────────────────────────────────────────────────
    RH_EMPLOYE_GERER("Gérer les employés"),
    RH_EMPLOYE_CONSULTER("Consulter les employés");

    private final String libelle;

    Permission(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
