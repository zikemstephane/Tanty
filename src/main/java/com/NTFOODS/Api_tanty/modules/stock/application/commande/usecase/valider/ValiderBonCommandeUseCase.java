package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.GenererPPHDepuisBCUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification.BonCommandeSpecification;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Use Case — valider un Bon de Commande (BROUILLON → VALIDE).
 */
@Service
@Transactional
public class ValiderBonCommandeUseCase {

    private static final Logger log = LoggerFactory.getLogger(ValiderBonCommandeUseCase.class);

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;
    private final EventPublisher eventPublisher;
    private final GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase;
    private final TransactionTemplate transactionTemplateNouvelle;

    public ValiderBonCommandeUseCase(BonCommandeRepository bonCommandeRepository,
                                     BonCommandeMapper bonCommandeMapper, EventPublisher eventPublisher,
                                     GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase,
                                     PlatformTransactionManager transactionManager) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
        this.eventPublisher = eventPublisher;
        this.genererPPHDepuisBCUseCase = genererPPHDepuisBCUseCase;

        // Template dédié : force une transaction PHYSIQUE indépendante et
        // garantit qu'elle est bien committée avant de rendre la main,
        // même lorsqu'on est appelé depuis un callback afterCommit() où
        // la propagation @Transactional standard n'est pas fiable.
        this.transactionTemplateNouvelle = new TransactionTemplate(transactionManager);
        this.transactionTemplateNouvelle.setPropagationBehavior(
            org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public BonCommandeResponse executer(String numero, Utilisateur acteur) {
        log.info("[VALIDER-BC] ▶ Début validation — BC={} acteur={} rôle={}",
            numero, acteur.getMatricule().getValeur(), acteur.getRole().getLibelle());

        if (!BonCommandeSpecification.peutValider(acteur.getRole())) {
            log.warn("[VALIDER-BC] ✗ Accès refusé — rôle {} insuffisant pour valider un BC", acteur.getRole().getLibelle());
            throw new AccesRefuseException("Le rôle '" + acteur.getRole().getLibelle()
                + "' ne peut pas valider un BC. Seul le Chef de Production valide les BC.");
        }

        BonCommande bc = bonCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> {
                log.error("[VALIDER-BC] ✗ BC introuvable en base : {}", numero);
                return EntiteNonTrouveeException.de("Bon de commande", numero);
            });

        log.info("[VALIDER-BC] BC trouvé — statut actuel={} lignes={} fournisseur={}",
            bc.getStatut(), bc.getLignes().size(), bc.getCodeFournisseur());

        bc.valider();
        log.info("[VALIDER-BC] Statut BC basculé → VALIDE");

        BonCommande sauvegarde = bonCommandeRepository.sauvegarder(bc);
        log.info("[VALIDER-BC] ✓ BC sauvegardé en base (id={})", sauvegarde.getId());

        bc.getDomainEvents().forEach(eventPublisher::publish);
        bc.clearDomainEvents();

        // IMPORTANT : la génération du PPH est informative et ne doit jamais
        // pouvoir faire échouer la validation du BC. On reporte donc cet
        // appel APRÈS le commit réussi de la transaction de validation,
        // via une synchronisation transactionnelle.
        //
        // À l'intérieur du callback afterCommit(), on ouvre EXPLICITEMENT
        // une nouvelle transaction physique (PROPAGATION_REQUIRES_NEW) via
        // TransactionTemplate plutôt que de compter sur le @Transactional
        // de GenererPPHDepuisBCUseCase : dans le contexte d'un callback
        // afterCommit(), Spring ne garantit pas l'ouverture/le commit
        // fiable d'une transaction pilotée uniquement par annotation,
        // ce qui provoquait un save() silencieusement non persisté.
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            log.info("[VALIDER-BC] Synchronisation transactionnelle active — génération PPH programmée afterCommit");
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("[VALIDER-BC] afterCommit ▶ lancement GenererPPHDepuisBCUseCase pour BC={}", numero);
                    try {
                        transactionTemplateNouvelle.executeWithoutResult(status ->
                            genererPPHDepuisBCUseCase.executer(sauvegarde, acteur));
                        log.info("[VALIDER-BC] afterCommit ✓ PPH généré avec succès pour BC={}", numero);
                    } catch (Exception e) {
                        log.error("[VALIDER-BC] afterCommit ✗ Génération PPH échouée pour BC={} — cause : {} — {}",
                            numero, e.getClass().getSimpleName(), e.getMessage(), e);
                    }
                }
            });
        } else {
            log.warn("[VALIDER-BC] Aucune synchronisation transactionnelle — génération PPH en mode direct (fallback)");
            try {
                transactionTemplateNouvelle.executeWithoutResult(status ->
                    genererPPHDepuisBCUseCase.executer(sauvegarde, acteur));
                log.info("[VALIDER-BC] ✓ PPH généré (mode direct) pour BC={}", numero);
            } catch (Exception e) {
                log.error("[VALIDER-BC] ✗ Génération PPH échouée (mode direct) pour BC={} — cause : {} — {}",
                    numero, e.getClass().getSimpleName(), e.getMessage(), e);
            }
        }

        log.info("[VALIDER-BC] ◀ Validation terminée — réponse renvoyée pour BC={}", numero);
        return bonCommandeMapper.versResponse(sauvegarde);
    }
}