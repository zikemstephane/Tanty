package com.NTFOODS.Api_tanty.shared.application.rapport;

/** Fichier de rapport généré (nom, type MIME, contenu binaire). Partagé par tous les modules. */
public record RapportFichier(String nomFichier, String contentType, byte[] contenu) {}
