package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.RegisterRequest;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.matricule.MatriculeSequenceService;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Collaborateur transactionnel séparé de {@link RegisterUseCase}.
 *
 * Isolé dans son propre bean (et non une méthode privée/protégée du même
 * use case) afin que {@code REQUIRES_NEW} fonctionne réellement : un appel
 * "self-invoked" depuis la même classe contournerait le proxy Spring AOP et
 * l'annotation @Transactional serait alors silencieusement ignorée. Chaque
 * tentative de {@link RegisterUseCase} obtient ainsi une transaction neuve
 * et proprement isolée, indispensable pour retenter après une collision de
 * matricule (PostgreSQL abandonne le reste d'une transaction dès la première
 * erreur de contrainte — retenter dans la même transaction échouerait à coup sûr).
 */
@Service
public class CreerUtilisateurTransactionnel {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final MatriculeSequenceService matriculeSequenceService;
    private final EventPublisher eventPublisher;

    public CreerUtilisateurTransactionnel(UtilisateurRepository utilisateurRepository,
                                          PasswordEncoder passwordEncoder,
                                          MatriculeSequenceService matriculeSequenceService,
                                          EventPublisher eventPublisher) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.matriculeSequenceService = matriculeSequenceService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String executer(RegisterRequest request, Email email) {
        String matriculeGenere = matriculeSequenceService.generer(request.role());
        Matricule matricule = Matricule.de(matriculeGenere);

        String hash = passwordEncoder.encode(request.motDePasse());

        Utilisateur utilisateur = Utilisateur.creer(
            matricule,
            NomComplet.de(request.prenom(), request.nom()),
            email,
            MotDePasse.deHash(hash),
            request.role()
        );

        utilisateur.modifierProfil(
            request.telephone(),
            request.residence(),
            request.photo(),
            request.cni(),
            request.dateNaissance(),
            request.lieuNaissance(),
            request.sexe()
        );
        if (request.dateEmbauche() != null) {
            utilisateur.setDateEmbauche(request.dateEmbauche());
        }

        utilisateurRepository.sauvegarder(utilisateur);
        utilisateur.getDomainEvents().forEach(eventPublisher::publish);
        utilisateur.clearDomainEvents();

        return matriculeGenere;
    }
}
