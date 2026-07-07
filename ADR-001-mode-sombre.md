# ADR-001 : Mode sombre Flutter (mobile-only)

**Statut :** Accepté
**Date : 2025-07-05

## Contexte

La charte graphique TANTY Digital (vert primaire #14532D, or #F6B60B, rouge #C22B2B, fond papier #FFFDF7) ne définit pas de mode sombre. L'application Angular (back-office desktop) n'en dispose pas.

Cependant, l'application Flutter (mobile, commerciaux terrain) implémente un mode sombre via `TantyColors.dark` et `ThemeCubit`.

## Décision

**Conserver le mode sombre comme feature mobile-only officielle.**

## Justification

1. **Usage terrain :** Les commerciaux travaillent tôt le matin, tard le soir, et en intérieur (magasins, entrepôts). Le mode sombre réduit la fatigue oculaire dans ces conditions.
2. **Standard mobile :** Le mode sombre est une attente UX standard sur mobile (iOS/Android). Son absence serait perçue comme un retard.
3. **Pas de contradiction charte :** La charte définit les couleurs de marque. Le mode sombre est une **variation thématique** qui préserve l'identité (vert primaire, or, rouge) avec des ajustements de luminosité.
4. **Indépendance plateforme :** Angular (desktop) et Flutter (mobile) ont des contextes d'usage différents. Une feature mobile-only n'a pas besoin d'être répliquée sur desktop.

## Implémentation

- `TantyColors.dark` : palette sombre officielle (déjà implémentée).
- `ThemeCubit` : gestion du `ThemeMode` (system/light/dark).
- `AppColors` : façade qui bascule automatiquement entre `TantyColors.light` et `TantyColors.dark`.
- `AppTheme.darkTheme` : `ThemeData` complet pour le mode sombre.
- Paramètres page : toggle pour basculer entre clair/sombre/système.

## Conséquences

- La charte graphique est étendue : mode clair (référence) + mode sombre (variation mobile).
- Angular reste en mode clair uniquement (cohérent avec son usage desktop).
- Tous les tokens (`AppColors`, `AppRadii`, `AppShadows`, `AppTextStyles`) sont compatibles avec les deux modes.
