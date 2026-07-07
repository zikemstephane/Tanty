package com.NTFOODS.Api_tanty.shared.presentation.controller;

import com.NTFOODS.Api_tanty.shared.infrastructure.storage.DocumentStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents — Import & Export", description = "Archivage documentaire (BC, BR, factures, bons de sortie, certificats)")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private static final Set<String> TYPES_AUTORISES = Set.of(
        "BON_COMMANDE", "BON_RECEPTION", "BON_SORTIE", "FACTURE",
        "BON_LIVRAISON", "CERTIFICAT_CONFORMITE", "BON_DOTATION", "RAPPORT");

    private static final Set<String> EXTENSIONS_AUTORISEES = Set.of(
        "pdf", "xlsx", "csv", "jpg", "jpeg", "png", "xml");

    private final DocumentStorageService storageService;

    public DocumentController(DocumentStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/importer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Importer un document (type + reference + fichier)")
    public ResponseEntity<String> importer(
            @RequestParam String typeDocument,
            @RequestParam String reference,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (!TYPES_AUTORISES.contains(typeDocument.toUpperCase())) {
            return ResponseEntity.badRequest().body("Type de document non autorisé: " + typeDocument);
        }

        String nomFichier = file.getOriginalFilename();
        String extension = nomFichier != null && nomFichier.contains(".")
            ? nomFichier.substring(nomFichier.lastIndexOf('.') + 1).toLowerCase() : "";
        if (!EXTENSIONS_AUTORISEES.contains(extension)) {
            return ResponseEntity.badRequest().body("Extension non autorisée: " + extension);
        }

        String cle = storageService.archiver(
            typeDocument.toUpperCase(),
            reference,
            extension,
            file.getInputStream(),
            file.getSize(),
            file.getContentType());

        return ResponseEntity.ok(cle);
    }

    @GetMapping("/recuperer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer un document archivé par sa clé")
    public ResponseEntity<byte[]> recuperer(@RequestParam String cle) throws java.io.IOException {
        byte[] data = storageService.recuperer(cle).readAllBytes();
        String contentType = guessContentType(cle);
        String filename = cle.contains("/") ? cle.substring(cle.lastIndexOf('/') + 1) : cle;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(data.length);
        return ResponseEntity.ok().headers(headers).body(data);
    }

    private String guessContentType(String cle) {
        if (cle.endsWith(".pdf")) return MediaType.APPLICATION_PDF_VALUE;
        if (cle.endsWith(".xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (cle.endsWith(".csv")) return "text/csv";
        if (cle.endsWith(".xml")) return MediaType.APPLICATION_XML_VALUE;
        if (cle.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        if (cle.endsWith(".jpg") || cle.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
