package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.repository;

import com.NTFOODS.Api_tanty.modules.auth.domain.rh.model.Employe;
import com.NTFOODS.Api_tanty.modules.auth.domain.rh.repository.EmployeRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.jpa.EmployeJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.jpa.EmployeJpaRepository;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.mapper.EmployePersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeRepositoryImpl implements EmployeRepository {

    private final EmployeJpaRepository jpaRepository;
    private final EmployePersistenceMapper mapper;

    public EmployeRepositoryImpl(EmployeJpaRepository jpaRepository, EmployePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Employe sauvegarder(Employe employe) {
        return mapper.versDomaine(jpaRepository.save(mapper.versEntite(employe)));
    }

    @Override
    public Optional<Employe> trouverParMatricule(String matricule) {
        return jpaRepository.findByMatricule(matricule).map(mapper::versDomaine);
    }

    @Override
    public List<Employe> trouverParRole(Role role) {
        return jpaRepository.findByRole(role).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Employe> trouverParRoleEtActif(Role role, boolean actif) {
        return jpaRepository.findByRoleAndActif(role, actif).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Employe> trouverParSuperviseur(String superviseurMatricule) {
        return jpaRepository.findBySuperviseurMatricule(superviseurMatricule).stream().map(mapper::versDomaine).toList();
    }

    @Override
    public List<Employe> trouverTous() {
        return jpaRepository.findAll().stream().map(mapper::versDomaine).toList();
    }

    @Override
    public boolean existeParMatricule(String matricule) {
        return jpaRepository.existsByMatricule(matricule);
    }
}
