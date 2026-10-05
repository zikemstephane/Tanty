package com.NTFOODS.Api_tanty.modules.commercial.application.vente.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.vente.service.ReglesAvarieService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Use Case — Déclaration de transaction non marchande (§12).
 *
 * <p>Dons, dotations, avaries et échanges n'encaissent rien mais font varier le
 * stock mobile du commercial. Cette variation est désormais réellement écrite :
 * jusqu'ici seule une {@code Vente} était créée, si bien que la réconciliation
 * énoncée par ce même use case — {@code StockInitial − Ventes − Dons − Dotations
 * − Avaries = Retour Physique} — ne pouvait jamais tomber juste.</p>
 *
 * <p>Où va la marchandise, selon sa nature :</p>
 * <ul>
 *   <li><strong>Avarie, don, dotation</strong> — sortie sèche du stock mobile. Rien
 *       n'est recrédité : un produit avarié ne doit pas redevenir vendable, et la
 *       marchandise donnée ne revient pas. Le mouvement reste tracé.</li>
 *   <li><strong>Échange</strong> — le produit remis au client sort du stock mobile,
 *       le produit repris y entre. Les deux étant de même nature, le repris demeure
 *       vendable.</li>
 * </ul>
 *
 * <p>La preuve photographique obligatoire est arbitrée par l'agrégat
 * {@link ReglesAvarie}, seul porteur de la règle. Le seuil « 5 % du lot » codé ici
 * auparavant ne pouvait de toute façon jamais se déclencher : le client envoyait
 * systématiquement un pourcentage nul.</p>
 */
@Service
@Transactional
public class DeclencherTransactionNonMarchandeUseCase {

    private final VenteRepository venteRepository;
    private final ReglesAvarieService reglesAvarieService;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final GenerateurNumeroMetier generateurNumero;

    public DeclencherTransactionNonMarchandeUseCase(VenteRepository venteRepository,
                                                    ReglesAvarieService reglesAvarieService,
                                                    EntrepotRepository entrepotRepository,
                                                    NiveauStockRepository niveauStockRepository,
                                                    MouvementStockRepository mouvementRepository,
                                                    ProduitRepository produitRepository,
                                                    GenerateurNumeroMetier generateurNumero) {
        this.venteRepository = venteRepository;
        this.reglesAvarieService = reglesAvarieService;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
        this.generateurNumero = generateurNumero;
    }

    public Vente executer(NonMarchandeRequest req) {
        Objects.requireNonNull(req.typeVente(), "Type de transaction obligatoire");
        if (req.typeVente() == TypeVente.COMPTANT || req.typeVente() == TypeVente.CREDIT) {
            throw new IllegalArgumentException(
                "Ce use case ne traite que les transactions non marchandes (AVARIE, DON, DOTATION, ECHANGE)");
        }
        if (req.motif() == null || req.motif().isBlank()) {
            throw new IllegalArgumentException("Motif obligatoire pour une transaction non marchande");
        }
        if (req.quantite() <= 0) {
            throw new IllegalArgumentException("La quantité déclarée doit être positive");
        }
        // Exigé par LigneVente, qui échouait jusqu'ici sur un NullPointerException
        // opaque plutôt que d'énoncer ce qui manquait.
        if (req.conditionnement() == null || req.conditionnement().isBlank()) {
            throw new IllegalArgumentException("Le conditionnement déclaré est obligatoire");
        }

        LocalDate date = req.date() != null ? req.date() : LocalDate.now();

        // Le conditionnement est résolu avant la règle de preuve : le seuil porte
        // sur les unités de base, et non sur le nombre de colis saisis. Sans cette
        // conversion, une avarie de dix cartons échappait à la preuve dès lors
        // qu'elle était déclarée « 10 » plutôt que « 250 ».
        Conditionnement remis = exigerConditionnement(req.codeProduit(), req.conditionnement());
        long quantiteEnBase = req.quantite() * remis.getFacteurConversionVersBase();

        if (req.typeVente() == TypeVente.AVARIE) {
            ReglesAvarie regles = reglesAvarieService.reglesApplicablesA(date);
            boolean preuveManquante = req.photoJustificative() == null || req.photoJustificative().isBlank();
            if (regles.photoObligatoirePour(quantiteEnBase) && preuveManquante) {
                throw new RegleMetierException("Une photo de preuve est obligatoire à partir de "
                    + regles.getQuantiteSeuilPhotoObligatoire() + " unité(s) de base en avarie : "
                    + req.quantite() + " × « " + remis.getLibelle() + " » en représente "
                    + quantiteEnBase + " (" + regles.getReference() + ")");
            }
        }

        String numero = generateurNumero.genererNumero(FamilleNumero.TRANSACTION_NON_MARCHANDE, date);
        appliquerAuStockMobile(req, remis, date, numero);

        LigneVente ligne = new LigneVente(req.codeProduit(), req.designationProduit(),
            req.conditionnement(), req.quantite(), 0.0, 0.0);

        Vente vente = Vente.creer(numero, null, req.matriculeCommercial(), req.codeClient(),
            date, ModeReglement.NON_APPLICABLE, req.typeVente(), List.of(ligne),
            req.photoJustificative());

        return venteRepository.sauvegarder(vente);
    }

