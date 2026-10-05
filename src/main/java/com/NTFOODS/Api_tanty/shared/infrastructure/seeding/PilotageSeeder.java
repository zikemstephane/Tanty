package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ParametrageObjectifRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Paramètres de pilotage : seuils d'alerte de stock et objectifs commerciaux.
 *
 * <p>Ce sont les repères sans lesquels les tableaux de bord n'ont rien à dire.
 * Un stock sans seuil ne déclenche jamais d'alerte : l'écran Alertes reste
 * vide, non parce que tout va bien, mais parce que rien n'a été défini. Un
 * commercial sans objectif a un taux d'atteinte incalculable, que les écrans
 * affichent alors comme zéro — indistinguable d'un commercial qui n'a rien
 * vendu.</p>
 *
 * <p><strong>Aucun objectif ne porte sur la semaine en cours.</strong>
 * {@link SeedCalendrier} l'impose et ce n'est pas une précaution formelle :
 * un objectif fictif sur la semaine courante entrerait dans le calcul des
 * primes réelles et dans le classement du jour.</p>
 */
@Component
@Order(6)
public class PilotageSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(PilotageSeeder.class);

    private static final String AUTEUR = "RP-001";
    /** Nombre de semaines révolues couvertes par les objectifs d'amorçage. */
    private static final int SEMAINES_HISTORIQUE = 6;

    /**
     * Objectif hebdomadaire de référence, en FCFA, par commercial.
     *
     * <p>Les montants diffèrent : les secteurs ne se valent pas, et un objectif
     * identique pour tous produirait un classement qui ne mesure que la taille
     * du secteur. COM-005 couvre les axes périurbains, à fréquence de tournée
     * plus faible ; son objectif est en conséquence.</p>
     */
    private static final Map<String, Double> OBJECTIF_HEBDO = Map.of(
        "COM-001", 2_400_000d,
        "COM-002", 1_950_000d,
        "COM-003", 2_700_000d,
        "COM-004", 2_100_000d,
        "COM-005", 1_500_000d
    );

    /** Répartition de l'objectif global entre les gammes, en proportion. */
    private static final Map<String, Double> REPARTITION_GAMMES = Map.of(
        "Bouillies", 0.55,
        "Chocolats", 0.25,
        "À grignoter", 0.20
    );

    private final ProduitRepository produitRepository;
    private final SeuilStockRepository seuilRepository;
    private final ObjectifCommercialRepository objectifRepository;
    private final ParametrageObjectifRepository parametrageRepository;

    public PilotageSeeder(ProduitRepository produitRepository,
                          SeuilStockRepository seuilRepository,
                          ObjectifCommercialRepository objectifRepository,
                          ParametrageObjectifRepository parametrageRepository) {
        this.produitRepository = produitRepository;
        this.seuilRepository = seuilRepository;
        this.objectifRepository = objectifRepository;
        this.parametrageRepository = parametrageRepository;
    }

    @Override
    @Transactional
    public void seed() {
        int seuils = seedSeuils();
        int parametrages = seedParametrageJoursOuvres();
        int objectifs = seedObjectifs();
        log.info("[SEED] Pilotage : {} seuil(s), {} paramétrage(s) de jours ouvrés, {} objectif(s)",
            seuils, parametrages, objectifs);
    }

    /**
     * Seuils de réapprovisionnement, en unités de base.
     *
     * <p>Ils sont proportionnés au rôle de l'entrepôt : le central doit tenir
     * plusieurs semaines, le tampon quelques jours. Un seuil unique pour les
     * deux déclencherait des alertes permanentes sur le tampon, qui se vide par
     * construction à chaque départ en tournée.</p>
     *
     * <p>Le stock mobile n'a délibérément pas de seuil, et {@code SeuilStock} le
     * refuse d'ailleurs à la construction : un camion n'est pas réapprovisionné,
     * il est <em>doté</em> le matin puis vidé dans la journée. Lui poser un
     * seuil de réappro produirait une alerte quotidienne signalant comme une
     * anomalie le déroulement normal d'une tournée.</p>
     */
    private int seedSeuils() {
        int poses = 0;
        for (Produit p : produitRepository.trouverTous()) {
            String code = p.getCode().getValeur();
            boolean vendable = p.getCategorie() == CategorieProduit.PRODUIT_FINI;

            poses += poser(code, TypeEntrepot.CENTRAL, vendable ? 600 : 200, vendable ? 300 : 100);
            if (vendable) {
                poses += poser(code, TypeEntrepot.TAMPON, 200, 100);
            }
        }
        return poses;
    }

    private int poser(String codeProduit, TypeEntrepot type, long reappro, long securite) {
        if (seuilRepository.trouver(codeProduit, type).isPresent()) {
            return 0;
        }
        seuilRepository.sauvegarder(
            SeuilStock.definirAvecSeuilSecurite(codeProduit, type, reappro, securite));
        return 1;
    }

    /**
     * Nombre de jours ouvrés servant au calcul des objectifs journaliers.
     *
     * <p>C'est la seule source de vérité de cette valeur : {@code ObjectifCommercial}
     * refuse volontairement toute valeur par défaut, précisément pour qu'elle ne
     * puisse pas exister en deux endroits.</p>
     */
    private int seedParametrageJoursOuvres() {
        if (parametrageRepository.trouverCourant().isPresent()) {
            return 0;
        }
        parametrageRepository.sauvegarder(ParametrageObjectif.creer(
            "PARAM-OBJ-001", 6,
            SeedCalendrier.ilYA(SEMAINES_HISTORIQUE * 7 + 1),
            "DG-001",
            "Semaine commerciale du lundi au samedi ; le dimanche n'est pas travaillé."));
        return 1;
    }

    private int seedObjectifs() {
        int nombreJoursOuvres = parametrageRepository.trouverCourant()
            .map(ParametrageObjectif::getNombreJoursOuvres)
            .orElse(6);

        int poses = 0;
        for (int recul = 1; recul <= SEMAINES_HISTORIQUE; recul++) {
            LocalDate lundi = lundiDeLaSemaine(recul);
            LocalDate samedi = lundi.plusDays(5);

            // Garde-fou : la semaine en cours ne doit jamais être touchée, même
            // si le calcul du lundi venait à changer.
            if (!SeedCalendrier.estPeuplable(samedi)) {
                continue;
            }

            for (Map.Entry<String, Double> e : OBJECTIF_HEBDO.entrySet()) {
                String matricule = e.getKey();
                if (objectifRepository.trouver(matricule, lundi).isPresent()) {
                    continue;
                }
                double global = e.getValue();
                Map<String, Double> parGamme = new LinkedHashMap<>();
                for (Map.Entry<String, Double> g : REPARTITION_GAMMES.entrySet()) {
                    parGamme.put(g.getKey(), (double) Math.round(global * g.getValue()));
                }
                objectifRepository.sauvegarder(ObjectifCommercial.definir(
                    matricule, lundi, samedi, global, parGamme, nombreJoursOuvres, AUTEUR));
                poses++;
            }
        }
        return poses;
    }

    /** Lundi de la semaine située {@code recul} semaines avant la semaine en cours. */
    private static LocalDate lundiDeLaSemaine(int recul) {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate lundiCourant = aujourdhui.minusDays(
            aujourdhui.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        return lundiCourant.minusWeeks(recul);
    }

    /** Exposé pour les tests : les commerciaux couverts par les objectifs. */
    static List<String> commerciauxCouverts() {
        return List.copyOf(OBJECTIF_HEBDO.keySet());
    }
}
