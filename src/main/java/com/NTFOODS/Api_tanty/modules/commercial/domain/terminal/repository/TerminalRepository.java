package com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.aggregate.Terminal;

import java.util.List;
import java.util.Optional;

public interface TerminalRepository {
    Terminal sauvegarder(Terminal terminal);
    Optional<Terminal> trouverParImei(String imei);
    Optional<Terminal> trouverParMatricule(String matriculeCommercial);
    List<Terminal> listerTous();
    List<Terminal> listerActifs();
}
