package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.ZoneDeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.ZoneDeVenteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.aggregate.Vehicule;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.repository.VehiculeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Réseau physique et territorial : entrepôts, secteurs de vente, véhicules.
 *
 * <p>Ces trois objets partagent une propriété : rien ne peut bouger sans eux.
 * Une réception exige un entrepôt cible ; une dotation exige le stock mobile du
 * commercial ; un client exige un secteur ; une demande de carburant exige un
 * véhicule immatriculé. Les peupler ensemble évite l'état intermédiaire où le
 * catalogue existe mais n'a nulle part où être stocké.</p>
 *
 * <p><strong>Un stock mobile par commercial.</strong> {@code creerMobile} dérive
 * le code de la matricule ({@code MOBILE-COM-001}) : c'est ce code que la
 * dotation crédite et que la vente débite. En omettre un rendrait ce
 * commercial-là incapable de partir en tournée, sans erreur visible avant le
 * jour où il essaie.</p>
 */
@Component
@Order(3)
public class ReseauSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(ReseauSeeder.class);

    /** Les commerciaux terrain, tels que les crée {@link AuthSeeder}. */
    static final List<String> COMMERCIAUX =
        List.of("COM-001", "COM-002", "COM-003", "COM-004", "COM-005");

    private final EntrepotRepository entrepotRepository;
    private final ZoneDeVenteRepository zoneRepository;
    private final VehiculeRepository vehiculeRepository;

    public ReseauSeeder(EntrepotRepository entrepotRepository,
                        ZoneDeVenteRepository zoneRepository,
                        VehiculeRepository vehiculeRepository) {
        this.entrepotRepository = entrepotRepository;
        this.zoneRepository = zoneRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Secteur de vente.
     *
     * <p>{@code clientsPotentiels} est le nombre de boutiques recensées sur le
     * terrain. C'est le dénominateur du taux d'occupation des marchés : le
     * poser à zéro rendrait l'indicateur incalculable, et le surestimer ferait
     * apparaître une sous-exploitation qui n'existe pas.</p>
     */
    private record Secteur(String nom, String type, String description, int clientsPotentiels) {}

    private static final List<Secteur> SECTEURS = List.of(
        new Secteur("Akwa", "URBAIN", "Centre commercial de Douala, forte densité de détaillants", 180),
        new Secteur("Bonabéri", "URBAIN", "Rive droite du Wouri, marchés de quartier", 140),
        new Secteur("New Bell", "URBAIN", "Grand marché populaire, majorité de revendeurs", 220),
        new Secteur("Deïdo", "URBAIN", "Secteur mixte, grossistes et détaillants", 120),
        new Secteur("Bonanjo", "URBAIN", "Quartier administratif, grandes surfaces", 60),
        new Secteur("Nkongsamba", "PERIURBAIN", "Axe Douala-Ouest, tournée hebdomadaire", 90),
        new Secteur("Édéa", "PERIURBAIN", "Axe Douala-Yaoundé, tournée bimensuelle", 75)
    );

    /** Véhicule affecté à un commercial, avec sa consommation de référence. */
    private record Camion(String immatriculation, String marque, String modele,
                          String matriculeCommercial, double consommationL100) {}

    private static final List<Camion> FLOTTE = List.of(
        new Camion("LT-471-AB", "Toyota", "Dyna", "COM-001", 12.5),
        new Camion("LT-882-CD", "Mitsubishi", "Canter", "COM-002", 13.2),
        new Camion("LT-315-EF", "Toyota", "Hiace", "COM-003", 10.8),
        new Camion("LT-609-GH", "Nissan", "Cabstar", "COM-004", 12.9),
        new Camion("LT-244-IJ", "Toyota", "Dyna", "COM-005", 12.5)
    );

    @Override
    @Transactional
    public void seed() {
        int entrepots = 0;
        if (!entrepotRepository.existeParCode("CENTRAL-01")) {
            entrepotRepository.sauvegarder(
                Entrepot.creerCentral("CENTRAL-01", "Entrepôt central Douala"));
            entrepots++;
        }
        if (!entrepotRepository.existeParCode("TAMPON-01")) {
            entrepotRepository.sauvegarder(
                Entrepot.creerTampon("TAMPON-01", "Zone tampon départ tournées"));
            entrepots++;
        }
        for (String matricule : COMMERCIAUX) {
            if (!entrepotRepository.existeParCode("MOBILE-" + matricule)) {
                entrepotRepository.sauvegarder(Entrepot.creerMobile(matricule));
                entrepots++;
            }
        }

        int secteurs = 0;
        for (Secteur s : SECTEURS) {
            if (zoneRepository.trouverParNom(s.nom()).isEmpty()) {
                zoneRepository.sauvegarder(
                    ZoneDeVente.creer(s.nom(), s.type(), s.description(), s.clientsPotentiels()));
                secteurs++;
            }
        }

        int camions = 0;
        for (Camion c : FLOTTE) {
            if (vehiculeRepository.trouverParImmatriculation(c.immatriculation()).isEmpty()) {
                vehiculeRepository.sauvegarder(Vehicule.enregistrer(
                    c.immatriculation(), c.marque(), c.modele(),
                    c.matriculeCommercial(), c.consommationL100()));
                camions++;
            }
        }

        log.info("[SEED] Réseau : {} entrepôt(s), {} secteur(s), {} véhicule(s) créé(s)",
            entrepots, secteurs, camions);
    }
}
