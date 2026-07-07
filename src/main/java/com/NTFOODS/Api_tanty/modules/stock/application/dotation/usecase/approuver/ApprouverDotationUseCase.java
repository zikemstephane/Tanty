package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.approuver;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.service.VerificationChargementService;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.LigneDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — approbation d'une dotation. Pour chaque ligne approuvée :
 * débit du stock tampon, crédit du stock mobile du commercial, mouvement SORTIE_DOTATION.
 * Le stock mobile est l'entrepôt MOBILE-{matricule}, créé à la volée si nécessaire.
 *
 * DOUBLE SIGNATURE POUR LE CHARGEMENT (Pilier 2) : avant toute libération physique,
 * {@link VerificationChargementService} vérifie que la secrétaire (versement du
 * cash de la veille) ET le comptable (validité de la commande) ont bien signé.
 * Sans ces deux signatures, l'approbation est bloquée.
 */
@Service
@Transactional
public class ApprouverDotationUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DotationRepository dotationRepository;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final DotationMapper dotationMapper;
    private final EventPublisher eventPublisher;
    private final VerificationChargementService verificationChargementService;

    public ApprouverDotationUseCase(DotationRepository dotationRepository, EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository, ProduitRepository produitRepository,
                                    DotationMapper dotationMapper, EventPublisher eventPublisher,
                                    VerificationChargementService verificationChargementService) {
        this.dotationRepository = dotationRepository;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
        this.dotationMapper = dotationMapper;
        this.eventPublisher = eventPublisher;
        this.verificationChargementService = verificationChargementService;
    }

    public DotationResponse executer(String numero, Utilisateur acteur) {
        Dotation dotation = dotationRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Dotation", numero));

        verificationChargementService.verifierDoubleSignature(
            dotation.getMatriculeCommercial(), dotation.getDateDotation(), dotation.getDateEffet());

        dotation.approuver(acteur.getMatricule().getValeur(), acteur.getRole());

        String tampon = entrepotRepository.trouverUniqueParType(TypeEntrepot.TAMPON)
            .map(Entrepot::getCode)
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt tampon configuré"));
        String mobile = resoudreEntrepotMobile(dotation.getMatriculeCommercial());

        for (LigneDotation ligne : dotation.getLignes()) {
            if (ligne.getQuantiteApprouvee() <= 0) continue;
            long base = convertirEnBase(ligne.getCodeProduit(), ligne.getSku(), ligne.getQuantiteApprouvee());

            NiveauStock niveauTampon = niveauStockRepository.trouver(ligne.getCodeProduit(), tampon)
                .orElseThrow(() -> new EntiteNonTrouveeException(
                    "Aucun stock tampon pour le produit " + ligne.getCodeProduit()));
            NiveauStock niveauMobile = niveauStockRepository.trouver(ligne.getCodeProduit(), mobile)
                .orElseGet(() -> NiveauStock.creer(ligne.getCodeProduit(), mobile, TypeEntrepot.MOBILE));

            niveauTampon.sortir(base);
            niveauMobile.entrer(base);

            MouvementStock mouvement = MouvementStock.enregistrer(genererReferenceMouvement(),
                TypeMouvement.SORTIE_DOTATION, ligne.getCodeProduit(), ligne.getSku(), base,
                tampon, mobile, acteur.getMatricule().getValeur(), dotation.getNumero(),
                "Dotation commerciale " + dotation.getMatriculeCommercial());

            niveauStockRepository.sauvegarder(niveauTampon);
            niveauStockRepository.sauvegarder(niveauMobile);
            mouvementRepository.enregistrer(mouvement);
        }

        Dotation sauvegarde = dotationRepository.sauvegarder(dotation);
        dotation.getDomainEvents().forEach(eventPublisher::publish);
        dotation.clearDomainEvents();
        return dotationMapper.versResponse(sauvegarde);
    }

    private String resoudreEntrepotMobile(String matriculeCommercial) {
        return entrepotRepository.trouverMobileParCommercial(matriculeCommercial)
            .map(Entrepot::getCode)
            .orElseGet(() -> {
                Entrepot mobile = entrepotRepository.sauvegarder(Entrepot.creerMobile(matriculeCommercial));
                return mobile.getCode();
            });
    }

    private long convertirEnBase(String codeProduit, String sku, long quantite) {
        if (sku == null || sku.isBlank()) return quantite;
        return produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .flatMap(p -> p.trouverParSku(sku))
            .map(c -> quantite * c.getFacteurConversionVersBase())
            .orElse(quantite);
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
