package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.matricule;

import com.NTFOODS.Api_tanty.modules.auth.domain.rh.repository.EmployeRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.Normalizer;

/**
 * Générateur de matricule employé selon la règle métier :
 * début du nom + zone de travail + 3 chiffres aléatoires.
 *
 * Exemple : DUPONT-SALLE-472
 */
@Component
public class GenerateurMatriculeEmploye {

    private static final int MAX_TENTATIVES = 10;
    private static final SecureRandom random = new SecureRandom();

    private final EmployeRepository employeRepository;

    public GenerateurMatriculeEmploye(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public String generer(String nom, String zoneTravail) {
        String prefixeNom = extrairePrefixeNom(nom);
        String zoneNormalisee = normaliser(zoneTravail);
        for (int tentative = 0; tentative < MAX_TENTATIVES; tentative++) {
            String matricule = prefixeNom + "-" + zoneNormalisee + "-" + troisChiffres();
            if (!employeRepository.existeParMatricule(matricule)) {
                return matricule;
            }
        }
        // Sécurité : si toutes les tentatives échouent, ajoute un suffixe supplémentaire
        return prefixeNom + "-" + zoneNormalisee + "-" + troisChiffres() + "-" + random.nextInt(100);
    }

    private String extrairePrefixeNom(String nom) {
        String normalise = Normalizer.normalize(nom.toUpperCase(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String sansEspace = normalise.replaceAll("[^A-Z]", "");
        return sansEspace.length() >= 5 ? sansEspace.substring(0, 5) : sansEspace;
    }

    private String normaliser(String valeur) {
        return Normalizer.normalize(valeur.toUpperCase(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("[^A-Z0-9]", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
    }

    private String troisChiffres() {
        return String.format("%03d", random.nextInt(1000));
    }
}
