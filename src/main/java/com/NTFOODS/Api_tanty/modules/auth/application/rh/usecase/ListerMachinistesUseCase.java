package com.NTFOODS.Api_tanty.modules.auth.application.rh.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.mapper.EmployeMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Use Case — Lister les machinistes actifs.
 *
 * Le Chef des Machinistes consulte/sélectionne les machinistes (rôle MACHINISTE
 * uniquement, sans le CHEF_MACHINISTE) parmi les comptes utilisateurs créés par le DG.
 */
@Service
@Transactional(readOnly = true)
public class ListerMachinistesUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final EmployeMapper employeMapper;

    public ListerMachinistesUseCase(UtilisateurRepository utilisateurRepository, EmployeMapper employeMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.employeMapper = employeMapper;
    }

    public List<EmployeResponse> executer() {
        return utilisateurRepository.listerTous().stream()
            .filter(u -> u.isActif() && !u.isCompteBloque() && u.getRole() == Role.MACHINISTE)
            .sorted(Comparator.comparing(u -> u.getNomComplet().getNomComplet()))
            .map(employeMapper::versResponse)
            .toList();
    }
}
