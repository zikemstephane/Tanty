# AUDIT UI/UX — TANTY Digital ERP (Flutter)

> **Légende :**  
> - **Fait** = Corrections appliquées et vérifiées (compilation propre)  
> - **En cours** = Corrections partielles ou blocage identifié  
> - **À faire** = Non traité  

---

## Module VENTES

### Pages

| # | Écran | Statut | Corrections appliquées |
|---|-------|--------|------------------------|
| 1 | `ventes_list_page.dart` | **Fait** | Row filtre : `Expanded`/`Flexible` + `ellipsis` sur les textes « FILTRER PAR PÉRIODE » et résumé. Chips de période enveloppés dans `SingleChildScrollView` horizontal (anti-overflow). Bouton « clear » ajouté au champ de recherche. Colonne montant du ticket enveloppée dans `Flexible` avec `ellipsis`. Espacements migrés vers `AppSpacing`. |
| 2 | `vente_detail_page.dart` | **Fait** | `maxLines: 1` + `overflow: TextOverflow.ellipsis` ajoutés sur nom client, téléphone/zone, type de transaction, date, articles (désignation + quantité), montant ligne, signature. Textes dynamiques enveloppés dans `Expanded`/`Flexible` pour prévenir les débordements sur petits écrans (~360dp). Espacements migrés vers `AppSpacing`. |
| 3 | `vente_step1_client_page.dart` | **Fait** | État vide « Aucun client trouvé » : remplacement du `Text` brut par le composant partagé `EmptyState`. Titre « Facturation · Étape 1/3 » : `ellipsis` + `maxLines`. Espacements du bottom sheet « Nouveau client » et de la barre de recherche migrés vers `AppSpacing`. Bouton retour : padding `AppSpacing.sm` (≈ 32dp touch zone). |
| 4 | `vente_step2_articles_page.dart` | **Fait** | État vide gamme : ajout de `EmptyState` quand aucune article dans la catégorie. Bannière client : `ellipsis` sur nom et segment/téléphone. Résumé panier : `Flexible` + `ellipsis` sur le total. Espacements migrés vers `AppSpacing`. Bouton retour : padding `AppSpacing.sm`. |
| 5 | `vente_step3_confirm_page.dart` | **Fait** | `ellipsis` + `maxLines` sur nom client, téléphone/zone, type de transaction, code article, désignation, quantité. Textes dynamiques enveloppés dans `Flexible` pour les Row. Espacements migrés vers `AppSpacing`. Bouton retour : padding `AppSpacing.sm`. |
| 6 | `vente_success_page.dart` | **Fait** | Aucune correction nécessaire — utilise déjà le composant partagé `SuccessScreen`. |

### Widgets

| # | Widget | Statut | Corrections appliquées |
|---|--------|--------|------------------------|
| 7 | `article_row.dart` | **Fait** | `ElevatedButton` remplacé par `TantyButton` (composant partagé). `ellipsis` sur stock, désignation, titre bottom sheet. `IconButton` +/- : ajout de `tooltip` pour l'accessibilité. Espacements migrés vers `AppSpacing`. |
| 8 | `client_list_item.dart` | **Fait** | Colonne dette enveloppée dans `Flexible` avec `ellipsis` sur montant et « Crédit bloqué ». Espacements migrés vers `AppSpacing`. |
| 9 | `signature_pad_widget.dart` | **Fait** | Bouton « Effacer » : `minimumSize: Size(44, 44)` pour respecter la zone tactile minimum. Espacements migrés vers `AppSpacing`. |

---

## Autres Modules (à traiter)

### Module STOCK

| # | Écran | Statut | Corrections appliquées |
|---|-------|--------|------------------------|
| 1 | `stock_page.dart` | **Fait** | `EmptyState` ajouté pour stock vide. `ellipsis`/`maxLines` sur code/gamme, valorisation, ratio stock. `Flexible` sur valorisation et badge ratio. Espacements migrés vers `AppSpacing`. |
| 2 | `stock_dashboard_page.dart` | **Fait** | `ElevatedButton` → `TantyButton` (Ajustement/Transfert/Confirmer). `TextStyle` brut → `AppTextStyles`. `EmptyState` pour vide + erreur avec réessayer. `ellipsis`/`maxLines` sur alertes, niveaux, dropdowns. `Expanded`/`Flexible` sur Row. Espacements migrés vers `AppSpacing`. |
| 3 | `retours_page.dart` | **Fait** | `ellipsis` sur header (titre + vérificateur). Espacements migrés vers `AppSpacing`. |
| 4 | `avarie_form_page.dart` | **Fait** | `TextButton` → `TantyButton` (Capturer/Reprendre). `ellipsis` sur dropdowns articles. Espacements migrés vers `AppSpacing`. |

### Module COMPTABILITÉ

