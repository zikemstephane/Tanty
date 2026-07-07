package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Service applicatif — cycle de vie des codes OTP (Pilier 3 : les opérations
 * financières sensibles, notamment les versements et les déblocages de prix
 * exceptionnels, exigent la saisie d'un code temporaire envoyé par SMS au
 * titulaire du compte).
 */
@Service
public class OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int MAX_TENTATIVES_VERIFICATION = 3;

    private final CodeOtpJpaRepository repository;
    private final SmsGatewayPort smsGateway;
    private final UtilisateurRepository utilisateurRepository;
    private final int dureeValiditeMinutes;

    public OtpService(CodeOtpJpaRepository repository, SmsGatewayPort smsGateway,
                       UtilisateurRepository utilisateurRepository,
                       @Value("${app.otp.duree-validite-minutes:5}") int dureeValiditeMinutes) {
        this.repository = repository;
        this.smsGateway = smsGateway;
        this.utilisateurRepository = utilisateurRepository;
        this.dureeValiditeMinutes = dureeValiditeMinutes;
    }

    /**
     * Génère un code à 6 chiffres pour l'opération ciblée et l'envoie par SMS
     * au titulaire du compte (téléphone associé au matricule dans la fiche
     * utilisateur / annuaire des commerciaux — ici on notifie via le canal
     * SMS générique, le matricule servant d'identifiant de destinataire).
     */
    @Transactional
    public void genererEtEnvoyer(String matricule, String operationCible) {
        utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> new OtpInvalideException("Utilisateur inconnu : " + matricule));

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));

        CodeOtpJpaEntity entity = new CodeOtpJpaEntity();
        entity.setMatricule(matricule);
        entity.setCode(code);
        entity.setOperationCible(operationCible);
        entity.setUtilise(false);
        entity.setTentativesVerification(0);
        entity.setDateGeneration(LocalDateTime.now());
        entity.setDateExpiration(LocalDateTime.now().plusMinutes(dureeValiditeMinutes));
        repository.save(entity);

        smsGateway.envoyer(matricule, "TANTY ERP — Code de confirmation : " + code
            + " (valable " + dureeValiditeMinutes + " min). Ne le partagez avec personne.");
    }

    /**
     * Vérifie le code fourni pour l'opération ciblée. Après 3 tentatives
     * erronées sur le même code, celui-ci est invalidé (il faut en régénérer
     * un nouveau) — même logique anti-forçage que pour la connexion.
     */
    @Transactional
    public void verifier(String matricule, String operationCible, String code) {
        CodeOtpJpaEntity entity = repository
            .findFirstByMatriculeAndOperationCibleAndUtiliseFalseOrderByDateGenerationDesc(matricule, operationCible)
            .orElseThrow(() -> new OtpInvalideException(
                "Aucun code OTP en attente pour cette opération. Veuillez en générer un nouveau."));

        if (entity.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new OtpInvalideException("Le code OTP a expiré. Veuillez en générer un nouveau.");
        }
        if (entity.getTentativesVerification() >= MAX_TENTATIVES_VERIFICATION) {
            throw new OtpInvalideException("Nombre maximal de tentatives atteint. Veuillez générer un nouveau code.");
        }
        if (!entity.getCode().equals(code)) {
            entity.setTentativesVerification(entity.getTentativesVerification() + 1);
            repository.save(entity);
            throw new OtpInvalideException("Code OTP incorrect.");
        }

        entity.setUtilise(true);
        repository.save(entity);
    }
}
