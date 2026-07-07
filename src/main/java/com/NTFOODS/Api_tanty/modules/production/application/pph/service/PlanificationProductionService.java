package com.NTFOODS.Api_tanty.modules.production.application.pph.service;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.LigneBonCommande;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de calcul de la planification de production à partir d'un bon de commande.
 *
 * Calcule :
 * - la quantité de poudre (maïs, soja, arachide) par jour ouvré pour les produits BOUILLIE
 * - le nombre de fûts de 50 kg prévus
 * - les emballages plastiques (sachets, couvercles, scellés)
 * - la quantité totale en grammes pour les produits A GRIGNOTER
 */
@Service
public class PlanificationProductionService {

    private static final double POUDRE_FUT_KG = 50.0;
    private static final Locale LOCALE_FR = Locale.FRANCE;

    public ResultatPlanification calculer(BonCommande bc, Map<String, Produit> produitsParCode) {
        LocalDate dateDebut = debutSemaine(bc.getDateCommande());
        LocalDate dateFin = dateDebut.plusDays(6);
        List<LocalDate> joursOuvres = joursOuvresEntre(dateDebut, dateFin);

        List<LignePPH> lignesPPH = new ArrayList<>();
        List<LignePlanDetail> lignesDetaillees = new ArrayList<>();
        List<RepartitionJournaliere> repartitionJours = new ArrayList<>();

        for (LigneBonCommande ligne : bc.getLignes()) {
            Produit produit = produitsParCode.get(ligne.getCodeProduit());
            if (produit == null) continue;

            Optional<Conditionnement> conditionnementOpt = trouverConditionnement(produit, ligne.getSku());
            if (conditionnementOpt.isEmpty()) continue;
            Conditionnement conditionnement = conditionnementOpt.get();

            int quantiteCartons = (int) ligne.getQuantiteCommandee();
            int nombreUnites = Math.max(1, conditionnement.getNombreUnites());
            double poidsUnitaireGrammes = conditionnement.getPoidsUnitaireGrammes();
            CategorieMetier categorie = produit.getCategorieMetier();
            RecetteBouillie recette = categorie.estBouillie()
                ? RecetteBouillie.depuisDesignation(produit.getDesignation())
                : null;

            int objectif = quantiteCartons;
            lignesPPH.add(new LignePPH(ligne.getCodeProduit(), produit.getDesignation(), objectif));

            List<LignePlanDetail> detailsParJour = repartirSurJoursOuvres(
                ligne.getCodeProduit(), produit.getDesignation(), categorie.name(),
                quantiteCartons, nombreUnites, poidsUnitaireGrammes, recette, joursOuvres
            );
            lignesDetaillees.addAll(detailsParJour);
        }

        Map<LocalDate, List<LignePlanDetail>> detailsParJour = lignesDetaillees.stream()
            .collect(Collectors.groupingBy(LignePlanDetail::getDateJour));

        for (LocalDate jour : joursOuvres) {
            List<LignePlanDetail> details = detailsParJour.getOrDefault(jour, List.of());
            double poudreMais = details.stream().mapToDouble(LignePlanDetail::getPoudreMaisKg).sum();
            double poudreSoja = details.stream().mapToDouble(LignePlanDetail::getPoudreSojaKg).sum();
            double poudreArachide = details.stream().mapToDouble(LignePlanDetail::getPoudreArachideKg).sum();
            int futs = details.stream().mapToInt(LignePlanDetail::getNombreFuts50kg).sum();
            int sachets = details.stream().mapToInt(LignePlanDetail::getTotalSachets).sum();
            int couvercles = details.stream().mapToInt(LignePlanDetail::getTotalCouvercles).sum();
            int scelles = details.stream().mapToInt(LignePlanDetail::getTotalScelles).sum();
            repartitionJours.add(new RepartitionJournaliere(
                jour, details, poudreMais, poudreSoja, poudreArachide, futs, sachets, couvercles, scelles
            ));
        }

        String semaine = "S" + dateDebut.get(WeekFields.of(LOCALE_FR).weekOfWeekBasedYear())
            + "-" + dateDebut.getYear();

        return new ResultatPlanification(semaine, dateDebut, dateFin, lignesPPH, lignesDetaillees, repartitionJours);
    }

    private Optional<Conditionnement> trouverConditionnement(Produit produit, String sku) {
        if (sku != null && !sku.isBlank()) {
            return produit.getConditionnements().stream()
                .filter(c -> c.getSku() != null && c.getSku().equalsIgnoreCase(sku.trim()))
                .findFirst();
        }
        return produit.getConditionnements().stream().findFirst();
    }

    private List<LignePlanDetail> repartirSurJoursOuvres(String codeProduit, String designation, String categorieMetier,
                                                         int quantiteCartons, int nombreUnites,
                                                         double poidsUnitaireGrammes, RecetteBouillie recette,
                                                         List<LocalDate> joursOuvres) {
        List<LignePlanDetail> resultat = new ArrayList<>();
        if (joursOuvres.isEmpty()) return resultat;

        int nbJours = joursOuvres.size();
        int base = quantiteCartons / nbJours;
        int reste = quantiteCartons % nbJours;

        for (int i = 0; i < nbJours; i++) {
            int qteJour = base + (i < reste ? 1 : 0);
            resultat.add(new LignePlanDetail(
                codeProduit, designation, categorieMetier, qteJour, nombreUnites,
                poidsUnitaireGrammes, recette, joursOuvres.get(i), qteJour
            ));
        }
        return resultat;
    }

    private LocalDate debutSemaine(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    private List<LocalDate> joursOuvresEntre(LocalDate debut, LocalDate fin) {
        List<LocalDate> jours = new ArrayList<>();
        LocalDate courant = debut;
        while (!courant.isAfter(fin)) {
            DayOfWeek jour = courant.getDayOfWeek();
            if (jour != DayOfWeek.SATURDAY && jour != DayOfWeek.SUNDAY) {
                jours.add(courant);
            }
            courant = courant.plusDays(1);
        }
        return jours;
    }

    public record ResultatPlanification(
        String semaine,
        LocalDate dateDebut,
        LocalDate dateFin,
        List<LignePPH> lignesPPH,
        List<LignePlanDetail> lignesDetaillees,
        List<RepartitionJournaliere> repartitionJours
    ) {}
}
