package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.OtpCodeJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.OtpCodeJpaRepository;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(1)
public class AuthSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(AuthSeeder.class);
    private static final String DEFAULT_PASSWORD = "test123";
    private static final String DEFAULT_OTP_CODE = "123456";

    private final UtilisateurJpaRepository utilisateurRepo;
    private final OtpCodeJpaRepository otpRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthSeeder(UtilisateurJpaRepository utilisateurRepo,
                      OtpCodeJpaRepository otpRepo,
                      PasswordEncoder passwordEncoder) {
        this.utilisateurRepo = utilisateurRepo;
        this.otpRepo = otpRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void seed() {
        String encodedPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
        String otpHash = Integer.toHexString(DEFAULT_OTP_CODE.hashCode());
        LocalDateTime now = LocalDateTime.now();

        seedUser("ADM-001", "Système", "Administrateur", "admin@ntfoods.cm", Role.ADMIN,
                "+237600000000", encodedPassword, now, "M", "Yaoundé", "Centre");
        seedUser("DG-001", "Nkengne", "Jean-Paul", "dg@ntfoods.cm", Role.DIRECTEUR_GENERAL,
                "+237690000001", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("DC-001", "Fotso", "Marie", "dc@ntfoods.cm", Role.DIRECTEUR_COMMERCIAL,
                "+237690000002", encodedPassword, now, "F", "Douala", "Littoral");
        seedUser("GS-001", "Tchoumi", "Paul", "stock@ntfoods.cm", Role.GESTIONNAIRE_STOCK,
                "+237690000003", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("CP-001", "Nguefack", "Robert", "prod@ntfoods.cm", Role.CHEF_PRODUCTION,
                "+237690000004", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("AP-001", "Kamga", "Alice", "agent.prod@ntfoods.cm", Role.RESPONSABLE_SALLE,
                "+237690000005", encodedPassword, now, "F", "Douala", "Littoral");
        seedUser("CM-001", "Bello", "Joseph", "chef.mach@ntfoods.cm", Role.CHEF_MACHINISTE,
                "+237690000006", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("MA-001", "Djoumessi", "Samuel", "mach1@ntfoods.cm", Role.MACHINISTE,
                "+237690000007", encodedPassword, now, "M", "Bafoussam", "Ouest");
        seedUser("MA-002", "Tagne", "Pierre", "mach2@ntfoods.cm", Role.MACHINISTE,
                "+237690000008", encodedPassword, now, "M", "Bafoussam", "Ouest");
        seedUser("AD-001", "Etoa", "David", "doseur@ntfoods.cm", Role.AGENT_DOSEUR,
                "+237690000009", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("CG-001", "Mfoumou", "Christine", "controle@ntfoods.cm", Role.CONTROLEUR_GENERAL,
                "+237690000010", encodedPassword, now, "F", "Yaoundé", "Centre");
        seedUser("COM-001", "Atangana", "Jean", "com1@ntfoods.cm", Role.COMMERCIAL,
                "+237691111111", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("COM-002", "Biya", "Sophie", "com2@ntfoods.cm", Role.COMMERCIAL,
                "+237692222222", encodedPassword, now, "F", "Douala", "Littoral");
        seedUser("COM-003", "Oumarou", "Ibrahim", "com3@ntfoods.cm", Role.COMMERCIAL,
                "+237693333333", encodedPassword, now, "M", "Garoua", "Nord");
        seedUser("COM-004", "Nkomo", "Patrick", "com4@ntfoods.cm", Role.COMMERCIAL,
                "+237694444444", encodedPassword, now, "M", "Yaoundé", "Centre");
        seedUser("COM-005", "Tchatchou", "Linda", "com5@ntfoods.cm", Role.COMMERCIAL,
                "+237695555555", encodedPassword, now, "F", "Bafoussam", "Ouest");
        seedUser("SEC-001", "Mbarga", "Estelle", "secretaire@ntfoods.cm", Role.SECRETAIRE,
                "+237690000011", encodedPassword, now, "F", "Douala", "Littoral");
        seedUser("CPT-001", "Foka", "Alain", "comptable@ntfoods.cm", Role.COMPTABLE,
                "+237690000012", encodedPassword, now, "M", "Douala", "Littoral");
        seedUser("RP-001", "Ngono", "Brigitte", "rp@ntfoods.cm", Role.CHARGEE_RP,
                "+237690000013", encodedPassword, now, "F", "Douala", "Littoral");

        seedOtpForAll(now, otpHash);

        log.info("[AuthSeeder] Seeded {} users with default password '{}' and OTP code '{}'",
                utilisateurRepo.count(), DEFAULT_PASSWORD, DEFAULT_OTP_CODE);
    }

    private void seedUser(String matricule, String nom, String prenom, String email, Role role,
                          String telephone, String encodedPassword, LocalDateTime now,
                          String sexe, String lieuNaissance, String residence) {
        if (utilisateurRepo.existsByMatricule(matricule)) {
            log.debug("[AuthSeeder] User {} already exists, skipping", matricule);
            return;
        }
        UtilisateurJpaEntity user = new UtilisateurJpaEntity();
        user.setMatricule(matricule);
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setMotDePasseHash(encodedPassword);
        user.setRole(role);
        user.setActif(true);
        user.setTentativesEchouees(0);
        user.setCompteBloque(false);
        user.setDateCreation(now);
        user.setTelephone(telephone);
        user.setSexe(sexe);
        user.setLieuNaissance(lieuNaissance);
        user.setResidence(residence);
        user.setDateEmbauche(LocalDate.of(2023, 1, 15));
        utilisateurRepo.save(user);
    }

    private void seedOtpForAll(LocalDateTime now, String otpHash) {
        var users = utilisateurRepo.findAll();
        for (UtilisateurJpaEntity user : users) {
            var existing = otpRepo.findByMatriculeOrderByDateCreationDesc(user.getMatricule());
            if (!existing.isEmpty()) {
                continue;
            }
            OtpCodeJpaEntity otp = new OtpCodeJpaEntity();
            otp.setMatricule(user.getMatricule());
            otp.setCodeHash(otpHash);
            otp.setTelephone(user.getTelephone());
            otp.setUtilise(false);
            otp.setDateCreation(now);
            otp.setDateExpiration(now.plusYears(1));
            otp.setTentativesVerification(0);
            otpRepo.save(otp);
        }
    }
}
