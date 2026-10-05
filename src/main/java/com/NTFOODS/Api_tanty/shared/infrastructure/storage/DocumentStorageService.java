package com.NTFOODS.Api_tanty.shared.infrastructure.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Service applicatif — archivage documentaire générique (Pilier "Gestion
 * Documentaire") : Bons de Commande, Bons de Réception, Bons de Transfert,
 * Bons de Dotation, factures, rapports exportés (PDF/Excel/CSV)...
 *
 * <p>Implémentation locale sur système de fichiers. Remplacer par MinIO
 * quand la dépendance sera disponible.</p>
 */
@Service
public class DocumentStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentStorageService.class);

    @Value("${tanty.document.storage.path:./documents}")
    private String storagePath;

    public String archiver(String typeDocument, String reference, String extension,
                            InputStream contenu, long tailleOctets, String contentType) {
        String cle = typeDocument + "/" + reference + "." + extension;
        try {
            Path baseDir = Paths.get(storagePath);
            Path typeDir = baseDir.resolve(typeDocument);
            Files.createDirectories(typeDir);
            Path filePath = typeDir.resolve(reference + "." + extension);
            Files.copy(contenu, filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Document archivé: {} ({} octets)", cle, tailleOctets);
            return cle;
        } catch (IOException e) {
            log.error("Erreur archivage document {}: {}", cle, e.getMessage());
            throw new IllegalStateException("Erreur d'archivage: " + e.getMessage(), e);
        }
    }

    public InputStream recuperer(String cle) {
        Path filePath = Paths.get(storagePath, cle);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("Récupération impossible pour {}: {}", cle, e.getMessage());
            throw new IllegalStateException("Document introuvable: " + cle, e);
        }
    }

    public boolean existe(String cle) {
        return Files.exists(Paths.get(storagePath, cle));
    }

    public long taille(String cle) {
        try {
            return Files.size(Paths.get(storagePath, cle));
        } catch (IOException e) {
            return 0;
        }
    }
}
