package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.creer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionProductionRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — créer une réception de produits finis depuis la Production
 * (déclaration de lot → réception physique par le Gestionnaire).
 */
@Service
@Transactional
public class CreerReceptionProductionUseCase {

    @Value("${app.stock.seuil-ecart-reception-pourcentage:10.0}")
    private double seuilEcart;

    private final ReceptionRepository receptionRepository;
    private final EntrepotRepository entrepotRepository;
    private final ReceptionMapper receptionMapper;

    public CreerReceptionProductionUseCase(ReceptionRepository receptionRepository,
                                           EntrepotRepository entrepotRepository,
                                           ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.entrepotRepository = entrepotRepository;
        this.receptionMapper = receptionMapper;
    }

    public ReceptionResponse executer(CreerReceptionProductionRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.GESTIONNAIRE_STOCK && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Gestionnaire de Stock peut réceptionner un lot de production");
        }
        String entrepotCible = resoudreEntrepotCentral(request.codeEntrepotCible());
        String numero = genererNumero();

        Reception reception = Reception.creerProduction(numero, request.numeroLotProduction(),
            entrepotCible, seuilEcart, LocalDate.now());

        for (var l : request.lignes()) {
            reception.ajouterLigne(l.codeProduit(), l.sku(), l.quantiteAttendue(), l.quantiteRecue(), null);
        }
        return receptionMapper.versResponse(receptionRepository.sauvegarder(reception));
    }

    private String resoudreEntrepotCentral(String codeFourni) {
        if (codeFourni != null && !codeFourni.isBlank()) return codeFourni.trim().toUpperCase();
        return entrepotRepository.trouverUniqueParType(TypeEntrepot.CENTRAL)
            .map(e -> e.getCode())
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt central configuré"));
    }

    private String genererNumero() {
        int annee = LocalDate.now().getYear();
        long sequence = receptionRepository.compterParAnnee(annee) + 1;
        return String.format("BR-%d-%04d", annee, sequence);
    }
}
