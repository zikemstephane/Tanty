package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.LoginResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.VerifierOtpRequest;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteBloqueException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteDesactiveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.UtilisateurNonTrouveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.security.JwtService;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.security.OtpService;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VerifierOtpUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final OtpService otpService;
    private final JwtService jwtService;
    private final EventPublisher eventPublisher;

    public VerifierOtpUseCase(UtilisateurRepository utilisateurRepository,
                               OtpService otpService,
                               JwtService jwtService,
                               EventPublisher eventPublisher) {
        this.utilisateurRepository = utilisateurRepository;
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
    }

    public LoginResponse executer(VerifierOtpRequest request) {
        Matricule matricule = Matricule.de(request.matricule());

        Utilisateur utilisateur = utilisateurRepository.trouverParMatricule(matricule)
            .orElseThrow(() -> new UtilisateurNonTrouveException(matricule.getValeur()));

        if (!utilisateur.isActif()) {
            throw new CompteDesactiveException(matricule.getValeur());
        }
        if (utilisateur.isCompteBloque()) {
            throw new CompteBloqueException(matricule.getValeur());
        }

        boolean otpValide = otpService.verifierOtp(matricule.getValeur(), request.codeOtp());
        if (!otpValide) {
            throw new IllegalArgumentException("Code OTP invalide ou expiré");
        }

        String sessionId = utilisateur.ouvrirNouvelleSession();
        utilisateur.getDomainEvents().forEach(eventPublisher::publish);
        utilisateur.clearDomainEvents();
        utilisateurRepository.sauvegarder(utilisateur);

        String token = jwtService.genererToken(
            utilisateur.getMatricule().getValeur(),
            utilisateur.getRole().name(),
            sessionId
        );

        return new LoginResponse(
            token,
            utilisateur.getMatricule().getValeur(),
            utilisateur.getNomComplet().getNomComplet(),
            utilisateur.getRole(),
            jwtService.getExpirationMs()
        );
    }
}
