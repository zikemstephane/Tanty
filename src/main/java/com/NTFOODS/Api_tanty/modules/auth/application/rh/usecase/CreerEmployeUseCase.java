package com.NTFOODS.Api_tanty.modules.auth.application.rh.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.request.CreerEmployeRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.mapper.EmployeMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.repository.EmployeRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.matricule.GenerateurMatriculeEmploye;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Use Case — Créer un employé (responsable ou exécutant).
 *
 * Le DG crée tout le personnel. Les responsables reçoivent un compte utilisateur.
 * Les exécutants (MACHINISTE, AGENT_PRODUCTION) n'ont pas de compte.
 */
@Service
@Transactional
public class CreerEmployeUseCase {

    private static final Set<Role> ROLES_EXECUTANTS = Set.of(Role.MACHINISTE, Role.AGENT_PRODUCTION);

    private final EmployeRepository employeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final GenerateurMatriculeEmploye generateurMatricule;
    private final EmployeMapper employeMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    public CreerEmployeUseCase(EmployeRepository employeRepository,
                               UtilisateurRepository utilisateurRepository,
                               GenerateurMatriculeEmploye generateurMatricule,
                               EmployeMapper employeMapper,
                               PasswordEncoder passwordEncoder,
                               EventPublisher eventPublisher) {
        this.employeRepository = employeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.generateurMatricule = generateurMatricule;
        this.employeMapper = employeMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public EmployeResponse executer(CreerEmployeRequest request) {
        boolean avecCompte = !ROLES_EXECUTANTS.contains(request.role());

        if (avecCompte && (request.email() == null || request.email().isBlank())) {
            throw new RegleMetierException("Un responsable doit avoir un email pour créer son compte utilisateur");
        }
        if (avecCompte && (request.motDePasse() == null || request.motDePasse().isBlank())) {
            throw new RegleMetierException("Un responsable doit avoir un mot de passe pour créer son compte utilisateur");
        }
        if (avecCompte && utilisateurRepository.existeParEmail(Email.de(request.email()))) {
            throw new RegleMetierException("L'email est déjà utilisé par un autre compte");
        }

        String matricule = generateurMatricule.generer(request.nom(), request.zoneTravail());

        Employe employe = Employe.creer(
            matricule,
            request.nom(),
            request.prenom(),
            request.role(),
            request.zoneTravail(),
            request.superviseurMatricule(),
            avecCompte,
            request.dateEmbauche()
        );
        Employe sauvegarde = employeRepository.sauvegarder(employe);

        if (avecCompte) {
            Utilisateur utilisateur = Utilisateur.creer(
                Matricule.de(matricule),
                NomComplet.de(request.prenom(), request.nom()),
                Email.de(request.email()),
                MotDePasse.deHash(passwordEncoder.encode(request.motDePasse())),
                request.role()
            );
            utilisateur.modifierProfil(
                null, null, null, null,
                request.dateEmbauche() != null ? request.dateEmbauche() : null,
                null, null
            );
            utilisateurRepository.sauvegarder(utilisateur);
            utilisateur.getDomainEvents().forEach(eventPublisher::publish);
            utilisateur.clearDomainEvents();
        }

        return employeMapper.versResponse(sauvegarde);
    }
}
