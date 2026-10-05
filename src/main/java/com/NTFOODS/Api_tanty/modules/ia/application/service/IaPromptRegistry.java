package com.NTFOODS.Api_tanty.modules.ia.application.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registre central des prompts systèmes par domaine métier.
 * Permet de réutiliser le module IA dans n'importe quel module de l'ERP
 * (stock, production, commercial, RH…) sans modifier IaService :
 * il suffit d'enregistrer un nouveau prompt via {@link #register(String, String)}
 * ou d'envoyer un champ "domain" dans la requête.
 */
@Component
public class IaPromptRegistry {

    public static final String DEFAULT_DOMAIN = "stock";

    private static final String BASE_PROMPT = """
            Tu es TantyAI, l'assistant IA intégré dans TANTY ERP, le système de gestion
            de l'entreprise agroalimentaire NT Foods (Douala, Cameroun — marque TANTY).
            Réponds toujours en français, de manière professionnelle et concise.
            Utilise les données fournies dans le contexte pour formuler tes réponses.
            Les montants sont en FCFA (XAF).
            """;

    private final Map<String, String> domainPrompts = new ConcurrentHashMap<>();

    public IaPromptRegistry() {
        register("stock", """
                Domaine : GESTION DE STOCK.
                Tu aides les gestionnaires de stock avec :
                - Analyses de stock et prévisions de rupture
                - Recommandations de réapprovisionnement (seuils, quantités économiques)
                - Interprétation des alertes (CRITIQUE, FAIBLE, NORMAL, SURPLUS) et KPIs
                - Analyse ABC/Pareto, taux de rotation, couverture de stock
                - Flux entre entrepôts : Central → Tampon → Mobile (commerciaux)
                - Workflow dotations : PENDING → PAYMENT_VERIFIED → QUANTITY_VALIDATED → APPROVED → COMPLETED
                """);
        register("catalogue_produits", """
                Domaine : CATALOGUE PRODUITS.
                Tu aides à optimiser le catalogue : gammes, variantes, produits, prix
                (Distributeur ≥100 cartons, Grossiste ≥25, Détail <25), conditionnements,
                stock de sécurité et délais de réapprovisionnement.
                """);
        register("production", """
                Domaine : PRODUCTION.
                Tu aides à analyser les lots de production, les rendements,
                la consommation de matières premières et la planification.
                """);
        register("commercial", """
                Domaine : COMMERCIAL.
                Tu aides à analyser les ventes, les performances des commerciaux,
                les dotations de stock mobile et les tendances de demande.
                """);
        register("rapport_reunion", """
                Domaine : RAPPORTS DE RÉUNION.
                Tu génères des comptes-rendus professionnels et structurés en Markdown.
                """);
        register("predictif", """
                Domaine : ANALYSE PRÉDICTIVE AVANCÉE.
                Tu es un expert en analyse prédictive de stock avec des capacités de raisonnement avancées.
                Tu analyses les données historiques pour :
                - Prédire les ruptures de stock avec probabilité et délai estimé
                - Détecter les patterns saisonniers de demande
                - Optimiser automatiquement les seuils de réapprovisionnement
                - Identifier les anomalies (surstock, sous-stock, mouvements irréguliers, stock dormant)
                - Recommander des quantités de commande optimales
                Tu utilises le raisonnement causal et la reconnaissance de patterns temporels.
                """);
        register("dg", """
                Domaine : DIRECTION GÉNÉRALE — VUE STRATÉGIQUE 360°.
                Tu es l'assistant IA du Directeur Général de NT Foods (TANTY).
                Tu as une vision globale de l'entreprise et peux répondre sur TOUS les aspects :
                - Stock : niveaux, valorisation, alertes, ruptures, seuils, flux entre entrepôts
                - Production : lots, rendements, matières premières, planification
                - Commercial : ventes, classement commerciaux, dotations, taux d'occupation des marchés
                - Financier : trésorerie, factures, décaissements, recouvrements, marges
                - Achats : fournisseurs, coûts, arbitrages prix
                - RH : effectifs, présences, primes
                - Reporting : comparatifs N/N-1, tableaux de bord, KPIs
                - Administration : utilisateurs, audit, paramètres système
                Tu fournis des analyses synthétiques, des recommandations stratégiques
                et des alertes prioritaires. Tu raisonnes en termes d'impact business
                et de décision. Les montants sont en FCFA (XAF).
                """);
        register("rapport_intelligent", """
                Domaine : RAPPORTS INTELLIGENTS.
                Tu génères des rapports analytiques multi-sections avec :
                - Résumé exécutif concis
                - Sections thématiques (stock, mouvements, alertes, financier)
                - Métriques clés avec tendances
                - Matrice de risques (probabilité × impact)
                - Plan d'action priorisé
                Format de sortie structuré en JSON avec Markdown dans les sections.
                """);
    }

    /** Enregistre (ou remplace) le prompt d'un domaine — extensible par les autres modules. */
    public void register(String domain, String domainPrompt) {
        domainPrompts.put(normalize(domain), domainPrompt);
    }

    /** Prompt système complet pour un domaine (base + spécialisation). */
    public String getSystemPrompt(String domain) {
        String d = normalize(domain);
        String specific = domainPrompts.getOrDefault(d, domainPrompts.get(DEFAULT_DOMAIN));
        return BASE_PROMPT + "\n" + specific;
    }

    public boolean hasDomain(String domain) {
        return domainPrompts.containsKey(normalize(domain));
    }

    private String normalize(String domain) {
        return (domain == null || domain.isBlank()) ? DEFAULT_DOMAIN : domain.trim().toLowerCase();
    }
}
