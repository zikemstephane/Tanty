package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.LigneVenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.KpisDgResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObtenirKpisDgUseCase {

    private final VenteJpaRepository venteJpaRepository;
    private final UtilisateurJpaRepository utilisateurJpaRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final JournalCaisseRepository caisseRepository;
    private final VersementRepository versementRepository;
    private final DecaissementRepository decaissementRepository;

    public ObtenirKpisDgUseCase(VenteJpaRepository venteJpaRepository,
                                UtilisateurJpaRepository utilisateurJpaRepository,
                                NiveauStockRepository niveauStockRepository,
                                JournalCaisseRepository caisseRepository,
                                VersementRepository versementRepository,
                                DecaissementRepository decaissementRepository) {
        this.venteJpaRepository = venteJpaRepository;
        this.utilisateurJpaRepository = utilisateurJpaRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.caisseRepository = caisseRepository;
        this.versementRepository = versementRepository;
        this.decaissementRepository = decaissementRepository;
    }

    public KpisDgResponse executer() {
        LocalDate now = LocalDate.now();
        LocalDate debutMois = now.withDayOfMonth(1);
        LocalDate debutMoisN1 = debutMois.minusMonths(1);
        LocalDate finMoisN1 = debutMois.minusDays(1);
        LocalDate debutMoisN2 = debutMoisN1.withDayOfMonth(1).minusMonths(1);
        LocalDate finMoisN2 = debutMoisN1.withDayOfMonth(1).minusDays(1);

        List<VenteJpaEntity> ventesMois = venteJpaRepository.findByDateBetween(debutMois, now);
        List<VenteJpaEntity> ventesN1 = venteJpaRepository.findByDateBetween(debutMoisN1, finMoisN1);
        List<VenteJpaEntity> ventesN2 = venteJpaRepository.findByDateBetween(debutMoisN2, finMoisN2);

        double caTotal = calculerCa(ventesMois);
        double caN1 = calculerCa(ventesN1);
        double caN2 = calculerCa(ventesN2);
        long nbVentes = ventesMois.size();

        int commerciauxActifs = (int) utilisateurJpaRepository.findByRole(Role.COMMERCIAL)
                .stream().filter(u -> u.isActif()).count();

        double stockCentral = niveauStockRepository.trouverParType(TypeEntrepot.CENTRAL).stream()
            .mapToLong(NiveauStock::getQuantiteEnBase).sum();
        double stockTampon = niveauStockRepository.trouverParType(TypeEntrepot.TAMPON).stream()
            .mapToLong(NiveauStock::getQuantiteEnBase).sum();
        double stockMobile = niveauStockRepository.trouverParType(TypeEntrepot.MOBILE).stream()
            .mapToLong(NiveauStock::getQuantiteEnBase).sum();

        double soldeCaisse = caisseRepository.trouverParDate(now)
            .map(j -> j.getSoldeCourant()).orElse(0.0);
        double totalVersementsValides = versementRepository.listerParStatut(StatutVersement.VALIDE).stream()
            .mapToDouble(v -> v.getCashVerse()).sum();
        double totalDecaissementsExecutes = decaissementRepository
            .listerParStatut(StatutDecaissement.EXECUTE).stream()
            .mapToDouble(d -> d.getMontantFCFA()).sum();
        double tresorerie = soldeCaisse + totalVersementsValides - totalDecaissementsExecutes;

        double margeGlobale = caTotal * 0.30;

        return new KpisDgResponse(
            caTotal, caN1, caN2,
            nbVentes, commerciauxActifs,
            0, caTotal,
            margeGlobale, tresorerie, 0,
            stockCentral, stockTampon, stockMobile
        );
    }

    private double calculerCa(List<VenteJpaEntity> ventes) {
        return ventes.stream()
            .flatMap(v -> v.getLignes().stream())
            .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite() * (1.0 - l.getRemise() / 100.0))
            .sum();
    }
}
