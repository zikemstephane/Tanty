package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.JournalAuditResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.audit.JournalAuditJpaEntity;
import com.NTFOODS.Api_tanty.shared.infrastructure.audit.JournalAuditJpaRepository;
import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConsulterJournalAuditUseCase {

    private final JournalAuditJpaRepository journalAuditJpaRepository;

    public ConsulterJournalAuditUseCase(JournalAuditJpaRepository journalAuditJpaRepository) {
        this.journalAuditJpaRepository = journalAuditJpaRepository;
    }

    public List<JournalAuditResponse> executer(String matricule,
                                                LocalDateTime debut,
                                                LocalDateTime fin,
                                                String module,
                                                TypeActionAudit typeAction,
                                                int limite) {
        int max = Math.min(limite, 1000);
        Pageable pageable = PageRequest.of(0, max, Sort.by(Sort.Direction.DESC, "horodatage"));

        List<JournalAuditJpaEntity> entries;

        if (matricule != null && !matricule.isBlank()) {
            entries = journalAuditJpaRepository.findByMatriculeOrderByHorodatageDesc(matricule);
        } else if (debut != null && fin != null) {
            entries = journalAuditJpaRepository.findByHorodatageBetweenOrderByHorodatageDesc(debut, fin);
        } else if (typeAction != null) {
            entries = journalAuditJpaRepository.findByTypeActionOrderByHorodatageDesc(typeAction);
        } else if (module != null && !module.isBlank()) {
            entries = journalAuditJpaRepository.findByModuleOrderByHorodatageDesc(module);
        } else {
            entries = journalAuditJpaRepository.findAll(pageable).getContent();
        }

        return entries.stream()
            .limit(max)
            .map(this::versResponse)
            .toList();
    }

    private JournalAuditResponse versResponse(JournalAuditJpaEntity e) {
        return new JournalAuditResponse(
            e.getId(), e.getMatricule(), e.getRole(), e.getTypeAction(),
            e.getModule(), e.getRessource(), e.getReferenceEntite(),
            e.getMethodeHttp(), e.getEndpoint(), e.getDetails(),
            e.isSucces(), e.getMessageErreur(), e.getAdresseIp(),
            e.getDureeMs(), e.getHorodatage()
        );
    }
}
