package com.NTFOODS.Api_tanty.shared.infrastructure.rapport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * Exporteur générique de tableaux vers Excel (.xlsx), CSV, PDF et JSON.
 * Partagé par tous les modules (Stock, Production, Commercial, Comptabilité).
 *
 * Habillage conforme à la Charte Graphique TANTY (section 07 — Tokens) :
 * Tanty Green comme couleur primaire des en-têtes, Ink pour le texte
 * courant, Cream pour les lignes alternées, Grey 300 pour les bordures.
 * Le jaune de marque n'est jamais utilisé en fond de texte (règle de
 * contraste WCAG de la charte) — il n'apparaît donc pas dans ces rapports
 * tabulaires denses.
 */
@Component
public class TableExporter {

    // ─── Tokens de la Charte Graphique TANTY (section 02 — Palette) ───────────
    private static final DeviceRgb TANTY_GREEN = new DeviceRgb(0x14, 0x53, 0x2D);
    private static final DeviceRgb TANTY_GREEN_LIGHT = new DeviceRgb(0x1E, 0x7A, 0x42);
    private static final DeviceRgb INK = new DeviceRgb(0x1A, 0x1A, 0x16);
    private static final DeviceRgb PAPER = new DeviceRgb(0xFF, 0xFD, 0xF7);
    private static final DeviceRgb CREAM = new DeviceRgb(0xFB, 0xF6, 0xE9);
    private static final DeviceRgb GREY_300 = new DeviceRgb(0xD8, 0xD3, 0xC4);
    private static final DeviceRgb WHITE = new DeviceRgb(0xFF, 0xFF, 0xFF);

    private static final String EXCEL_GREEN_HEX = "14532D";
    private static final String EXCEL_CREAM_HEX = "FBF6E9";

    private final ObjectMapper objectMapper;

    public TableExporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] versExcel(String titre, List<String> entetes, List<List<String>> lignes) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(tronquer(titre));

            // En-tête : fond Tanty Green, texte blanc, gras (charte : jamais de texte sur jaune)
            org.apache.poi.xssf.usermodel.XSSFCellStyle styleEntete = workbook.createCellStyle();
            styleEntete.setFillForegroundColor(couleurExcel(workbook, EXCEL_GREEN_HEX));
            styleEntete.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font policeEntete = workbook.createFont();
            policeEntete.setBold(true);
            policeEntete.setColor(IndexedColors.WHITE.getIndex());
            policeEntete.setFontName("Poppins");
            styleEntete.setFont(policeEntete);

            org.apache.poi.xssf.usermodel.XSSFCellStyle styleLigneAlternee = workbook.createCellStyle();
            styleLigneAlternee.setFillForegroundColor(couleurExcel(workbook, EXCEL_CREAM_HEX));
            styleLigneAlternee.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font policeCorps = workbook.createFont();
            policeCorps.setFontName("Inter");
            CellStyle styleCorps = workbook.createCellStyle();
            styleCorps.setFont(policeCorps);

            Row ligneEntete = sheet.createRow(0);
            for (int c = 0; c < entetes.size(); c++) {
                org.apache.poi.ss.usermodel.Cell cellule = ligneEntete.createCell(c);
                cellule.setCellValue(entetes.get(c));
                cellule.setCellStyle(styleEntete);
            }
            int r = 1;
            for (List<String> ligne : lignes) {
                Row row = sheet.createRow(r);
                CellStyle style = (r % 2 == 0) ? styleLigneAlternee : styleCorps;
                for (int c = 0; c < ligne.size(); c++) {
                    var cell = row.createCell(c);
                    cell.setCellValue(ligne.get(c) == null ? "" : ligne.get(c));
                    cell.setCellStyle(style);
                }
                r++;
            }
            for (int c = 0; c < entetes.size(); c++) sheet.autoSizeColumn(c);
            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new UncheckedIOException("Erreur de génération Excel", new java.io.IOException(e));
        }
    }

    public byte[] versCsv(List<String> entetes, List<List<String>> lignes) {
        StringBuilder sb = new StringBuilder();
        sb.append(joindreCsv(entetes)).append("\r\n");
        for (List<String> ligne : lignes) sb.append(joindreCsv(ligne)).append("\r\n");
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public byte[] versPdf(String titre, List<String> entetes, List<List<String>> lignes) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(out));
            Document document = new Document(pdf);

            document.add(new Paragraph("TANTY ERP").setBold().setFontSize(10).setFontColor(TANTY_GREEN_LIGHT));
            document.add(new Paragraph(titre).setBold().setFontSize(16).setFontColor(TANTY_GREEN));

            Table table = new Table(UnitValue.createPercentArray(entetes.size())).useAllAvailableWidth();
            entetes.forEach(h -> table.addHeaderCell(
                new Cell().add(new Paragraph(h).setFontColor(WHITE).setBold())
                    .setBackgroundColor(TANTY_GREEN)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPadding(6)));

            int ligneIndex = 0;
            for (List<String> ligne : lignes) {
                DeviceRgb fond = (ligneIndex % 2 == 0) ? CREAM : WHITE;
                for (String valeur : ligne) {
                    table.addCell(new Cell()
                        .add(new Paragraph(valeur == null ? "" : valeur).setFontColor(INK))
                        .setBackgroundColor(fond)
                        .setBorder(new com.itextpdf.layout.borders.SolidBorder(GREY_300, 0.5f))
                        .setPadding(5));
                }
                ligneIndex++;
            }
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new UncheckedIOException("Erreur de génération PDF", new java.io.IOException(e));
        }
    }

    public byte[] versJson(Object donnees) {
        try {
            return objectMapper.writeValueAsBytes(donnees);
        } catch (Exception e) {
            throw new UncheckedIOException("Erreur de génération JSON", new java.io.IOException(e));
        }
    }

    private XSSFColor couleurExcel(XSSFWorkbook workbook, String hex) {
        java.awt.Color awt = new java.awt.Color(
            Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16));
        return new XSSFColor(awt, null);
    }

    private String joindreCsv(List<String> valeurs) {
        return String.join(";", valeurs.stream().map(this::echapperCsv).toList());
    }

    private String echapperCsv(String valeur) {
        if (valeur == null) return "";
        String v = valeur.replace("\"", "\"\"");
        return (v.contains(";") || v.contains("\"") || v.contains("\n")) ? "\"" + v + "\"" : v;
    }

    private String tronquer(String titre) {
        if (titre == null) return "Rapport";
        return titre.length() > 31 ? titre.substring(0, 31) : titre;
    }
}
