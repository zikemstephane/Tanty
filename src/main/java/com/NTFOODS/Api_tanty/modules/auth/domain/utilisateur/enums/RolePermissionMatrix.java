package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Matrice statique — Permissions de base par rôle.
 *
 * Définit quelles permissions chaque rôle possède par défaut.
 * Des overrides individuels peuvent être appliqués en DB
 * (ajout ou retrait de permissions pour un utilisateur spécifique).
 *
 * ADMIN possède toutes les permissions (super-administrateur).
 */
public final class RolePermissionMatrix {

    private static final Map<Role, Set<Permission>> MATRIX = Map.ofEntries(
        Map.entry(Role.ADMIN, EnumSet.allOf(Permission.class)),

        Map.entry(Role.DIRECTEUR_GENERAL, EnumSet.of(
            // Stock — consultation et gestion complète
            Permission.STOCK_PRODUIT_CREER, Permission.STOCK_PRODUIT_MODIFIER,
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_PRODUIT_DESACTIVER,
            Permission.STOCK_CONDITIONNEMENT_GERER, Permission.STOCK_FOURNISSEUR_GERER,
            Permission.STOCK_ENTREPOT_GERER, Permission.STOCK_BON_COMMANDE_CREER,
            Permission.STOCK_BON_COMMANDE_VALIDER, Permission.STOCK_RECEPTION_GERER,
            Permission.STOCK_TRANSFERT_GERER, Permission.STOCK_DOTATION_GERER,
            Permission.STOCK_LOT_GERER, Permission.STOCK_SESSION_OUVRIR,
            Permission.STOCK_RETOUR_GERER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_MOUVEMENT_CONSULTER, Permission.STOCK_ALERTE_GERER,
            Permission.STOCK_INVENTAIRE_GERER, Permission.STOCK_SEUIL_GERER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Production — gestion complète
            Permission.PRODUCTION_PPH_GERER, Permission.PRODUCTION_OF_CREER,
            Permission.PRODUCTION_OF_VALIDER, Permission.PRODUCTION_OF_CONSULTER,
            Permission.PRODUCTION_LOT_GERER, Permission.PRODUCTION_LOT_VALIDER_STOCK,
            Permission.PRODUCTION_EXPRESSION_BESOIN_GERER,
            Permission.PRODUCTION_FICHE_PRODUIT_GERER,
            Permission.PRODUCTION_AFFECTATION_GERER,
            Permission.PRODUCTION_BROYAGE_GERER, Permission.PRODUCTION_DOSAGE_GERER,
            Permission.PRODUCTION_REGISTRE_CONSULTER,
            // Commercial — gestion complète
            Permission.COMMERCIAL_VENTE_CREER, Permission.COMMERCIAL_VENTE_CONSULTER,
            Permission.COMMERCIAL_VENTE_FINALISER,
            Permission.COMMERCIAL_VENTE_VALIDER, Permission.COMMERCIAL_PRECOMMANDE_GERER,
            Permission.COMMERCIAL_PRECOMMANDE_ARBITRER,
            Permission.COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE,
            Permission.COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE,
            Permission.COMMERCIAL_COMMANDE_DISTANCE_GERER,
            Permission.COMMERCIAL_FEUILLE_ROUTE_GERER, Permission.COMMERCIAL_CARBURANT_GERER,
            Permission.COMMERCIAL_RECOUVREMENT_GERER, Permission.COMMERCIAL_FACTURE_AVOIR_GERER,
            Permission.COMMERCIAL_PRIX_EXCEPTIONNEL_GERER, Permission.COMMERCIAL_CLIENT_GERER,
            Permission.COMMERCIAL_GRILLE_TARIFAIRE_GERER,
            Permission.COMMERCIAL_ZONE_VENTE_GERER, Permission.COMMERCIAL_VEHICULE_GERER,
            Permission.COMMERCIAL_TERMINAL_GERER,
            // Comptabilité — validation et gestion complète
            Permission.COMPTABILITE_FACTURE_GERER, Permission.COMPTABILITE_CAISSE_GERER,
            Permission.COMPTABILITE_DECAISSEMENT_PROPOSER,
            Permission.COMPTABILITE_DECAISSEMENT_VALIDER_CG,
            Permission.COMPTABILITE_DECAISSEMENT_VALIDER_DG,
            Permission.COMPTABILITE_DECAISSEMENT_EXECUTER,
            Permission.COMPTABILITE_OBJECTIF_GERER, Permission.COMPTABILITE_PRIME_GERER,
            Permission.COMPTABILITE_BAREME_PRIME_GERER,
            Permission.COMPTABILITE_PARAMETRAGE_CAISSE_GERER,
            Permission.COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER,
            Permission.COMMERCIAL_REGLES_AVARIE_GERER,
            Permission.COMPTABILITE_VERSEMENT_GERER, Permission.COMPTABILITE_VERSEMENT_VALIDER,
            Permission.COMPTABILITE_CAISSE_CONSULTER, Permission.COMPTABILITE_DASHBOARD_CONSULTER,
            Permission.COMPTABILITE_DECAISSEMENT_CONSULTER, Permission.COMPTABILITE_FACTURE_CONSULTER,
            Permission.COMPTABILITE_OBJECTIF_CONSULTER, Permission.COMPTABILITE_PRIME_CONSULTER,
            Permission.COMPTABILITE_RAPPORT_CONSULTER, Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            // Commercialisation
            Permission.COMMERCIALISATION_PROMOTION_GERER,
            // Contrôle de Gestion / DG / RP / Secrétariat / IA / Sync
            Permission.CONTROLE_DASHBOARD_CONSULTER, Permission.DG_DASHBOARD_CONSULTER,
            Permission.DG_PARAMETRES_GERER, Permission.DG_VALIDATION_RESOUDRE,
            Permission.IA_ASSISTANT_UTILISER, Permission.RP_DASHBOARD_CONSULTER,
            Permission.SECRETARIAT_DASHBOARD_CONSULTER, Permission.SYNC_PUSH_PULL,
            Permission.SYNC_HISTORIQUE_CONSULTER,
            // Auth — gestion utilisateurs
            Permission.AUTH_USER_CREER, Permission.AUTH_USER_MODIFIER,
            Permission.AUTH_USER_CONSULTER, Permission.AUTH_USER_DESACTIVER,
            Permission.AUTH_USER_DEBLOQUER, Permission.AUTH_USER_CHANGER_ROLE,
            Permission.AUTH_USER_RESET_PASSWORD, Permission.AUTH_PERMISSION_GERER,
            Permission.AUTH_PERMISSION_CONSULTER,
            // RH
            Permission.RH_EMPLOYE_GERER, Permission.RH_EMPLOYE_CONSULTER
        )),

        Map.entry(Role.DIRECTEUR_COMMERCIAL, EnumSet.of(
            // Stock — consultation
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Commercial — gestion complète
            Permission.COMMERCIAL_VENTE_CREER, Permission.COMMERCIAL_VENTE_CONSULTER,
            Permission.COMMERCIAL_VENTE_FINALISER,
            Permission.COMMERCIAL_VENTE_VALIDER, Permission.COMMERCIAL_PRECOMMANDE_GERER,
            Permission.COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE,
            Permission.COMMERCIAL_COMMANDE_DISTANCE_GERER,
            Permission.COMMERCIAL_FEUILLE_ROUTE_GERER, Permission.COMMERCIAL_CARBURANT_GERER,
            Permission.COMMERCIAL_RECOUVREMENT_GERER, Permission.COMMERCIAL_FACTURE_AVOIR_GERER,
            Permission.COMMERCIAL_PRIX_EXCEPTIONNEL_GERER, Permission.COMMERCIAL_CLIENT_GERER,
            Permission.COMMERCIAL_GRILLE_TARIFAIRE_GERER,
            Permission.COMMERCIAL_ZONE_VENTE_GERER, Permission.COMMERCIAL_VEHICULE_GERER,
            Permission.COMMERCIAL_TERMINAL_GERER,
            // Commercialisation
            Permission.COMMERCIALISATION_PROMOTION_GERER,
            // Comptabilité — objectifs et primes
            Permission.COMPTABILITE_OBJECTIF_GERER, Permission.COMPTABILITE_PRIME_GERER,
            Permission.COMPTABILITE_VERSEMENT_GERER,
            // Corollaire de COMPTABILITE_VERSEMENT_GERER, juste au-dessus : sans
            // la lecture, il administrait des versements qu'aucune liste ne
            // pouvait lui montrer.
            Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            // Auth — consultation
            Permission.AUTH_USER_CONSULTER, Permission.AUTH_PERMISSION_CONSULTER,
            // IA
            Permission.IA_ASSISTANT_UTILISER
        )),

        Map.entry(Role.GESTIONNAIRE_STOCK, EnumSet.of(
            // 2ᵉ validation, mais des seules réceptions de produits finis : le
            // domaine la lui réserve pour cette catégorie et la lui refuse pour
            // les deux autres. La permission ouvre la porte, le domaine trie.
            Permission.STOCK_RECEPTION_VALIDER_ETAPE2,
            // Stock — gestion complète
            Permission.STOCK_PRODUIT_CREER, Permission.STOCK_PRODUIT_MODIFIER,
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_PRODUIT_DESACTIVER,
            Permission.STOCK_CONDITIONNEMENT_GERER, Permission.STOCK_FOURNISSEUR_GERER,
            Permission.STOCK_ENTREPOT_GERER, Permission.STOCK_BON_COMMANDE_CREER,
            Permission.STOCK_BON_COMMANDE_VALIDER, Permission.STOCK_RECEPTION_GERER,
            Permission.STOCK_TRANSFERT_GERER, Permission.STOCK_DOTATION_GERER,
            Permission.STOCK_LOT_GERER, Permission.STOCK_SESSION_OUVRIR,
            Permission.STOCK_RETOUR_GERER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_MOUVEMENT_CONSULTER, Permission.STOCK_ALERTE_GERER,
            Permission.STOCK_INVENTAIRE_GERER, Permission.STOCK_SEUIL_GERER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Production — lots et expressions de besoin
            Permission.PRODUCTION_LOT_VALIDER_STOCK, Permission.PRODUCTION_EXPRESSION_BESOIN_GERER,
            Permission.PRODUCTION_OF_CONSULTER,
            // Commercial — transactions non marchandes (dons/avaries/dotations)
            Permission.COMMERCIAL_VENTE_CREER,
            // Commercial — arbitrage des quantités de précommande avant livraison,
            // au vu de la disponibilité réelle du stock
            Permission.COMMERCIAL_PRECOMMANDE_ARBITRER,
            // C'est le gestionnaire de stock qui instruit les avaries : il fixe donc
            // le seuil à partir duquel une preuve photographique est exigée.
            Permission.COMMERCIAL_REGLES_AVARIE_GERER,
            // IA
            Permission.IA_ASSISTANT_UTILISER
        )),

        Map.entry(Role.CHEF_PRODUCTION, EnumSet.of(
            // Production — gestion complète
            Permission.PRODUCTION_PPH_GERER, Permission.PRODUCTION_FORMULE_GERER, Permission.PRODUCTION_OF_CREER,
            Permission.PRODUCTION_OF_VALIDER, Permission.PRODUCTION_OF_CONSULTER,
            Permission.PRODUCTION_LOT_GERER, Permission.PRODUCTION_LOT_VALIDER_STOCK,
            Permission.PRODUCTION_EXPRESSION_BESOIN_GERER,
            Permission.PRODUCTION_FICHE_PRODUIT_GERER,
            Permission.PRODUCTION_AFFECTATION_GERER,
            Permission.PRODUCTION_BROYAGE_GERER, Permission.PRODUCTION_DOSAGE_GERER,
            Permission.PRODUCTION_REGISTRE_CONSULTER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Stock — consultation, et Bons de Commande fournisseurs : BonCommandeSpecification
            // l'autorise déjà à les créer et à les valider (seul rôle habilité avec ADMIN),
            // mais ces deux permissions manquaient ici, bloquant l'écran dédié en 403.
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_MOUVEMENT_CONSULTER,
            Permission.STOCK_BON_COMMANDE_CREER, Permission.STOCK_BON_COMMANDE_VALIDER,
            // Commercial — signature demandes carburant
            Permission.COMMERCIAL_CARBURANT_GERER,
            // IA
            Permission.IA_ASSISTANT_UTILISER
        )),

        Map.entry(Role.RESPONSABLE_SALLE, EnumSet.of(
            // Production — affectations et registres
            Permission.PRODUCTION_AFFECTATION_GERER,
            Permission.PRODUCTION_REGISTRE_CONSULTER,
            Permission.PRODUCTION_OF_CONSULTER,
            Permission.PRODUCTION_PPH_GERER,
            Permission.PRODUCTION_FORMULE_GERER,
            Permission.PRODUCTION_DOSAGE_GERER,
            Permission.PRODUCTION_LOT_GERER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Écrans propres à ce rôle, jusqu'ici bloqués en 403 dès le GET faute
            // de permission (pas de variante lecture seule pour ces deux-là) :
            // "Expressions de Besoin" et "Fiche Info Produit" sont dans son menu.
            Permission.PRODUCTION_EXPRESSION_BESOIN_GERER,
            Permission.PRODUCTION_FICHE_PRODUIT_GERER
        )),

        Map.entry(Role.CHEF_MACHINISTE, EnumSet.of(
            // Production — broyage, dosage, affectations
            Permission.PRODUCTION_BROYAGE_GERER, Permission.PRODUCTION_DOSAGE_GERER,
            Permission.PRODUCTION_AFFECTATION_GERER, Permission.PRODUCTION_REGISTRE_CONSULTER,
            Permission.PRODUCTION_OF_CONSULTER
        )),

        // Exécutant : n'accède pas au système, seul le Chef Machiniste s'y connecte.
        Map.entry(Role.MACHINISTE, EnumSet.noneOf(Permission.class)),

        Map.entry(Role.AGENT_DOSEUR, EnumSet.of(
            Permission.PRODUCTION_DOSAGE_GERER, Permission.PRODUCTION_REGISTRE_CONSULTER,
            Permission.PRODUCTION_OF_CONSULTER
        )),

        // Exécutant : n'accède pas au système.
        Map.entry(Role.AGENT_PRODUCTION, EnumSet.noneOf(Permission.class)),

        Map.entry(Role.CONTROLEUR_GENERAL, EnumSet.of(
            // 2ᵉ validation des réceptions de consommables — le pendant de ce que
            // le Comptable exerce sur les matières premières.
            Permission.STOCK_RECEPTION_VALIDER_ETAPE2,
            // Consultation transversale
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_MOUVEMENT_CONSULTER, Permission.STOCK_ALERTE_GERER,
            Permission.STOCK_RAPPORT_CONSULTER, Permission.PRODUCTION_OF_CONSULTER, Permission.PRODUCTION_REGISTRE_CONSULTER,
            Permission.COMMERCIAL_VENTE_CONSULTER, Permission.COMMERCIAL_RECOUVREMENT_GERER,
            Permission.COMMERCIAL_CARBURANT_GERER,
            Permission.COMPTABILITE_FACTURE_GERER, Permission.COMPTABILITE_CAISSE_GERER,
            Permission.COMPTABILITE_VERSEMENT_GERER, Permission.COMPTABILITE_PRIME_GERER,
            Permission.COMPTABILITE_DECAISSEMENT_PROPOSER,
            Permission.COMPTABILITE_CAISSE_CONSULTER, Permission.COMPTABILITE_DASHBOARD_CONSULTER,
            Permission.COMPTABILITE_DECAISSEMENT_CONSULTER, Permission.COMPTABILITE_FACTURE_CONSULTER,
            Permission.COMPTABILITE_OBJECTIF_CONSULTER, Permission.COMPTABILITE_PRIME_CONSULTER,
            Permission.COMPTABILITE_RAPPORT_CONSULTER, Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            Permission.AUTH_USER_CONSULTER, Permission.AUTH_PERMISSION_CONSULTER,
            Permission.RH_EMPLOYE_CONSULTER,
            // Contrôle de Gestion / IA
            Permission.CONTROLE_DASHBOARD_CONSULTER, Permission.IA_ASSISTANT_UTILISER
        )),

        Map.entry(Role.COMMERCIAL, EnumSet.of(
            // Commercial — terrain
            // COMMERCIAL_VENTE_FINALISER : clôture opérationnelle de sa propre vente sur le
            // terrain (Vente.finaliser()). Distincte de COMMERCIAL_VENTE_VALIDER, qui reste
            // réservée au contrôle hiérarchique et n'est pas accordée au commercial.
            Permission.COMMERCIAL_VENTE_CREER, Permission.COMMERCIAL_VENTE_CONSULTER,
            Permission.COMMERCIAL_VENTE_FINALISER,
            // Déclarer la fin de sa propre tournée. Volontairement distincte de
            // STOCK_DOTATION_GERER : le commercial n'administre pas le stock, il
            // signale que sa journée est terminée et la soumet au contrôle du siège.
            Permission.STOCK_SESSION_OUVRIR,
            Permission.STOCK_SESSION_DECLARER_FIN_TOURNEE,
            // Déclarer ses invendus. Cette permission ne produit aucun mouvement de
            // stock : elle enregistre une annonce, que le gestionnaire confronte
            // ensuite à son comptage. STOCK_RETOUR_GERER reste au dépôt.
            Permission.STOCK_RETOUR_DECLARER,
            // Soumettre sa dotation : le commercial demande ce qu'il emportera,
            // le dépôt décide. SoumettreDotationUseCase réserve d'ailleurs l'acte
            // au rôle COMMERCIAL — sans cette permission, personne ne pouvait le
            // faire : le commercial était arrêté par le garde, le gestionnaire
            // par le domaine.
            Permission.STOCK_DOTATION_SOUMETTRE,
            Permission.COMMERCIAL_PRECOMMANDE_GERER, Permission.COMMERCIAL_COMMANDE_DISTANCE_GERER,
            Permission.COMMERCIAL_FEUILLE_ROUTE_GERER, Permission.COMMERCIAL_CARBURANT_GERER,
            Permission.COMMERCIAL_RECOUVREMENT_GERER, Permission.COMMERCIAL_PRIX_EXCEPTIONNEL_GERER,
            Permission.COMMERCIAL_CLIENT_GERER,
            // Stock — consultation
            Permission.STOCK_PRODUIT_CONSULTER, Permission.STOCK_NIVEAU_CONSULTER,
            Permission.STOCK_RAPPORT_CONSULTER,
            // Comptabilité — consultation
            Permission.COMPTABILITE_CAISSE_CONSULTER, Permission.COMPTABILITE_DECAISSEMENT_CONSULTER,
            Permission.COMPTABILITE_OBJECTIF_CONSULTER, Permission.COMPTABILITE_PRIME_CONSULTER,
            Permission.COMPTABILITE_RAPPORT_CONSULTER, Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            // IA / Sync
            Permission.IA_ASSISTANT_UTILISER, Permission.SYNC_PUSH_PULL
        )),

        Map.entry(Role.SECRETAIRE, EnumSet.of(
            // Rapprochement de fin de tournée : la secrétaire contrôle la remise
            // d'espèces avant que le comptable ne clôture le cycle.
            Permission.STOCK_SESSION_VALIDER_SECRETAIRE,
            Permission.STOCK_NIVEAU_CONSULTER,
            // Commercial — validation précommandes
            Permission.COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE,
            Permission.COMMERCIAL_COMMANDE_DISTANCE_GERER,
            Permission.COMMERCIAL_VENTE_CONSULTER,
            Permission.COMMERCIAL_CLIENT_GERER,
            // Comptabilité — caisse et versements
            Permission.COMPTABILITE_CAISSE_GERER, Permission.COMPTABILITE_VERSEMENT_GERER,
            Permission.COMPTABILITE_VERSEMENT_VALIDER, Permission.COMPTABILITE_FACTURE_GERER,
            Permission.COMPTABILITE_DECAISSEMENT_EXECUTER,
            Permission.COMPTABILITE_CAISSE_CONSULTER, Permission.COMPTABILITE_FACTURE_CONSULTER,
            // Corollaire de COMPTABILITE_DECAISSEMENT_EXECUTER, juste au-dessus :
            // la secrétaire exécute le paiement à l'étape 3. Sans la lecture, elle
            // devait payer un décaissement qu'aucune liste ne pouvait lui montrer.
            // C'est la seule permission ajoutée ici, et elle n'ouvre rien d'autre
            // que la vue de ce qu'elle a déjà le pouvoir de régler.
            Permission.COMPTABILITE_DECAISSEMENT_CONSULTER,
            Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            // RH
            Permission.RH_EMPLOYE_CONSULTER,
            // Secrétariat
            Permission.SECRETARIAT_DASHBOARD_CONSULTER
        )),

        Map.entry(Role.COMPTABLE, EnumSet.of(
            // 2ᵉ validation des réceptions de matière première : le domaine la
            // lui réserve (Reception.verifierRoleEtape2), la permission le lui
            // permet enfin. Elle ne lui ouvre ni la création ni l'enregistrement
            // physique d'une réception, qui restent au dépôt.
            Permission.STOCK_RECEPTION_VALIDER_ETAPE2,
            // Comptabilité — gestion complète
            Permission.COMPTABILITE_FACTURE_GERER, Permission.COMPTABILITE_CAISSE_GERER,
            Permission.COMPTABILITE_DECAISSEMENT_PROPOSER,
            Permission.COMPTABILITE_DECAISSEMENT_VALIDER_CG,
            Permission.COMPTABILITE_OBJECTIF_GERER, Permission.COMPTABILITE_PRIME_GERER,
            Permission.COMPTABILITE_VERSEMENT_GERER, Permission.COMPTABILITE_VERSEMENT_VALIDER,
            // Paramétrages dont le comptable a la charge métier : les seuils de caisse
            // et de versement qu'il applique, et le découpage des objectifs qu'il fixe.
            Permission.COMPTABILITE_PARAMETRAGE_CAISSE_GERER,
            Permission.COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER,
            // Validation finale du cycle de fin de tournée, après la secrétaire.
            Permission.STOCK_SESSION_VALIDER_COMPTABLE,
            Permission.STOCK_NIVEAU_CONSULTER,
            // Commercial — validation précommandes
            Permission.COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE,
            Permission.COMMERCIAL_FACTURE_AVOIR_GERER,
            Permission.COMMERCIAL_VENTE_CONSULTER,
            Permission.COMMERCIAL_COMMANDE_DISTANCE_GERER,
            // Tarification : le comptable homologue les prix et accorde les
            // dérogations au même titre que le DG, le Directeur Commercial et
            // la Chargée RP.
            Permission.COMMERCIAL_GRILLE_TARIFAIRE_GERER,
            Permission.COMMERCIAL_PRIX_EXCEPTIONNEL_GERER,
            // Zones de vente : le découpage territorial conditionne les objectifs
            // qu'il fixe, il l'administre donc au même titre que la Chargée RP.
            Permission.COMMERCIAL_ZONE_VENTE_GERER,
            Permission.COMPTABILITE_CAISSE_CONSULTER, Permission.COMPTABILITE_DASHBOARD_CONSULTER,
            Permission.COMPTABILITE_DECAISSEMENT_CONSULTER, Permission.COMPTABILITE_FACTURE_CONSULTER,
            Permission.COMPTABILITE_OBJECTIF_CONSULTER, Permission.COMPTABILITE_PRIME_CONSULTER,
            Permission.COMPTABILITE_RAPPORT_CONSULTER, Permission.COMPTABILITE_VERSEMENT_CONSULTER,
            // Contrôle de Gestion / IA / Secrétariat
            Permission.CONTROLE_DASHBOARD_CONSULTER, Permission.IA_ASSISTANT_UTILISER,
            Permission.SECRETARIAT_DASHBOARD_CONSULTER
        )),

        // La Chargée RP tient le rôle fonctionnel de Directeur Commercial, poste
        // inexistant dans l'entreprise : elle pilote donc le volet commercial des
        // commerciaux — objectifs, primes et leurs règles de calcul.
        Map.entry(Role.CHARGEE_RP, EnumSet.of(
            // Commercialisation
            Permission.COMMERCIALISATION_PROMOTION_GERER,
            // Commercial — consultation
            Permission.COMMERCIAL_VENTE_CONSULTER, Permission.COMMERCIAL_CLIENT_GERER,
            // Tarification. La grille homologuée relève du pilotage commercial courant ;
            // le prix exceptionnel est une dérogation individuelle qu'elle peut
            // accorder au même titre que le Directeur Commercial et le Comptable.
            Permission.COMMERCIAL_GRILLE_TARIFAIRE_GERER,
            Permission.COMMERCIAL_PRIX_EXCEPTIONNEL_GERER,
            // Zones de vente : elle constate la sous-exploitation, elle la corrige.
            Permission.COMMERCIAL_ZONE_VENTE_GERER,
            // Pilotage commercial — objectifs, primes et barème
            Permission.COMPTABILITE_OBJECTIF_GERER, Permission.COMPTABILITE_OBJECTIF_CONSULTER,
            Permission.COMPTABILITE_PRIME_GERER, Permission.COMPTABILITE_PRIME_CONSULTER,
            Permission.COMPTABILITE_BAREME_PRIME_GERER,
            Permission.COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER,
            // RP
            Permission.RP_DASHBOARD_CONSULTER
        ))
    );

    private RolePermissionMatrix() {}

    /**
     * Retourne les permissions de base d'un rôle (non modifiable).
     */
    public static Set<Permission> permissionsDe(Role role) {
        return Collections.unmodifiableSet(
            MATRIX.getOrDefault(role, EnumSet.noneOf(Permission.class))
        );
    }

    /**
     * Vérifie si un rôle possède une permission de base.
     */
    public static boolean possede(Role role, Permission permission) {
        return permissionsDe(role).contains(permission);
    }

    /**
     * Retourne la matrice complète (non modifiable).
     */
    public static Map<Role, Set<Permission>> matriceComplete() {
        return Collections.unmodifiableMap(MATRIX);
    }
}