| # | Écran | Statut | Corrections appliquées |
|---|-------|--------|------------------------|
| 1 | `caisse_page.dart` | **Fait** | `OutlinedButton`/`ElevatedButton` → `TantyButton` (Ouvrir/Entrée/Sortie + formulaires). `_emptyState` custom → `EmptyState` partagé. État erreur : `EmptyState` avec bouton « Réessayer ». `withOpacity` → `withValues(alpha:)`. `TextStyle` brut → `AppTextStyles`. `Expanded`/`Flexible` + `ellipsis` sur journal date, statut, soldes. Espacements migrés vers `AppSpacing`. |
| 2 | `decaissements_page.dart` | **Fait** | `ElevatedButton`/`OutlinedButton` → `TantyButton` (Approuver/Rejeter/Exécuter). `_emptyState` custom → `EmptyState` partagé. État erreur : `EmptyState` avec bouton « Réessayer ». `withOpacity` → `withValues(alpha:)`. `TextStyle` brut → `AppTextStyles`. `Expanded`/`Flexible` + `ellipsis` sur bénéficiaire, statut, montant, motif. Espacements migrés vers `AppSpacing`. |
| 3 | `objectifs_commerciaux_page.dart` | **Fait** | `_emptyState` custom → `EmptyState` partagé. État erreur : `EmptyState` avec bouton « Réessayer ». `TextStyle` brut → `AppTextStyles`. `Expanded`/`Flexible` + `ellipsis` sur matricule, période, cible, réalisé. Espacements migrés vers `AppSpacing`. |
| 4 | `versements_page.dart` | **Fait** | `ElevatedButton`/`TextButton` → `TantyButton` (Annuler/Enregistrer). `_emptyState` custom → `EmptyState` partagé. État erreur : `EmptyState` avec bouton « Réessayer ». `withOpacity` → `withValues(alpha:)`. `TextStyle` brut → `AppTextStyles`. `Expanded`/`Flexible` + `ellipsis` sur montant, mode, date, statut. Espacements migrés vers `AppSpacing`. |

### Module PERFORMANCE

| # | Écran | Statut | Corrections appliquées |
|---|-------|--------|------------------------|
| 1 | `performances_page.dart` | **Fait** | `EmptyState` pour données indisponibles (remplace `Text` brut). `ellipsis`/`maxLines` sur nom commercial, classement, stock, CA, objectif, désignation, gamme, libellés jour, prime, gammes bloquantes. `Flexible` sur badge classement, badge stock, montant CA, libellés Row. Espacements migrés vers `AppSpacing` (toutes sections : banner, cards, chart, prime, leaderboard). |
| 2 | `gamme_progress_row.dart` | **Fait** | `ellipsis`/`maxLines` sur nom gamme, tier label, pourcentage. `Flexible` sur Row gauche et pourcentage. Espacements migrés vers `AppSpacing`. |
| 3 | `rank_list_item.dart` | **Fait** | `ellipsis`/`maxLines` sur initiales avatar, nom commercial, pourcentage. `Flexible` sur badge pourcentage. Espacements migrés vers `AppSpacing`. |

### Module PRECOMMANDE

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `validation_precommande_page.dart` | À faire | — |
| 2 | `stepper_status.dart` | À faire | — |

### Module PRODUCTION

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `production_pph_page.dart` | À faire | — |

### Module AUDIT

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `audit_journal_page.dart` | À faire | — |

### Module FEUILLE DE ROUTE

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `feuille_route_page.dart` | À faire | — |

### Module DG

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `dg_dashboard_page.dart` | À faire | — |
| 2 | `dg_users_page.dart` | À faire | — |
| 3 | `reporting_dg_page.dart` | À faire | — |

### Module NOTIFICATIONS

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `notifications_page.dart` | À faire | — |

### Module DASHBOARD

| # | Écran | Statut | Corrections appliquées |
|---|-------|--------|------------------------|
| 1 | `dashboard_page.dart` | **Fait** | `EmptyState` pour activités vides (remplace `Text` brut). `TextStyle` brut → `AppTextStyles` (badge notification, bottom sheet ListTiles). `ellipsis`/`maxLines` sur date, userName, zoneName, titres sections, stat titles/values, client nom, typeVente, montant, objectif. `Flexible` sur montant activité et libellés jauge. `Expanded` sur header Column. Espacements migrés vers `AppSpacing` (header, hero card, stats card, grid, activités, bottom sheet, quick action tile). |

### Module AUTH

| # | Écran | Statut | Notes |
|---|-------|--------|-------|
| 1 | `login_page.dart` | À faire | — |

---

## Résumé de compilation

| Métrique | Valeur |
|----------|--------|
| Erreurs | **0** |
| Warnings | **0** |
| Info (lints) | 131 (`prefer_const_constructors`, etc.) |

---

## Tokens utilisés

- `AppSpacing` : `xs=4, sm=8, md=12, base=16, lg=20, xl=24, xxl=32, huge=40`
- `AppColors` : palette centralisée (getters non-const)
- `AppRadii` : `brXs, brSm, brMd, brLg, brButton, brCard`
- `AppTextStyles` : `h1, h2, h3, h4, bodyL, bodyM, bodyS, caption, labelUp, amount, button`
- Composants partagés : `TantyButton, TantyCard, TantyTextField, TantyHeader, TantyLoader, EmptyState, StatusBadge, SegmentBadge, AmountDisplay, ValidationWidget, SuccessScreen, UnsavedChangesSheet, FormSummaryDialog`
