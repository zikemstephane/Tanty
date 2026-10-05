package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.RegisterRequest;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.EmailDejaUtiliseException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Use Case — Création d'un utilisateur (Pilier 3 : Identification Unique).
 *
 * Le matricule est généré automatiquement et ne doit JAMAIS entrer en
 * collision, y compris lors de créations concurrentes (deux administrateurs
 * enregistrant simultanément deux commerciaux, par exemple). La génération
 * par comptage n'étant pas atomique, une nouvelle tentative — dans une
 * transaction neuve à chaque fois — est effectuée en cas de violation de la
 * contrainte d'unicité sur le matricule.
 */
@Service
public class RegisterUseCase {

    private static final int MAX_TENTATIVES = 3;

    private final UtilisateurRepository utilisateurRepository;
    private final CreerUtilisateurTransactionnel creerUtilisateurTransactionnel;

    public RegisterUseCase(UtilisateurRepository utilisateurRepository,
                           CreerUtilisateurTransactionnel creerUtilisateurTransactionnel) {
        this.utilisateurRepository = utilisateurRepository;
        this.creerUtilisateurTransactionnel = creerUtilisateurTransactionnel;
    }

    public String executer(RegisterRequest request) {
        Email email = Email.de(request.email());
        if (utilisateurRepository.existeParEmail(email)) {
            throw new EmailDejaUtiliseException(request.email());
        }

        DataIntegrityViolationException derniereErreur = null;
        for (int tentative = 1; tentative <= MAX_TENTATIVES; tentative++) {
            try {
                return creerUtilisateurTransactionnel.executer(request, email);
            } catch (DataIntegrityViolationException e) {
                // Collision de matricule sous concurrence : on retente avec un comptage à jour,
                // dans une transaction neuve (voir CreerUtilisateurTransactionnel).
                derniereErreur = e;
            }
        }
        throw derniereErreur;
    }
}
