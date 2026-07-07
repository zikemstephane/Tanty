package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.soumettre;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request.SoumettreDotationRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — soumission d'une dotation par un commercial.
 * Date d'effet = lendemain (gérée par l'agrégat).
 */
@Service
@Transactional
public class SoumettreDotationUseCase {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;
    private final EventPublisher eventPublisher;

    public SoumettreDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper,
                                    EventPublisher eventPublisher) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
        this.eventPublisher = eventPublisher;
    }

    public DotationResponse executer(SoumettreDotationRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.COMMERCIAL && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul un commercial peut soumettre une dotation");
        }
        String matricule = acteur.getMatricule().getValeur();
        Dotation dotation = Dotation.soumettre(genererNumero(), matricule, LocalDate.now());
        request.lignes().forEach(l -> dotation.ajouterLigne(l.codeProduit(), l.sku(), l.quantiteDemandee()));

        Dotation sauvegarde = dotationRepository.sauvegarder(dotation);
        dotation.getDomainEvents().forEach(eventPublisher::publish);
        dotation.clearDomainEvents();
        return dotationMapper.versResponse(sauvegarde);
    }

    private String genererNumero() {
        int annee = LocalDate.now().getYear();
        long sequence = dotationRepository.compterParAnnee(annee) + 1;
        return String.format("BD-%d-%04d", annee, sequence);
    }
}
