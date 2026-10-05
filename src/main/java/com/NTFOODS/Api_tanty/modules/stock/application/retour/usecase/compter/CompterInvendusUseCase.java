package com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.compter;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.CompterInvendusRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.mapper.RetourMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Use Case — comptage des invendus par le gestionnaire de stock.
 *
 * <p>Le gestionnaire constate ce qu'il a réellement sous les yeux. La quantité
 * déclarée par le commercial est conservée intacte à côté : c'est l'écart entre
 * les deux qui constitue l'information recherchée.</p>
 *
 * <p>Comme la déclaration, le comptage ne déplace aucun stock — les invendus
 * restent au camion. Seules les avaries et les échanges font varier le stock, et
 * ils sont déclarés comme transactions non marchandes.</p>
 */
@Service
@Transactional
public class CompterInvendusUseCase {

    private final BonRetourRepository retourRepository;
    private final RetourMapper retourMapper;

    public CompterInvendusUseCase(BonRetourRepository retourRepository, RetourMapper retourMapper) {
        this.retourRepository = retourRepository;
        this.retourMapper = retourMapper;
    }

    public RetourResponse executer(String numero, CompterInvendusRequest request, Utilisateur acteur) {
        BonRetour constat = retourRepository.trouverParNumero(numero)
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun constat d'invendus " + numero));

        Map<String, Long> quantites = new LinkedHashMap<>();
        for (CompterInvendusRequest.LigneComptageRequest ligne : request.lignes()) {
            String code = ligne.codeProduit().trim().toUpperCase();
            if (quantites.put(code, ligne.quantiteComptee()) != null) {
                throw new RegleMetierException("Le produit " + code + " est compté deux fois");
            }
        }

        constat.compter(quantites, acteur.getMatricule().getValeur());
        return retourMapper.versResponse(retourRepository.sauvegarder(constat));
    }
}
