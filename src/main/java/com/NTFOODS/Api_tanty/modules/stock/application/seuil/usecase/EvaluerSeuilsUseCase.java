package com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.mapper.AlerteMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.model.SeuilStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case — évalue les niveaux de stock par rapport aux seuils et génère les
 * alertes actionnables (central/tampon sous seuil). Idempotent : pas de doublon
 * tant qu'une alerte active existe pour (type, produit, entrepôt).
 */
@Service
@Transactional
public class EvaluerSeuilsUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final SeuilStockRepository seuilRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final AlerteRepository alerteRepository;
    private final AlerteMapper alerteMapper;
    private final EventPublisher eventPublisher;

    public EvaluerSeuilsUseCase(SeuilStockRepository seuilRepository, NiveauStockRepository niveauStockRepository,
                                AlerteRepository alerteRepository, AlerteMapper alerteMapper,
                                EventPublisher eventPublisher) {
        this.seuilRepository = seuilRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.alerteRepository = alerteRepository;
        this.alerteMapper = alerteMapper;
        this.eventPublisher = eventPublisher;
    }

    public List<AlerteResponse> executer() {
        List<Alerte> creees = new ArrayList<>();

        for (SeuilStock seuil : seuilRepository.trouverTous()) {
            if (!seuil.isActif()) continue;
            List<NiveauStock> niveaux = niveauStockRepository.trouverParProduit(seuil.getCodeProduit()).stream()
                .filter(n -> n.getTypeEntrepot() == seuil.getTypeEntrepot())
                .toList();

            for (NiveauStock niveau : niveaux) {
                if (!seuil.estFranchiPar(niveau.getQuantiteEnBase())) continue;
                TypeAlerte type = seuil.getTypeEntrepot() == TypeEntrepot.TAMPON
                    ? TypeAlerte.TAMPON_SOUS_SEUIL : TypeAlerte.CENTRAL_SOUS_SEUIL;

                if (alerteRepository.existeAlerteActive(type, niveau.getCodeProduit(), niveau.getCodeEntrepot())) continue;

                Alerte alerte = Alerte.creer(genererReference(), type,
                    String.format("%s : produit %s à %d unités (seuil %d) dans %s",
                        type.getLibelle(), niveau.getCodeProduit(), niveau.getQuantiteEnBase(),
                        seuil.getSeuilReapprovisionnement(), niveau.getCodeEntrepot()),
                    niveau.getCodeProduit(), niveau.getCodeEntrepot(), null);

                Alerte sauvegarde = alerteRepository.sauvegarder(alerte);
                alerte.getDomainEvents().forEach(eventPublisher::publish);
                alerte.clearDomainEvents();
                creees.add(sauvegarde);
            }
        }
        return creees.stream().map(alerteMapper::versResponse).toList();
    }

    private String genererReference() {
        long sequence = alerteRepository.compter() + 1;
        return "ALR-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
