package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Clientèle, rattachée aux secteurs et aux commerciaux.
 *
 * <p>Le segment ({@link TypeClient}) n'est pas décoratif : c'est lui qui décide
 * du prix opposable, la grille tarifaire étant indexée sur le couple
 * produit × segment. Un client mal segmenté se verrait facturer un prix
 * parfaitement valide mais qui n'est pas le sien — une erreur qu'aucun contrôle
 * ne peut rattraper en aval, puisque la vente serait conforme à la grille.</p>
 *
 * <p>La répartition suit ce qu'on observe sur le terrain : beaucoup de détail
 * et de revendeurs, quelques semi-grossistes, de rares grossistes et
 * distributeurs. Une clientèle uniformément répartie donnerait des moyennes et
 * des classements qui ne ressemblent à rien de réel.</p>
 *
 * <p>Les coordonnées sont celles des quartiers de Douala. Elles servent à la
 * composition des tournées ; les laisser nulles priverait la feuille de route
 * de tout regroupement géographique.</p>
 */
@Component
@Order(4)
public class ClienteleSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(ClienteleSeeder.class);

    private final ClientRepository clientRepository;

    public ClienteleSeeder(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private record Boutique(String code, String nom, String telephone, String adresse,
                            String localite, TypeClient type, String commercial,
                            double latitude, double longitude) {}

    private static final List<Boutique> CLIENTS = List.of(
        // ── Akwa — COM-001 ─────────────────────────────────────────────────
        new Boutique("CLI-0001", "Alimentation Bonne Récolte", "+237677100101",
            "Rue Joffre, face pharmacie", "Akwa", TypeClient.DETAIL, "COM-001", 4.0483, 9.7043),
        new Boutique("CLI-0002", "Établissements Njoya & Fils", "+237677100102",
            "Boulevard de la Liberté", "Akwa", TypeClient.GROSSISTE, "COM-001", 4.0501, 9.7012),
        new Boutique("CLI-0003", "Boutique Mami Ngo", "+237677100103",
            "Marché Congo, allée 4", "Akwa", TypeClient.DETAIL, "COM-001", 4.0466, 9.7089),
        new Boutique("CLI-0004", "Superette Akwa Palace", "+237677100104",
            "Avenue King Akwa", "Akwa", TypeClient.SEMI_GROSSISTE, "COM-001", 4.0512, 9.7061),
        new Boutique("CLI-0005", "Kiosque Chez Tantine", "+237677100105",
            "Carrefour Douche Municipale", "Akwa", TypeClient.REVENDEUR, "COM-001", 4.0455, 9.7035),

        // ── Bonabéri — COM-002 ─────────────────────────────────────────────
        new Boutique("CLI-0006", "Marché Bonabéri Centre", "+237677100106",
            "Entrée marché principal", "Bonabéri", TypeClient.SEMI_GROSSISTE, "COM-002", 4.0672, 9.6784),
        new Boutique("CLI-0007", "Alimentation Le Wouri", "+237677100107",
            "Route de Bonassama", "Bonabéri", TypeClient.DETAIL, "COM-002", 4.0715, 9.6802),
        new Boutique("CLI-0008", "Distribution Sawa", "+237677100108",
            "Zone industrielle Bonabéri", "Bonabéri", TypeClient.DISTRIBUTEUR, "COM-002", 4.0738, 9.6741),
        new Boutique("CLI-0009", "Boutique Mbappé", "+237677100109",
            "Quartier Mabanda", "Bonabéri", TypeClient.REVENDEUR, "COM-002", 4.0690, 9.6829),

        // ── New Bell — COM-003 ─────────────────────────────────────────────
        new Boutique("CLI-0010", "Comptoir New Bell", "+237677100110",
            "Marché central, bloc B", "New Bell", TypeClient.GROSSISTE, "COM-003", 4.0343, 9.7191),
        new Boutique("CLI-0011", "Alimentation Douala Bercy", "+237677100111",
            "Rue de la Réunification", "New Bell", TypeClient.DETAIL, "COM-003", 4.0361, 9.7228),
        new Boutique("CLI-0012", "Boutique Ngangue", "+237677100112",
            "Quartier Nkololoun", "New Bell", TypeClient.REVENDEUR, "COM-003", 4.0318, 9.7167),
        new Boutique("CLI-0013", "Provisions Madiba", "+237677100113",
            "Avenue Ahidjo", "New Bell", TypeClient.DETAIL, "COM-003", 4.0375, 9.7203),
        new Boutique("CLI-0014", "Établissement Soppo", "+237677100114",
            "Marché Nkololoun", "New Bell", TypeClient.SEMI_GROSSISTE, "COM-003", 4.0329, 9.7182),

        // ── Deïdo / Bonanjo — COM-004 ──────────────────────────────────────
        new Boutique("CLI-0015", "Supermarché Deïdo", "+237677100115",
            "Rond-point Deïdo", "Deïdo", TypeClient.SEMI_GROSSISTE, "COM-004", 4.0601, 9.7118),
        new Boutique("CLI-0016", "Alimentation Bonalembé", "+237677100116",
            "Rue Bonalembé", "Deïdo", TypeClient.DETAIL, "COM-004", 4.0623, 9.7095),
        new Boutique("CLI-0017", "Grande Surface Bonanjo", "+237677100117",
            "Boulevard de la République", "Bonanjo", TypeClient.DISTRIBUTEUR, "COM-004", 4.0447, 9.6889),
        new Boutique("CLI-0018", "Épicerie du Port", "+237677100118",
            "Avenue du Général de Gaulle", "Bonanjo", TypeClient.DETAIL, "COM-004", 4.0421, 9.6912),

        // ── Axes périurbains — COM-005 ─────────────────────────────────────
        new Boutique("CLI-0019", "Dépôt Nkongsamba Centre", "+237677100119",
            "Marché central Nkongsamba", "Nkongsamba", TypeClient.GROSSISTE, "COM-005", 4.9547, 9.9404),
        new Boutique("CLI-0020", "Alimentation Moungo", "+237677100120",
            "Route de Bafang", "Nkongsamba", TypeClient.DETAIL, "COM-005", 4.9581, 9.9372),
        new Boutique("CLI-0021", "Comptoir Édéa", "+237677100121",
            "Marché d'Édéa", "Édéa", TypeClient.SEMI_GROSSISTE, "COM-005", 3.7998, 10.1329),
        new Boutique("CLI-0022", "Boutique Sanaga", "+237677100122",
            "Quartier Bilalang", "Édéa", TypeClient.REVENDEUR, "COM-005", 3.8021, 10.1301)
    );

    @Override
    @Transactional
    public void seed() {
        int crees = 0;
        for (Boutique b : CLIENTS) {
            if (clientRepository.trouverParCode(b.code()).isPresent()) {
                continue;
            }
            clientRepository.sauvegarder(Client.creer(
                b.code(), b.nom(), b.telephone(), b.adresse(), b.localite(),
                b.type(), b.commercial(), b.latitude(), b.longitude()));
            crees++;
        }
        log.info("[SEED] Clientèle : {} client(s) créé(s), {} déjà présent(s)",
            crees, CLIENTS.size() - crees);
    }
}
