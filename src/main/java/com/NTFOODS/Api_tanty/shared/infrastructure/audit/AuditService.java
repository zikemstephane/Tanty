package com.NTFOODS.Api_tanty.shared.infrastructure.audit;

import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service applicatif — écriture dans le Journal d'Audit.
 *
 * Utilisé automatiquement par {@link AuditAspect} pour toutes les requêtes
 * REST, et peut être appelé explicitement par un use case pour enrichir une
 * entrée (ex : "impression d'une facture", "export d'un rapport").
 *
 * Chaque écriture s'exécute dans sa PROPRE transaction (REQUIRES_NEW) afin
 * que l'audit d'un échec métier soit conservé même si la transaction
 * appelante est annulée (rollback).
 */
@Service
public class AuditService {

    private final JournalAuditJpaRepository repository;

    public AuditService(JournalAuditJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enregistrer(String matricule, String role, TypeActionAudit typeAction, String module,
                             String ressource, String referenceEntite, String methodeHttp, String endpoint,
                             String details, boolean succes, String messageErreur, String adresseIp, long dureeMs) {
        JournalAuditJpaEntity entity = new JournalAuditJpaEntity();
        entity.setMatricule(matricule != null ? matricule : "ANONYME");
        entity.setRole(role);
        entity.setTypeAction(typeAction);
        entity.setModule(module);
        entity.setRessource(ressource);
        entity.setReferenceEntite(referenceEntite);
        entity.setMethodeHttp(methodeHttp);
        entity.setEndpoint(tronquer(endpoint, 300));
        entity.setDetails(tronquer(details, 500));
        entity.setSucces(succes);
        entity.setMessageErreur(tronquer(messageErreur, 500));
        entity.setAdresseIp(adresseIp);
        entity.setDureeMs(dureeMs);
        entity.setHorodatage(LocalDateTime.now());
        repository.save(entity);
    }

    /** Point d'entrée simplifié pour un enregistrement manuel (ex: impression, export). */
    public void tracer(String matricule, TypeActionAudit typeAction, String module, String ressource,
                        String referenceEntite, String details) {
        enregistrer(matricule, null, typeAction, module, ressource, referenceEntite,
            null, null, details, true, null, null, 0L);
    }

    private String tronquer(String valeur, int max) {
        if (valeur == null) return null;
        return valeur.length() > max ? valeur.substring(0, max - 3) + "..." : valeur;
    }
}
