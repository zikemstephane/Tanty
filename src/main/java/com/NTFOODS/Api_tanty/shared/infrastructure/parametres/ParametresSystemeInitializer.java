package com.NTFOODS.Api_tanty.shared.infrastructure.parametres;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ParametresSystemeInitializer implements CommandLineRunner {

    private final ParametresSystemeJpaRepository repository;

    public ParametresSystemeInitializer(ParametresSystemeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        initIfAbsent("SEUIL_SECURITE_CAISSE", 500_000,
            "Seuil de sécurisation caisse (FCFA) — déclenche transfert banque");
        initIfAbsent("SEUIL_ALERTE_ROUGE_VERSEMENT", 5_000,
            "Écart maximal toléré (FCFA) entre cash versé et montant attendu");
        initIfAbsent("MONTANT_PRIME_HEBDO", 12_000,
            "Montant prime hebdomadaire commercial (FCFA)");
        initIfAbsent("TAUX_OBJECTIF_GLOBAL_PRIME", 80,
            "Taux minimum de réalisation global pour éligibilité prime (%)");
        initIfAbsent("TAUX_OBJECTIF_GAMME_PRIME", 75,
            "Taux minimum de réalisation par gamme pour éligibilité prime (%)");
        initIfAbsent("SEUIL_ACTIVITE_SUSPECTE", 5,
            "Nombre d'annulations/modifications répétées en 24h déclenchant une alerte");
        initIfAbsent("DUREE_SESSION_HEURES", 8,
            "Durée de validité d'une session utilisateur (heures)");
        initIfAbsent("MAX_TENTATIVES_CONNEXION", 5,
            "Nombre maximum de tentatives de connexion avant blocage compte");
    }

    private void initIfAbsent(String cle, double valeur, String description) {
        if (repository.findByCle(cle).isEmpty()) {
            ParametresSystemeJpaEntity p = new ParametresSystemeJpaEntity();
            p.setCle(cle);
            p.setValeurNumerique(valeur);
            p.setDescription(description);
            p.setModifiePar("SYSTEME");
            p.setHorodatage(LocalDateTime.now());
            repository.save(p);
        }
    }
}
