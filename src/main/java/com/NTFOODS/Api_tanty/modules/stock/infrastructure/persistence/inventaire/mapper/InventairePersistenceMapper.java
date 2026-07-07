package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.mapper;

import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.TypeInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.LigneInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.LigneInventaireJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa.SessionInventaireJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventairePersistenceMapper {

    public SessionInventaireJpaEntity versEntite(SessionInventaire s) {
        SessionInventaireJpaEntity entite = SessionInventaireJpaEntity.builder()
            .id(s.getId()).numero(s.getNumero()).type(s.getType().name())
            .codeEntrepot(s.getCodeEntrepot()).perimetre(s.getPerimetre()).statut(s.getStatut().name())
            .matriculeInitiateur(s.getMatriculeInitiateur()).matriculeValidateurDG(s.getMatriculeValidateurDG())
            .justificationEcarts(s.getJustificationEcarts()).dateInventaire(s.getDateInventaire())
            .dateCreation(s.getDateCreation()).dateValidation(s.getDateValidation()).version(s.getVersion())
            .lignes(new ArrayList<>())
            .build();

        List<LigneInventaireJpaEntity> lignes = s.getLignes().stream()
            .map(l -> LigneInventaireJpaEntity.builder()
                .id(l.getId()).session(entite).codeProduit(l.getCodeProduit()).emplacement(l.getEmplacement())
                .quantiteTheorique(l.getQuantiteTheorique()).quantiteComptee(l.getQuantiteComptee()).build())
            .toList();
        entite.getLignes().addAll(lignes);
        return entite;
    }

    public SessionInventaire versDomaine(SessionInventaireJpaEntity e) {
        List<LigneInventaire> lignes = e.getLignes().stream()
            .map(l -> LigneInventaire.reconstituer(l.getId(), l.getCodeProduit(), l.getEmplacement(),
                l.getQuantiteTheorique(), l.getQuantiteComptee()))
            .toList();
        return SessionInventaire.reconstituer(e.getId(), e.getNumero(), TypeInventaire.valueOf(e.getType()),
            e.getCodeEntrepot(), e.getPerimetre(), StatutInventaire.valueOf(e.getStatut()), lignes,
            e.getMatriculeInitiateur(), e.getMatriculeValidateurDG(), e.getJustificationEcarts(),
            e.getDateInventaire(), e.getDateCreation(), e.getDateValidation(), e.getVersion());
    }
}
