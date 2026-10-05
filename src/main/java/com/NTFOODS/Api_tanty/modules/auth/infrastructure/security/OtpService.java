package com.NTFOODS.Api_tanty.modules.auth.infrastructure.security;

import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.OtpCodeJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.OtpCodeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service("authOtpService")
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private static final int LONGUEUR_CODE = 6;
    private static final int DUREE_VALIDITE_MINUTES = 5;
    private static final int MAX_TENTATIVES_VERIFICATION = 3;

    private final OtpCodeJpaRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(OtpCodeJpaRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Transactional
    public String genererEtEnvoyerOtp(String matricule, String telephone) {
        String code = genererCode();
        String codeHash = Integer.toHexString(code.hashCode());

        OtpCodeJpaEntity otp = new OtpCodeJpaEntity();
        otp.setMatricule(matricule);
        otp.setCodeHash(codeHash);
        otp.setTelephone(telephone);
        otp.setUtilise(false);
        otp.setDateCreation(LocalDateTime.now());
        otp.setDateExpiration(LocalDateTime.now().plusMinutes(DUREE_VALIDITE_MINUTES));
        otp.setTentativesVerification(0);
        otpRepository.save(otp);

        envoyerOtpParSms(telephone, code, matricule);

        return code;
    }

    @Transactional
    public boolean verifierOtp(String matricule, String codeSaisi) {
        OtpCodeJpaEntity otp = otpRepository
            .findFirstByMatriculeAndUtiliseFalseOrderByDateCreationDesc(matricule)
            .orElse(null);

        if (otp == null) return false;
        if (otp.isUtilise()) return false;
        if (otp.getDateExpiration().isBefore(LocalDateTime.now())) return false;
        if (otp.getTentativesVerification() >= MAX_TENTATIVES_VERIFICATION) {
            otp.setUtilise(true);
            otpRepository.save(otp);
            return false;
        }

        otp.setTentativesVerification(otp.getTentativesVerification() + 1);

        String codeHashSaisi = Integer.toHexString(codeSaisi.hashCode());
        if (codeHashSaisi.equals(otp.getCodeHash())) {
            otp.setUtilise(true);
            otpRepository.save(otp);
            return true;
        }

        otpRepository.save(otp);
        return false;
    }

    @Transactional
    public void purgerOtpExpires() {
        otpRepository.deleteByDateExpirationBefore(LocalDateTime.now());
    }

    private String genererCode() {
        StringBuilder sb = new StringBuilder(LONGUEUR_CODE);
        for (int i = 0; i < LONGUEUR_CODE; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }

    private void envoyerOtpParSms(String telephone, String code, String matricule) {
        log.info("[OTP] Envoi SMS au {} pour {} — code OTP généré (durée {} min)",
            telephone, matricule, DUREE_VALIDITE_MINUTES);
    }
}
