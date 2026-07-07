package com.NTFOODS.Api_tanty.modules.administration.application;

import com.NTFOODS.Api_tanty.modules.administration.application.dto.ParametresResponse;
import com.NTFOODS.Api_tanty.modules.administration.infrastructure.persistence.ReferentielStringJpaEntity;
import com.NTFOODS.Api_tanty.modules.administration.infrastructure.persistence.ReferentielStringJpaRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.parametres.ParametresSystemeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParametresAdminService {

    private static final String CLE_TVA = "TAUX_TVA";
    private static final String CLE_PRIME = "MONTANT_PRIME_HEBDO";
    private static final String CLE_SEUIL_STOCK = "SEUIL_ALERTE_STOCK";

    private final ParametresSystemeJpaRepository parametresRepo;
    private final ReferentielStringJpaRepository referentielRepo;

    public ParametresAdminService(ParametresSystemeJpaRepository parametresRepo,
                                   ReferentielStringJpaRepository referentielRepo) {
        this.parametresRepo = parametresRepo;
        this.referentielRepo = referentielRepo;
    }

    @Transactional(readOnly = true)
    public ParametresResponse consulter() {
        double tva = getValeur(CLE_TVA, 19.25);
        double prime = getValeur(CLE_PRIME, 12000);
        double seuil = getValeur(CLE_SEUIL_STOCK, 20);

        List<String> zones = listerReferentiel("ZONE");
        List<String> gammes = listerReferentiel("GAMME");
        List<String> villes = listerReferentiel("VILLE");

        return new ParametresResponse(tva, prime, seuil, zones, gammes, villes);
    }

    public ParametresResponse modifier(double tvaTaux, double primeMontant, double alerteStockSeuil) {
        setValeur(CLE_TVA, tvaTaux);
        setValeur(CLE_PRIME, primeMontant);
        setValeur(CLE_SEUIL_STOCK, alerteStockSeuil);
        return consulter();
    }

    public ParametresResponse ajouterReferentiel(String categorie, String valeur) {
        if (!referentielRepo.existsByCategorieAndValeur(categorie, valeur)) {
            ReferentielStringJpaEntity e = new ReferentielStringJpaEntity();
            e.setCategorie(categorie);
            e.setValeur(valeur);
            referentielRepo.save(e);
        }
        return consulter();
    }

    public ParametresResponse supprimerReferentiel(String categorie, String valeur) {
        referentielRepo.deleteByCategorieAndValeur(categorie, valeur);
        return consulter();
    }

    private double getValeur(String cle, double defaut) {
        return parametresRepo.findByCle(cle)
            .map(p -> p.getValeurNumerique())
            .orElse(defaut);
    }

    private void setValeur(String cle, double valeur) {
        parametresRepo.findByCle(cle).ifPresent(p -> {
            p.setValeurNumerique(valeur);
            parametresRepo.save(p);
        });
    }

    private List<String> listerReferentiel(String categorie) {
        return referentielRepo.findByCategorieOrderByValeurAsc(categorie).stream()
            .map(ReferentielStringJpaEntity::getValeur).toList();
    }
}
