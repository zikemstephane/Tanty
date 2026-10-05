package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.SourceReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.LigneReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.LigneReceptionJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa.ReceptionJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReceptionPersistenceMapper {

    public ReceptionJpaEntity versEntite(Reception r) {
        ReceptionJpaEntity entite = ReceptionJpaEntity.builder()
            .id(r.getId()).numero(r.getNumero()).source(r.getSource().name())
            .categorie(r.getCategorie().name()).codeFournisseur(r.getCodeFournisseur())
            .numeroBonCommande(r.getNumeroBonCommande()).numeroLotProduction(r.getNumeroLotProduction())
            .codeEntrepotCible(r.getCodeEntrepotCible()).statut(r.getStatut().name())
            .matriculeValidateur1(r.getMatriculeValidateur1()).matriculeValidateur2(r.getMatriculeValidateur2())
            .notesValidation1(r.getNotesValidation1()).notesValidation2(r.getNotesValidation2())
            .dateValidation1(r.getDateValidation1()).dateValidation2(r.getDateValidation2())
            .motifRejet(r.getMotifRejet()).seuilEcartPourcentage(r.getSeuilEcartPourcentage())
            .dateReception(r.getDateReception()).dateCreation(r.getDateCreation())
            .dateModification(r.getDateModification()).version(r.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneReceptionJpaEntity> lignes = r.getLignes().stream()
            .map(l -> versLigneEntite(l, entite)).toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    private LigneReceptionJpaEntity versLigneEntite(LigneReception l, ReceptionJpaEntity parent) {
        return LigneReceptionJpaEntity.builder()
            .id(l.getId()).reception(parent).codeProduit(l.getCodeProduit()).sku(l.getSku())
            .quantiteAttendue(l.getQuantiteAttendue()).quantiteRecue(l.getQuantiteRecue())
            .idLigneBC(l.getIdLigneBC())
            .build();
    }

    public Reception versDomaine(ReceptionJpaEntity e) {
        List<LigneReception> lignes = e.getLignes().stream()
            .map(l -> LigneReception.reconstituer(l.getId(), l.getCodeProduit(), l.getSku(),
                l.getQuantiteAttendue(), l.getQuantiteRecue(), l.getIdLigneBC()))
            .toList();

        return Reception.reconstituer(e.getId(), e.getNumero(), SourceReception.valueOf(e.getSource()),
            CategorieProduit.valueOf(e.getCategorie()), e.getCodeFournisseur(), e.getNumeroBonCommande(),
            e.getNumeroLotProduction(), e.getCodeEntrepotCible(), StatutReception.valueOf(e.getStatut()),
            lignes, e.getMatriculeValidateur1(), e.getMatriculeValidateur2(), e.getMotifRejet(),
            e.getSeuilEcartPourcentage(), e.getDateReception(), e.getDateCreation(),
            e.getDateModification(), e.getVersion(),
            e.getNotesValidation1(), e.getNotesValidation2(),
            e.getDateValidation1(), e.getDateValidation2());
    }
}
