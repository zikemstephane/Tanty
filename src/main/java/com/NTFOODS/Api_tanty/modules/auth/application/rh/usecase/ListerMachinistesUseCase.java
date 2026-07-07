package com.NTFOODS.Api_tanty.modules.auth.application.rh.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.mapper.EmployeMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.repository.EmployeRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Lister les machinistes actifs.
 *
 * Le Chef des Machinistes consulte/sélectionne les machinistes créés par le DG.
 * Il n'a pas le droit de créer un machiniste.
 */
@Service
@Transactional(readOnly = true)
public class ListerMachinistesUseCase {

    private final EmployeRepository employeRepository;
    private final EmployeMapper employeMapper;

    public ListerMachinistesUseCase(EmployeRepository employeRepository, EmployeMapper employeMapper) {
        this.employeRepository = employeRepository;
        this.employeMapper = employeMapper;
    }

    public List<EmployeResponse> executer() {
        return employeRepository.trouverParRoleEtActif(Role.MACHINISTE, true).stream()
            .map(employeMapper::versResponse)
            .toList();
    }
}
