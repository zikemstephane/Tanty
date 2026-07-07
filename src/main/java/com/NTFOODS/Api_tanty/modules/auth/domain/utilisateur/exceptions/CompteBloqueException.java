package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

/**
 * Levée lorsqu'un utilisateur tente de se connecter alors que son compte a été
 * automatiquement bloqué après 3 tentatives de connexion erronées (comme une
 * carte bancaire). Seul un administrateur peut débloquer le compte.
 */
public class CompteBloqueException extends AdminException {
    public CompteBloqueException(String matricule) {
        super("Le compte " + matricule + " est bloqué après 3 tentatives échouées. " +
              "Contacter l'administrateur pour un déblocage manuel.");
    }
}
