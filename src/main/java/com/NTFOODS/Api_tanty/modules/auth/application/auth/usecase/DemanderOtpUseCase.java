package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.DemanderOtpRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.OtpResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteBloqueException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteDesactiveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.UtilisateurNonTrouveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.security.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DemanderOtpUseCase {

    private static final Logger log = LoggerFactory.getLogger(DemanderOtpUseCase.class);

    private final UtilisateurRepository utilisateurRepository;
    private final OtpService otpService;

    public DemanderOtpUseCase(UtilisateurRepository utilisateurRepository, OtpService otpService) {
        this.utilisateurRepository = utilisateurRepository;
        this.otpService = otpService;
    }

    public OtpResponse executer(DemanderOtpRequest request) {
        Matricule matricule = Matricule.de(request.matricule());

        Utilisateur utilisateur = utilisateurRepository.trouverParMatricule(matricule)
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule.getValeur()));

        if (!utilisateur.isActif()) {
            throw new CompteDesactiveException(matricule.getValeur());
        }
        if (utilisateur.isCompteBloque()) {
            throw new CompteBloqueException(matricule.getValeur());
        }

        String telephone = utilisateur.getTelephone();
        if (telephone == null || telephone.isBlank()) {
            throw new IllegalStateException(
                "Aucun numéro de téléphone enregistré pour " + matricule.getValeur() +
                " — impossible d'envoyer un OTP");
        }

        otpService.genererEtEnvoyerOtp(matricule.getValeur(), telephone);

        log.info("OTP demandé pour {} — raison: {}", matricule.getValeur(),
            request.raison() != null ? request.raison() : "non précisée");

        return new OtpResponse(
            matricule.getValeur(),
            true,
            masquerTelephone(telephone),
            5
        );
    }

    private String masquerTelephone(String tel) {
        if (tel == null || tel.length() < 4) return "***";
        return tel.substring(0, 3) + "****" + tel.substring(tel.length() - 2);
    }
}
