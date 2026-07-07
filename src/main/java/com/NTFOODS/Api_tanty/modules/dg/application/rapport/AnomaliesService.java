package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.audit.JournalAuditJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service applicatif — Détection d'anomalies transversale (Pilier 3 :
 * Surveillance et Sécurité Anti-Forçage). Rassemble en une seule vue les
 * signaux à risque déjà détectés module par module : écarts financiers
 * (versements), stocks mobiles anormaux, écarts de réception, et
 * comportements suspects tracés dans le Journal d'Audit (annulations /
 * modifications répétées).
 */
@Service
@Transactional(readOnly = true)
public class AnomaliesService {

    private final VersementRepository versementRepository;
    private final AlerteRepository alerteRepository;
    private final JournalAuditJpaRepository journalAuditJpaRepository;

    public AnomaliesService(VersementRepository versementRepository, AlerteRepository alerteRepository,
                             JournalAuditJpaRepository journalAuditJpaRepository) {
        this.versementRepository = versementRepository;
        this.alerteRepository = alerteRepository;
        this.journalAuditJpaRepository = journalAuditJpaRepository;
    }

    public List<AnomalieResponse> detecter() {
        List<AnomalieResponse> resultats = new ArrayList<>();

        versementRepository.listerAlertesRouges().forEach(v ->
            resultats.add(new AnomalieResponse("ALERTE_ROUGE_VERSEMENT", v.getReferenceVersement(),
                "Écart cash déclaré vs CA — " + v.getMatriculeCommercial() + " (" + v.getDate() + ")",
                v.getMatriculeCommercial())));

        alerteRepository.trouverParStatut(StatutAlerte.NOUVELLE).stream()
            .filter(a -> a.getType() == TypeAlerte.STOCK_MOBILE_ANORMAL || a.getType() == TypeAlerte.ECART_RECEPTION)
            .forEach(a -> resultats.add(new AnomalieResponse(a.getType().name(), a.getReference(),
                a.getMessage(), a.getReferenceLiee())));

        journalAuditJpaRepository.detecterActivitesSuspectes(LocalDateTime.now().minusHours(24), 5).forEach(p ->
            resultats.add(new AnomalieResponse("ACTIVITE_SUSPECTE", p.getRessource(),
                p.getOccurrences() + " annulation(s)/modification(s) répétée(s) en 24h sur " + p.getRessource(),
                p.getMatricule())));

        return resultats;
    }
}
