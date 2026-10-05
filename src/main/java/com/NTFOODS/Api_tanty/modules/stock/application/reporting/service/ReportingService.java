package com.NTFOODS.Api_tanty.modules.stock.application.reporting.service;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.LigneInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository.StockMobileRepository;
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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service de reporting puissant — génère et exporte les rapports sur tout.
 * Formats supportés : Excel (.xlsx) et PDF.
 */
@Service
public class ReportingService {

    private final StockMobileRepository stockMobileRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final DotationRepository dotationRepository;
    private final ProduitRepository produitRepository;
    private final InventaireRepository inventaireRepository;

    public ReportingService(StockMobileRepository stockMobileRepository,
                            NiveauStockRepository niveauStockRepository,
                            MouvementStockRepository mouvementStockRepository,
                            DotationRepository dotationRepository,
                            ProduitRepository produitRepository,
                            InventaireRepository inventaireRepository) {
        this.stockMobileRepository = stockMobileRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.dotationRepository = dotationRepository;
        this.produitRepository = produitRepository;
        this.inventaireRepository = inventaireRepository;
    }

    // ─── STOCKS MOBILES ───────────────────────────────────────────────────

    public byte[] rapportStocksMobilesExcel() throws Exception {
        List<StockMobile> stocks = stockMobileRepository.listerTous();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Stocks Mobiles");

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Commercial", "Date Session", "Produit", "Désignation", "Conditionnement",
                "Qté Initiale", "Qté Vendue", "Qté Avariée", "Qté Donnée", "Qté Restante", "Dernier Mvt"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (StockMobile sm : stocks) {
                for (var ligne : sm.getLignes()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(sm.getMatriculeCommercial());
                    row.createCell(1).setCellValue(sm.getDateSession().toString());
                    row.createCell(2).setCellValue(ligne.getCodeProduit());
                    row.createCell(3).setCellValue(ligne.getDesignation());
                    row.createCell(4).setCellValue(ligne.getConditionnement());
                    row.createCell(5).setCellValue(ligne.getQuantiteInitiale());
                    row.createCell(6).setCellValue(ligne.getQuantiteVendue());
                    row.createCell(7).setCellValue(ligne.getQuantiteAvariee());
                    row.createCell(8).setCellValue(ligne.getQuantiteDonnee());
                    row.createCell(9).setCellValue(ligne.getQuantiteDisponible());
                    row.createCell(10).setCellValue(ligne.getDateDernierMouvement() != null
                        ? ligne.getDateDernierMouvement().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
                }
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] rapportStocksMobilesPdf() throws Exception {
        List<StockMobile> stocks = stockMobileRepository.listerTous();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(out));
            Document doc = new Document(pdf);

            doc.add(new Paragraph("Rapport des Stocks Mobiles Commerciaux")
                .setBold().setFontSize(16));
            doc.add(new Paragraph("Généré le " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setFontSize(9));

            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 3, 2, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f}))
                .setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"Commercial", "Date", "Produit", "Désignation", "Cond.", "Init.", "Vendu", "Avar.", "Donné", "Restant"};
            for (String h : headers) table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(h).setFontSize(7)));

            for (StockMobile sm : stocks) {
                for (var ligne : sm.getLignes()) {
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(sm.getMatriculeCommercial()).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(sm.getDateSession().toString()).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(ligne.getCodeProduit()).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(ligne.getDesignation()).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(ligne.getConditionnement()).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(ligne.getQuantiteInitiale())).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(ligne.getQuantiteVendue())).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(ligne.getQuantiteAvariee())).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(ligne.getQuantiteDonnee())).setFontSize(6)));
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(ligne.getQuantiteDisponible())).setFontSize(6)));
                }
            }
            doc.add(table);
            doc.close();
            return out.toByteArray();
        }
    }

    // ─── PERFORMANCES COMMERCIAUX ─────────────────────────────────────────

    public byte[] rapportPerformancesExcel() throws Exception {
        List<StockMobile> stocks = stockMobileRepository.listerTous();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Performances Commerciaux");

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Commercial", "Date Session", "Total Vendu", "Total Restant", "Total Avarié", "Taux de Vente (%)"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (StockMobile sm : stocks) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(sm.getMatriculeCommercial());
                row.createCell(1).setCellValue(sm.getDateSession().toString());
                row.createCell(2).setCellValue(sm.totalVentes());
                row.createCell(3).setCellValue(sm.totalRestant());
                row.createCell(4).setCellValue(sm.totalAvaries());
                long totalInit = sm.totalVentes() + sm.totalRestant() + sm.totalAvaries();
                double taux = totalInit > 0 ? (sm.totalVentes() * 100.0 / totalInit) : 0;
                row.createCell(5).setCellValue(String.format("%.1f", taux));
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── STOCK CENTRAL & TAMPON ───────────────────────────────────────────

    public byte[] rapportStockCentralExcel() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.CENTRAL);
        return genererExcelNiveauxStock(niveaux, "Stock Central", IndexedColors.LIGHT_BLUE);
    }

    public byte[] rapportStockCentralPdf() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.CENTRAL);
        return genererPdfNiveauxStock(niveaux, "Rapport du Stock Central");
    }

    public byte[] rapportTamponExcel() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.TAMPON);
        return genererExcelNiveauxStock(niveaux, "Stock Tampon", IndexedColors.LIGHT_YELLOW);
    }

    public byte[] rapportTamponPdf() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.TAMPON);
        return genererPdfNiveauxStock(niveaux, "Rapport du Stock Tampon");
    }

    // ─── MOUVEMENTS DE STOCK ──────────────────────────────────────────────

    public byte[] rapportMouvementsExcel(LocalDate debut, LocalDate fin) throws Exception {
        List<MouvementStock> mouvements = mouvementStockRepository.trouverParPeriode(debut, fin);
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Mouvements");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Reference", "Date", "Type", "Produit", "SKU", "Quantite", "Source", "Cible", "Acteur", "Document", "Motif"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (MouvementStock m : mouvements) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getReference());
                row.createCell(1).setCellValue(m.getHorodatage().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                row.createCell(2).setCellValue(m.getType().getLibelle());
                row.createCell(3).setCellValue(m.getCodeProduit());
                row.createCell(4).setCellValue(m.getSku() != null ? m.getSku() : "");
                row.createCell(5).setCellValue(m.getQuantiteEnBase());
                row.createCell(6).setCellValue(m.getCodeEntrepotSource() != null ? m.getCodeEntrepotSource() : "");
                row.createCell(7).setCellValue(m.getCodeEntrepotCible() != null ? m.getCodeEntrepotCible() : "");
                row.createCell(8).setCellValue(m.getMatriculeActeur());
                row.createCell(9).setCellValue(m.getReferenceDocument() != null ? m.getReferenceDocument() : "");
                row.createCell(10).setCellValue(m.getMotif() != null ? m.getMotif() : "");
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── VALORISATION DU STOCK ────────────────────────────────────────────

    public byte[] rapportValorisationExcel() throws Exception {
        List<NiveauStock> tous = niveauStockRepository.trouverTous();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Valorisation Stock");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Produit", "Entrepot", "Type", "Quantite Base", "Prix Unit. HT", "Valorisation Totale"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            BigDecimal totalGlobal = BigDecimal.ZERO;
            for (NiveauStock n : tous) {
                Produit produit = produitRepository.trouverParCode(CodeProduit.de(n.getCodeProduit())).orElse(null);
                BigDecimal prixUnit = produit != null && !produit.getConditionnements().isEmpty()
                    ? produit.getConditionnements().get(0).getPrixUnitaireHT() : BigDecimal.ZERO;
                BigDecimal valorisation = prixUnit.multiply(BigDecimal.valueOf(n.getQuantiteEnBase()));
                totalGlobal = totalGlobal.add(valorisation);
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(n.getCodeProduit());
                row.createCell(1).setCellValue(n.getCodeEntrepot());
                row.createCell(2).setCellValue(n.getTypeEntrepot().getLibelle());
                row.createCell(3).setCellValue(n.getQuantiteEnBase());
                row.createCell(4).setCellValue(prixUnit.toPlainString());
                row.createCell(5).setCellValue(valorisation.toPlainString());
            }
            Row totalRow = sheet.createRow(rowIdx);
            totalRow.createCell(4).setCellValue("TOTAL");
            totalRow.createCell(5).setCellValue(totalGlobal.toPlainString());
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── DOTATIONS VS VENTES ──────────────────────────────────────────────

    public byte[] rapportDotationsVsVentesExcel() throws Exception {
        List<Dotation> dotations = dotationRepository.trouverTous();
        List<StockMobile> stocks = stockMobileRepository.listerTous();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Dotations vs Ventes");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Commercial", "Dotation #", "Date Effet", "Statut", "Total Dotation", "Total Vendu", "Taux Vente (%)", "Taux Retour (%)"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (Dotation d : dotations) {
                long totalDotation = d.getLignes().stream().mapToLong(l -> l.getQuantiteApprouvee()).sum();
                long totalVendu = stocks.stream()
                    .filter(s -> s.getMatriculeCommercial().equals(d.getMatriculeCommercial()))
                    .mapToLong(StockMobile::totalVentes).sum();
                long totalRestant = stocks.stream()
                    .filter(s -> s.getMatriculeCommercial().equals(d.getMatriculeCommercial()))
                    .mapToLong(StockMobile::totalRestant).sum();
                double tauxVente = totalDotation > 0 ? (totalVendu * 100.0 / totalDotation) : 0;
                double tauxRetour = totalDotation > 0 ? (totalRestant * 100.0 / totalDotation) : 0;
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(d.getMatriculeCommercial());
                row.createCell(1).setCellValue(d.getNumero());
                row.createCell(2).setCellValue(d.getDateEffet().toString());
                row.createCell(3).setCellValue(d.getStatut().getLibelle());
                row.createCell(4).setCellValue(totalDotation);
                row.createCell(5).setCellValue(totalVendu);
                row.createCell(6).setCellValue(String.format("%.1f", tauxVente));
                row.createCell(7).setCellValue(String.format("%.1f", tauxRetour));
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── ÉCARTS D'INVENTAIRE ─────────────────────────────────────────────

    public byte[] rapportEcartsInventaireExcel() throws Exception {
        List<SessionInventaire> sessions = inventaireRepository.trouverTous();
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Écarts Inventaire");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Session", "Date", "Type", "Entrepot", "Produit", "Qté Théorique", "Qté Comptée", "Écart", "Écart (%)", "Statut"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (SessionInventaire session : sessions) {
                for (LigneInventaire ligne : session.getLignes()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(session.getNumero());
                    row.createCell(1).setCellValue(session.getDateInventaire().toString());
                    row.createCell(2).setCellValue(session.getType().name());
                    row.createCell(3).setCellValue(session.getCodeEntrepot());
                    row.createCell(4).setCellValue(ligne.getCodeProduit());
                    row.createCell(5).setCellValue(ligne.getQuantiteTheorique());
                    row.createCell(6).setCellValue(ligne.getQuantiteComptee() != null ? ligne.getQuantiteComptee() : 0);
                    row.createCell(7).setCellValue(ligne.getEcart());
                    double pct = ligne.getQuantiteTheorique() > 0
                        ? (ligne.getEcart() * 100.0 / ligne.getQuantiteTheorique()) : 0;
                    row.createCell(8).setCellValue(String.format("%.1f", pct));
                    row.createCell(9).setCellValue(session.getStatut().name());
                }
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── ROTATION DES STOCKS ──────────────────────────────────────────────

    public byte[] rapportRotationStockExcel() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverTous();
        List<MouvementStock> mouvements = mouvementStockRepository.trouverParPeriode(
            LocalDate.now().minusMonths(3), LocalDate.now());

        Map<String, Long> sortiesParProduit = new HashMap<>();
        for (MouvementStock m : mouvements) {
            if (m.getType().getSens() == com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.SensMouvement.SORTIE) {
                sortiesParProduit.merge(m.getCodeProduit(), m.getQuantiteEnBase(), Long::sum);
            }
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Rotation Stock");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Produit", "Entrepot", "Type", "Stock Actuel", "Sorties 3 mois", "Taux Rotation", "Statut"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (NiveauStock n : niveaux) {
                long sorties = sortiesParProduit.getOrDefault(n.getCodeProduit(), 0L);
                double tauxRotation = n.getQuantiteEnBase() > 0
                    ? (sorties * 100.0 / n.getQuantiteEnBase()) : 0;
                String statut = tauxRotation == 0 ? "DORMANT"
                    : tauxRotation < 25 ? "LENTE"
                    : tauxRotation < 75 ? "NORMALE"
                    : "RAPIDE";
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(n.getCodeProduit());
                row.createCell(1).setCellValue(n.getCodeEntrepot());
                row.createCell(2).setCellValue(n.getTypeEntrepot().getLibelle());
                row.createCell(3).setCellValue(n.getQuantiteEnBase());
                row.createCell(4).setCellValue(sorties);
                row.createCell(5).setCellValue(String.format("%.1f%%", tauxRotation));
                row.createCell(6).setCellValue(statut);
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ─── EXPORT CSV ───────────────────────────────────────────────────────

    public byte[] rapportStockCentralCsv() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.CENTRAL);
        return genererCsvNiveauxStock(niveaux);
    }

    public byte[] rapportTamponCsv() throws Exception {
        List<NiveauStock> niveaux = niveauStockRepository.trouverParType(TypeEntrepot.TAMPON);
        return genererCsvNiveauxStock(niveaux);
    }

    public byte[] rapportMouvementsCsv(LocalDate debut, LocalDate fin) throws Exception {
        List<MouvementStock> mouvements = mouvementStockRepository.trouverParPeriode(debut, fin);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); Writer writer = new OutputStreamWriter(out)) {
            writer.write("Reference;Date;Type;Produit;SKU;Quantite;Source;Cible;Acteur;Document;Motif\n");
            for (MouvementStock m : mouvements) {
                writer.write(String.join(";",
                    csv(m.getReference()),
                    csv(m.getHorodatage().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                    csv(m.getType().getLibelle()),
                    csv(m.getCodeProduit()),
                    csv(m.getSku()),
                    String.valueOf(m.getQuantiteEnBase()),
                    csv(m.getCodeEntrepotSource()),
                    csv(m.getCodeEntrepotCible()),
                    csv(m.getMatriculeActeur()),
                    csv(m.getReferenceDocument()),
                    csv(m.getMotif())) + "\n");
            }
            writer.flush();
            return out.toByteArray();
        }
    }

    private byte[] genererCsvNiveauxStock(List<NiveauStock> niveaux) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); Writer writer = new OutputStreamWriter(out)) {
            writer.write("Produit;Entrepot;Quantite Base;Derniere Modification\n");
            for (NiveauStock n : niveaux) {
                writer.write(String.join(";",
                    csv(n.getCodeProduit()),
                    csv(n.getCodeEntrepot()),
                    String.valueOf(n.getQuantiteEnBase()),
                    n.getDateModification() != null
                        ? csv(n.getDateModification().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) : "") + "\n");
            }
            writer.flush();
            return out.toByteArray();
        }
    }

    private String csv(String value) {
        if (value == null) return "";
        if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // ─── MÉTHODES UTILITAIRES ─────────────────────────────────────────────

    private byte[] genererExcelNiveauxStock(List<NiveauStock> niveaux, String titre, IndexedColors color) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet(titre);
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(color.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] cols = {"Produit", "Entrepot", "Quantite Base", "Derniere Modification"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i); c.setCellValue(cols[i]); c.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (NiveauStock n : niveaux) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(n.getCodeProduit());
                row.createCell(1).setCellValue(n.getCodeEntrepot());
                row.createCell(2).setCellValue(n.getQuantiteEnBase());
                row.createCell(3).setCellValue(n.getDateModification() != null
                    ? n.getDateModification().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
            }
            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    private byte[] genererPdfNiveauxStock(List<NiveauStock> niveaux, String titre) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(out));
            Document doc = new Document(pdf);
            doc.add(new Paragraph(titre).setBold().setFontSize(16));
            doc.add(new Paragraph("Généré le " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).setFontSize(9));
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 3}))
                .setWidth(UnitValue.createPercentValue(100));
            String[] headers = {"Produit", "Entrepot", "Qté Base", "Modification"};
            for (String h : headers) table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(h).setFontSize(8)));
            for (NiveauStock n : niveaux) {
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(n.getCodeProduit()).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(n.getCodeEntrepot()).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(n.getQuantiteEnBase())).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(
                    n.getDateModification() != null ? n.getDateModification().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "").setFontSize(7)));
            }
            doc.add(table);
            doc.close();
            return out.toByteArray();
        }
    }

    public byte[] rapportPerformancesPdf() throws Exception {
        List<StockMobile> stocks = stockMobileRepository.listerTous();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(out));
            Document doc = new Document(pdf);

            doc.add(new Paragraph("Rapport de Performances Commerciaux")
                .setBold().setFontSize(16));
            doc.add(new Paragraph("Généré le " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setFontSize(9));

            Table table = new Table(UnitValue.createPercentArray(new float[]{2.5f, 2, 1.5f, 1.5f, 1.5f, 2}))
                .setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"Commercial", "Date", "Vendu", "Restant", "Avarié", "Taux Vente"};
            for (String h : headers) table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(h).setFontSize(8)));

            for (StockMobile sm : stocks) {
                long totalInit = sm.totalVentes() + sm.totalRestant() + sm.totalAvaries();
                double taux = totalInit > 0 ? (sm.totalVentes() * 100.0 / totalInit) : 0;
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(sm.getMatriculeCommercial()).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(sm.getDateSession().toString()).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(sm.totalVentes())).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(sm.totalRestant())).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(sm.totalAvaries())).setFontSize(7)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.format("%.1f%%", taux)).setFontSize(7)));
            }
            doc.add(table);
            doc.close();
            return out.toByteArray();
        }
    }
}