    /**
     * Écrit la variation de stock correspondant à la nature de la transaction.
     *
     * <p>Exécuté avant l'enregistrement de la transaction : un stock mobile
     * insuffisant doit faire échouer la déclaration entière, et non laisser une
     * trace comptable sans contrepartie physique.</p>
     */
    private void appliquerAuStockMobile(NonMarchandeRequest req, Conditionnement remis,
                                        LocalDate date, String referenceDocument) {
        String mobile = entrepotRepository.trouverMobileParCommercial(req.matriculeCommercial())
            .map(Entrepot::getCode)
            .orElseThrow(() -> new EntiteNonTrouveeException(
                "Aucun stock mobile pour le commercial " + req.matriculeCommercial()));

        // Le conditionnement remis est déjà résolu par l'appelant, qui en a besoin
        // pour la règle de preuve. Celui de la contrepartie l'est ici, mais avant
        // toute écriture : un échange dont la reprise n'est pas chiffrable ne doit
        // pas laisser derrière lui une sortie de stock déjà consommée.
        Conditionnement repris = null;
        if (req.typeVente() == TypeVente.ECHANGE) {
            if (req.codeProduitEchange() == null || req.codeProduitEchange().isBlank()) {
                throw new RegleMetierException(
                    "Le produit repris au client est obligatoire pour un échange");
            }
            repris = conditionnementEquivalent(req.codeProduitEchange(), remis);
        }

        sortir(mobile, req.codeProduit(), remis.getSku(),
            req.quantite() * remis.getFacteurConversionVersBase(),
            typeMouvementSortie(req.typeVente()), req, date, referenceDocument);

        if (repris != null) {
            entrer(mobile, req.codeProduitEchange(), repris.getSku(),
                req.quantite() * repris.getFacteurConversionVersBase(),
                TypeMouvement.ECHANGE_REPRISE_CLIENT, req, date, referenceDocument);
        }
    }

    private TypeMouvement typeMouvementSortie(TypeVente type) {
        return switch (type) {
            case AVARIE -> TypeMouvement.SORTIE_AVARIE;
            case DON -> TypeMouvement.SORTIE_DON;
            case DOTATION -> TypeMouvement.SORTIE_DOTATION;
            case ECHANGE -> TypeMouvement.ECHANGE_REMISE_CLIENT;
            default -> throw new IllegalArgumentException("Type non marchand inattendu : " + type);
        };
    }

