package com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.declarer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.EnregistrerRetourRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.mapper.RetourMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Use Case — déclaration des invendus par le commercial en fin de tournée.
 *
 * <p><strong>Aucun stock n'est déplacé.</strong> Les invendus ne repartent pas au
 * dépôt : ils restent dans le stock mobile du commercial. Ce qui était fait ici
 * auparavant — débiter le mobile et créditer le tampon — recréditait le stock
 * vendable du dépôt sur la seule saisie d'une déclaration, alors que la
 * marchandise n'y est jamais revenue.</p>
 *
 * <p>La déclaration n'engage donc que son auteur ; c'est le comptage du
 * gestionnaire qui l'arrête ({@link com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.compter.CompterInvendusUseCase}).</p>
 */
@Service
@Transactional
public class DeclarerInvendusUseCase {

    private final BonRetourRepository retourRepository;
    private final GenerateurNumeroMetier generateurNumero;
    private final RetourMapper retourMapper;

    public DeclarerInvendusUseCase(BonRetourRepository retourRepository,
                                   GenerateurNumeroMetier generateurNumero,
                                   RetourMapper retourMapper) {
        this.retourRepository = retourRepository;
        this.generateurNumero = generateurNumero;
        this.retourMapper = retourMapper;
    }

    public RetourResponse executer(EnregistrerRetourRequest request, Utilisateur acteur) {
        String matriculeActeur = acteur.getMatricule().getValeur();
        String matriculeCommercial = request.matriculeCommercial();

        // Un commercial ne déclare que ses propres invendus : la déclaration vaut
        // engagement de son auteur, elle ne peut pas être portée au nom d'un tiers.
        if (acteur.getRole() == Role.COMMERCIAL && !matriculeActeur.equals(matriculeCommercial)) {
            throw new AccesRefuseException(
                "Un commercial ne peut déclarer que ses propres invendus");
        }

        LocalDate date = LocalDate.now();
        BonRetour constat = BonRetour.declarer(
            generateurNumero.genererNumero(FamilleNumero.CONSTAT_INVENDUS, date),
            matriculeCommercial, date);
        request.lignes().forEach(l -> constat.ajouterLigne(l.codeProduit(), l.sku(), l.quantite(), l.motif()));
        constat.exigerNonVide();

        return retourMapper.versResponse(retourRepository.sauvegarder(constat));
    }
}
