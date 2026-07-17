package com.NTFOODS.Api_tanty.modules.production.application.dosage.service;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * Service d'export du registre de dosage (PDF et Excel).
 */
@Service
public class RegistreDosageExportService {

    private final SessionDosageRepository repository;

    public RegistreDosageExportService(SessionDosageRepository repository) {
        this.repository = repository;
    }

    public byte[] exportPdf(Long sessionId) {
        SessionDosage s = repository.trouverParId(sessionId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(sessionId)));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Registre de dosage - NTFOODS TANTY").setBold().setFontSize(18));
            document.add(new Paragraph("Référence PPH : " + s.getReferencePPH()));
            document.add(new Paragraph("Date : " + s.getDate()));
            document.add(new Paragraph("Agent doseur : " + s.getMatriculeAgentDoseur()));
            document.add(new Paragraph("Fûts sortis : " + s.getNbFutsProduits()));
            document.add(new Paragraph("Fûts nets : " + s.getNbFutsNets()));
            document.add(new Paragraph("Clôturée : " + (s.isCloturee() ? "Oui" : "Non")));
            if (s.getJustificationEcart() != null) {
                document.add(new Paragraph("Justification écart : " + s.getJustificationEcart()));
            }

            document.add(new Paragraph("Matières premières utilisées").setBold().setFontSize(14));
            Table tableMatieres = new Table(new float[]{1, 1}).useAllAvailableWidth();
            tableMatieres.addHeaderCell("Type").addHeaderCell("Quantité (kg)");
            for (SessionDosage.MatierePremiereUtilisee m : s.getMatieresUtilisees()) {
                tableMatieres.addCell(m.getTypeMatiere());
                tableMatieres.addCell(String.valueOf(m.getQuantiteKg()));
            }
            document.add(tableMatieres);

            document.add(new Paragraph("Machines mobilisées").setBold().setFontSize(14));
            Table tableMachines = new Table(new float[]{1}).useAllAvailableWidth();
            tableMachines.addHeaderCell("Machine");
            for (String machine : s.getMachinesMobilisees()) {
                tableMachines.addCell(machine);
            }
            document.add(tableMachines);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    public byte[] exportExcel(Long sessionId) {
        SessionDosage s = repository.trouverParId(sessionId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(sessionId)));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Registre Dosage");
            int rowIdx = 0;

            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Registre de dosage - NTFOODS TANTY");

            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Référence PPH"); r.createCell(1).setCellValue(s.getReferencePPH());
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Date"); r.createCell(1).setCellValue(s.getDate().toString());
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Agent doseur"); r.createCell(1).setCellValue(s.getMatriculeAgentDoseur());
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Fûts sortis"); r.createCell(1).setCellValue(s.getNbFutsProduits());
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Fûts nets"); r.createCell(1).setCellValue(s.getNbFutsNets());
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Clôturée"); r.createCell(1).setCellValue(s.isCloturee() ? "Oui" : "Non");
            if (s.getJustificationEcart() != null) {
                r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue("Justification écart"); r.createCell(1).setCellValue(s.getJustificationEcart());
            }

            rowIdx++;
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Matières premières utilisées");
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Type"); r.createCell(1).setCellValue("Quantité (kg)");
            for (SessionDosage.MatierePremiereUtilisee m : s.getMatieresUtilisees()) {
                r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(m.getTypeMatiere());
                r.createCell(1).setCellValue(m.getQuantiteKg());
            }

            rowIdx++;
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Machines mobilisées");
            r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue("Machine");
            for (String machine : s.getMachinesMobilisees()) {
                r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(machine);
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du Excel", e);
        }
    }
}
