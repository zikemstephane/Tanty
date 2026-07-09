    package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums;

    /**
     * Catégorie métier d'un produit NTFOODS TANTY.
     *
     * Détermine le circuit et les règles de calcul du plan de production.
     */
    public enum CategorieMetier {

        TANTY_BOUILLIE_DE_SOJA("TANTY BOUILLIE DE SOJA"),
        TANTY_CHOCOLAT("TANTY CHOCOLAT"),
        INGREDIENTS("INGREDIENTS"),
        TANTY_A_GRIGNOTER("TANTY A GRIGNOTER");

        private final String libelle;

        CategorieMetier(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        public boolean estBouillie() {
            return this == TANTY_BOUILLIE_DE_SOJA;
        }

        public boolean estGrignoter() {
            return this == TANTY_A_GRIGNOTER;
        }

        public static CategorieMetier depuisLibelle(String valeur) {

        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("La catégorie métier est obligatoire.");
        }

        for (CategorieMetier c : values()) {
            if (c.libelle.equalsIgnoreCase(valeur.trim())) {
                return c;
            }
        }

        throw new IllegalArgumentException(
            "Catégorie métier inconnue : " + valeur
        );
    }
    }