    private void sortir(String entrepot, String codeProduit, String sku, long quantiteEnBase,
                        TypeMouvement type, NonMarchandeRequest req, LocalDate date, String referenceDocument) {
        NiveauStock niveau = niveauStockRepository.trouver(codeProduit, entrepot)
            .orElseThrow(() -> new EntiteNonTrouveeException(
                "Aucun stock mobile pour le produit " + codeProduit));
        niveau.sortir(quantiteEnBase);
        niveauStockRepository.sauvegarder(niveau);
        tracer(type, codeProduit, sku, quantiteEnBase, entrepot, null, req, date, referenceDocument);
    }

    private void entrer(String entrepot, String codeProduit, String sku, long quantiteEnBase,
                        TypeMouvement type, NonMarchandeRequest req, LocalDate date, String referenceDocument) {
        NiveauStock niveau = niveauStockRepository.trouver(codeProduit, entrepot)
            .orElseGet(() -> NiveauStock.creer(codeProduit, entrepot, TypeEntrepot.MOBILE));
        niveau.entrer(quantiteEnBase);
        niveauStockRepository.sauvegarder(niveau);
        tracer(type, codeProduit, sku, quantiteEnBase, null, entrepot, req, date, referenceDocument);
    }

    private void tracer(TypeMouvement type, String codeProduit, String sku, long quantiteEnBase,
                        String source, String cible, NonMarchandeRequest req, LocalDate date,
                        String referenceDocument) {
        mouvementRepository.enregistrer(MouvementStock.enregistrer(
            generateurNumero.genererNumero(FamilleNumero.MOUVEMENT_STOCK, date),
            type, codeProduit, sku, quantiteEnBase, source, cible,
            req.matriculeCommercial(), referenceDocument, req.motif()));
    }

    /**
     * Conditionnement déclaré, exigé au catalogue du produit concerné.
     *
     * <p>Le repli silencieux « conditionnement inconnu → quantité inchangée » n'est
     * pas tenable ici : le conditionnement vient directement de la saisie terrain.
     * Un libellé envoyé à la place d'un SKU faisait déduire un carton de 25 comme
     * une seule unité, sans la moindre trace. Mieux vaut refuser la déclaration que
     * fausser le stock.</p>
     */
    private Conditionnement exigerConditionnement(String codeProduit, String sku) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> new EntiteNonTrouveeException(
                "Produit inconnu au catalogue : " + codeProduit));
        return produit.trouverParSku(sku)
            .orElseThrow(() -> new RegleMetierException(
                "Conditionnement « " + sku + " » inconnu pour le produit " + codeProduit
                    + ". Déclarez l'un des conditionnements du catalogue : "
                    + produit.getConditionnements().stream()
                        .map(Conditionnement::getSku).collect(Collectors.joining(", "))));
    }

    /**
     * Conditionnement du produit repris, au même niveau que celui remis.
     *
     * <p>Un échange est une remise à l'identique : ce qui sort en carton revient en
     * carton. Le SKU, lui, est propre à chaque produit — appliquer celui du produit
     * remis au produit repris ne désignait rien et laissait la quantité brute
     * entrer en stock.</p>
     */
    private Conditionnement conditionnementEquivalent(String codeProduitRepris, Conditionnement remis) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduitRepris))
            .orElseThrow(() -> new EntiteNonTrouveeException(
                "Produit repris inconnu au catalogue : " + codeProduitRepris));
        try {
            return produit.trouverParNiveau(remis.getNiveau());
        } catch (IllegalArgumentException absent) {
            throw new RegleMetierException(
                "Le produit repris " + codeProduitRepris + " n'existe pas en "
                    + remis.getNiveau().getLibelle() + " : l'échange ne peut pas être chiffré à l'identique");
        }
    }

    public record NonMarchandeRequest(
        TypeVente typeVente,
        String matriculeCommercial,
        String codeClient,
        String codeProduit,
        String designationProduit,
        String conditionnement,
        int quantite,
        LocalDate date,
        String motif,
        String photoJustificative,
        /** Produit repris au client ; renseigné pour un ÉCHANGE uniquement. */
        String codeProduitEchange
    ) {}
}
