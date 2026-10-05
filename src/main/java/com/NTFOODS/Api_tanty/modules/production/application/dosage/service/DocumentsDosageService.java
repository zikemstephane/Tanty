package com.NTFOODS.Api_tanty.modules.production.application.dosage.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service de regroupement des documents liés à une session de dosage.
 *
 * Génère un archive ZIP contenant le registre PDF et Excel.
 */
@Service
public class DocumentsDosageService {

    private final RegistreDosageExportService exportService;

    public DocumentsDosageService(RegistreDosageExportService exportService) {
        this.exportService = exportService;
    }

    public byte[] genererZipDocuments(Long sessionId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            byte[] pdf = exportService.exportPdf(sessionId);
            zos.putNextEntry(new ZipEntry("registre-dosage-" + sessionId + ".pdf"));
            zos.write(pdf);
            zos.closeEntry();

            byte[] excel = exportService.exportExcel(sessionId);
            zos.putNextEntry(new ZipEntry("registre-dosage-" + sessionId + ".xlsx"));
            zos.write(excel);
            zos.closeEntry();

            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du zip de documents", e);
        }
    }
}
