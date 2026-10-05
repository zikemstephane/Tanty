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

        /**
         * Accepte aussi bien le libellé affiché (« TANTY BOUILLIE DE SOJA ») que
         * le nom de la constante (« TANTY_BOUILLIE_DE_SOJA »).
         *
         * Les deux circulent : le libellé vient des écrans et des imports, le nom
         * vient de l'API et de la base. N'en reconnaître qu'un seul faisait
         * échouer la création de produit selon la porte empruntée.
         */
        public static CategorieMetier depuisLibelle(String valeur) {

        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("La catégorie métier est obligatoire.");
        }

        String propre = valeur.trim();
        for (CategorieMetier c : values()) {
            if (c.libelle.equalsIgnoreCase(propre) || c.name().equalsIgnoreCase(propre)) {
                return c;
            }
        }

        throw new IllegalArgumentException(
            "Catégorie métier inconnue : " + valeur + ". Valeurs acceptées : "
                + java.util.Arrays.stream(values()).map(Enum::name).reduce((a, b) -> a + ", " + b).orElse("")
        );
    }
    }
