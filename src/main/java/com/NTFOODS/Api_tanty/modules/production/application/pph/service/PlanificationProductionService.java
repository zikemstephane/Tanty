package com.NTFOODS.Api_tanty.modules.production.application.pph.service;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.util.RepartitionExacte;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.BesoinEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.LigneBonCommande;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de calcul de la planification de production à partir d'un bon de commande.
 *
 * Pour chaque ligne du bon de commande, calcule :
 * - le poids total à produire, depuis le conditionnement réellement commandé
 *   (jamais une valeur figée) ;
 * - les besoins en matières premières, via la {@link FormuleProduction} du
 *   produit — un produit sans formule n'a pas de besoin calculé, mais c'est
 *   signalé explicitement ({@link LignePlanDetail#isFormuleManquante()}),
 *   jamais un zéro silencieux, et cela ne bloque pas la génération du plan ;
 * - les besoins en composants d'emballage, via la composition propre au
 *   conditionnement — un conditionnement sans type/composition n'a pas de
 *   besoin calculé, également signalé explicitement
 *   ({@link LignePlanDetail#isCompositionEmballageManquante()}) sans bloquer
 *   la génération du plan, la majorité du catalogue n'ayant pas encore cette
 *   donnée renseignée (cf. V27__composition_emballage.sql).
 *
 * Le besoin total de chaque ligne (production, matières premières, composants
 * d'emballage) est calculé UNE SEULE FOIS pour la ligne entière, puis réparti
 * sur les jours ouvrés de la période de production du BC — {@link
 * BonCommande#getDateDebutProduction()}/{@link BonCommande#getDateFinProduction()}
 * si renseignées, sinon repli sur la semaine ouvrée de la date de commande
 * (comportement historique, cf. {@link #resoudrePeriode}). La répartition
 * ({@link RepartitionExacte}) garantit par arrondi au plus grand reste que la
 * somme des jours est toujours exactement égale à ce total déjà calculé —
 * jamais une valeur figée par jour, toujours dérivée des données du BC.
 */
@Service
public class PlanificationProductionService {

    private static final Logger log = LoggerFactory.getLogger(PlanificationProductionService.class);

    private static final Locale LOCALE_FR = Locale.FRANCE;
    private static final int ECHELLE_KG = 2;

    private final FormuleProductionRepository formuleProductionRepository;

    public PlanificationProductionService(FormuleProductionRepository formuleProductionRepository) {
        this.formuleProductionRepository = formuleProductionRepository;
    }

    public ResultatPlanification calculer(BonCommande bc, Map<String, Produit> produitsParCode) {
        PeriodeProduction periode = resoudrePeriode(bc);
        List<LocalDate> joursOuvres = periode.joursOuvres();

        List<LignePPH> lignesPPH = new ArrayList<>();
        List<LignePlanDetail> lignesDetaillees = new ArrayList<>();
        List<RepartitionJournaliere> repartitionJours = new ArrayList<>();

        for (LigneBonCommande ligne : bc.getLignes()) {
            Produit produit = produitsParCode.get(ligne.getCodeProduit());
            if (produit == null) {
                log.warn("[PLANIF-PPH] Ligne ignorée — produit non trouvé dans le catalogue : code={} sku={}",
                    ligne.getCodeProduit(), ligne.getSku());
                continue;
            }

            Optional<Conditionnement> conditionnementOpt = trouverConditionnement(produit, ligne.getSku());
            if (conditionnementOpt.isEmpty()) {
                log.warn("[PLANIF-PPH] Ligne ignorée — conditionnement non trouvé : code={} sku={} conditionnements disponibles={}",
                    ligne.getCodeProduit(), ligne.getSku(),
                    produit.getConditionnements().stream().map(Conditionnement::getSku).collect(Collectors.toList()));
                continue;
            }
            Conditionnement conditionnement = conditionnementOpt.get();

            if (conditionnement.getPoidsUnitaireGrammes() <= 0) {
                throw new RegleMetierException(
                    "Poids unitaire manquant ou invalide pour le produit " + produit.getCode().getValeur()
                        + " (" + produit.getDesignation() + "), conditionnement " + conditionnement.getSku()
                        + " : impossible de calculer le plan de production sans ce poids. "
                        + "Corriger le poids unitaire du produit avant de valider ce bon de commande.");
            }

            int quantiteTotal = (int) ligne.getQuantiteCommandee();
            int nombreUnites = Math.max(1, conditionnement.getNombreUnites());
            double poidsUnitaireGrammes = conditionnement.getPoidsUnitaireGrammes();
            BigDecimal poidsTotalKg = BigDecimal.valueOf(quantiteTotal * (long) nombreUnites * poidsUnitaireGrammes / 1000.0);

            Optional<FormuleProduction> formuleOpt = formuleProductionRepository.trouverParCodeProduit(produit.getCode().getValeur());
            if (formuleOpt.isEmpty()) {
                log.info("[PLANIF-PPH] Produit {} sans formule de production — aucun besoin en matière première calculé (état signalé, pas une erreur)",
                    produit.getCode().getValeur());
            }
            boolean compositionEmballageManquante = conditionnement.getType() == null || conditionnement.getComposants().isEmpty();
            if (compositionEmballageManquante) {
                log.info("[PLANIF-PPH] Conditionnement {} (produit {}) sans type/composition d'emballage — aucun besoin d'emballage calculé (état signalé, pas une erreur)",
                    conditionnement.getSku(), produit.getCode().getValeur());
            }

            List<BesoinMatierePremiere> besoinsMatieresTotal = formuleOpt
                .map(f -> f.calculerBesoins(poidsTotalKg))
                .orElse(List.of());
            List<BesoinEmballage> besoinsEmballageTotal = compositionEmballageManquante
                ? List.of()
                : conditionnement.calculerComposantsEmballage(quantiteTotal);

            lignesPPH.add(new LignePPH(ligne.getCodeProduit(), produit.getDesignation(), quantiteTotal));

            List<LignePlanDetail> detailsParJour = repartirSurJours(
                ligne.getCodeProduit(), produit.getDesignation(), produit.getCategorieMetier().name(),
                quantiteTotal, nombreUnites, poidsUnitaireGrammes,
                besoinsMatieresTotal, formuleOpt.isEmpty(),
                besoinsEmballageTotal, compositionEmballageManquante,
                joursOuvres
            );
            lignesDetaillees.addAll(detailsParJour);
        }

        Map<LocalDate, List<LignePlanDetail>> detailsParJour = lignesDetaillees.stream()
            .collect(Collectors.groupingBy(LignePlanDetail::getDateJour));

        for (LocalDate jour : joursOuvres) {
            List<LignePlanDetail> details = detailsParJour.getOrDefault(jour, List.of());
            List<BesoinMatierePremiere> besoinsMatieres = agregerBesoinsMatieres(details);
            List<BesoinEmballage> besoinsEmballage = agregerBesoinsEmballage(details);
            double totalMatieresKg = besoinsMatieres.stream()
                .map(BesoinMatierePremiere::quantiteKg).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            double futs = totalMatieresKg <= 0 ? 0.0 : totalMatieresKg / 50.0;
            repartitionJours.add(new RepartitionJournaliere(jour, details, besoinsMatieres, besoinsEmballage, futs));
        }

        String semaine = calculerLabelSemaine(periode.debut(), periode.fin());

        return new ResultatPlanification(semaine, periode.debut(), periode.fin(), lignesPPH, lignesDetaillees, repartitionJours);
    }

    /**
     * Résout la période de production du BC : sa propre période
     * ({@code dateDebutProduction}/{@code dateFinProduction}) si renseignée,
     * sinon repli sur la semaine ouvrée de la date de commande (comportement
     * historique, pour ne rien casser pour les BC sans cette donnée).
     */
    private PeriodeProduction resoudrePeriode(BonCommande bc) {
        LocalDate debut = bc.getDateDebutProduction();
        LocalDate fin = bc.getDateFinProduction();
        if (debut == null || fin == null) {
            debut = debutSemaine(bc.getDateCommande());
            fin = debut.plusDays(6);
        }
        if (fin.isBefore(debut)) {
            throw new RegleMetierException(
                "Période de production invalide pour le BC " + bc.getNumero() + " : la date de fin (" + fin
                    + ") est antérieure à la date de début (" + debut + ").");
        }
        List<LocalDate> joursOuvres = joursOuvresEntre(debut, fin);
        if (joursOuvres.isEmpty()) {
            throw new RegleMetierException(
                "Aucun jour ouvré dans la période de production du BC " + bc.getNumero() + " (" + debut + " → " + fin
                    + ") : impossible de générer un plan de production.");
        }
        return new PeriodeProduction(debut, fin, joursOuvres);
    }

    private String calculerLabelSemaine(LocalDate debut, LocalDate fin) {
        WeekFields wf = WeekFields.of(LOCALE_FR);
        String labelDebut = "S" + debut.get(wf.weekOfWeekBasedYear()) + "-" + debut.getYear();
        String labelFin = "S" + fin.get(wf.weekOfWeekBasedYear()) + "-" + fin.getYear();
        return labelDebut.equals(labelFin) ? labelDebut : labelDebut + " → " + labelFin;
    }

    private List<BesoinMatierePremiere> agregerBesoinsMatieres(List<LignePlanDetail> details) {
        Map<String, BigDecimal> parMatiere = new LinkedHashMap<>();
        for (LignePlanDetail d : details) {
            for (BesoinMatierePremiere b : d.getBesoinsMatieresPremiere()) {
                parMatiere.merge(b.matierePremiere(), b.quantiteKg(), BigDecimal::add);
            }
        }
        return parMatiere.entrySet().stream()
            .map(e -> new BesoinMatierePremiere(e.getKey(), e.getValue()))
            .toList();
    }

    private List<BesoinEmballage> agregerBesoinsEmballage(List<LignePlanDetail> details) {
        Map<TypeEmballage, Long> parType = new LinkedHashMap<>();
        for (LignePlanDetail d : details) {
            for (BesoinEmballage b : d.getBesoinsEmballage()) {
                parType.merge(b.type(), b.quantite(), Long::sum);
            }
        }
        return parType.entrySet().stream()
            .map(e -> new BesoinEmballage(e.getKey(), e.getValue()))
            .toList();
    }

    private Optional<Conditionnement> trouverConditionnement(Produit produit, String sku) {
        if (sku != null && !sku.isBlank()) {
            return produit.getConditionnements().stream()
                .filter(c -> c.getSku() != null && c.getSku().equalsIgnoreCase(sku.trim()))
                .findFirst();
        }
        return produit.getConditionnements().stream().findFirst();
    }

    /**
     * Répartit le besoin total (déjà calculé pour la ligne entière) sur les
     * jours ouvrés de la période — la production au prorata de poids égaux
     * (un jour = un jour), les matières premières et l'emballage au prorata
     * de la production de chaque jour. {@link RepartitionExacte} garantit que
     * la somme des jours reproduit exactement le total fourni, quels que
     * soient les arrondis.
     */
    private List<LignePlanDetail> repartirSurJours(String codeProduit, String designation, String categorieMetier,
                                                   int quantiteTotal, int nombreUnites, double poidsUnitaireGrammes,
                                                   List<BesoinMatierePremiere> besoinsMatieresTotal, boolean formuleManquante,
                                                   List<BesoinEmballage> besoinsEmballageTotal, boolean compositionEmballageManquante,
                                                   List<LocalDate> joursOuvres) {
        int nbJours = joursOuvres.size();
        List<Long> poidsEgaux = new ArrayList<>(nbJours);
        for (int i = 0; i < nbJours; i++) poidsEgaux.add(1L);

        List<Long> quantitesJour = RepartitionExacte.repartirEntiers(quantiteTotal, poidsEgaux);

        Map<String, List<BigDecimal>> matiereParJour = new LinkedHashMap<>();
        for (BesoinMatierePremiere b : besoinsMatieresTotal) {
            matiereParJour.put(b.matierePremiere(), RepartitionExacte.repartirDecimal(b.quantiteKg(), quantitesJour, ECHELLE_KG));
        }

        Map<TypeEmballage, List<Long>> emballageParJour = new LinkedHashMap<>();
        for (BesoinEmballage b : besoinsEmballageTotal) {
            emballageParJour.put(b.type(), RepartitionExacte.repartirEntiers(b.quantite(), quantitesJour));
        }

        List<LignePlanDetail> resultat = new ArrayList<>(nbJours);
        for (int i = 0; i < nbJours; i++) {
            int qteJour = quantitesJour.get(i).intValue();
            int indexJour = i;
            List<BesoinMatierePremiere> besoinsJour = matiereParJour.entrySet().stream()
                .map(e -> new BesoinMatierePremiere(e.getKey(), e.getValue().get(indexJour)))
                .toList();
            List<BesoinEmballage> besoinsEmbJour = emballageParJour.entrySet().stream()
                .map(e -> new BesoinEmballage(e.getKey(), e.getValue().get(indexJour)))
                .toList();
            resultat.add(new LignePlanDetail(
                codeProduit, designation, categorieMetier, qteJour, nombreUnites, poidsUnitaireGrammes,
                besoinsJour, formuleManquante, besoinsEmbJour, compositionEmballageManquante,
                joursOuvres.get(i), qteJour
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

    private record PeriodeProduction(LocalDate debut, LocalDate fin, List<LocalDate> joursOuvres) {}

    public record ResultatPlanification(
        String semaine,
        LocalDate dateDebut,
        LocalDate dateFin,
        List<LignePPH> lignesPPH,
        List<LignePlanDetail> lignesDetaillees,
        List<RepartitionJournaliere> repartitionJours
    ) {}
}
