package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

public record LoginResponse(
    String token,
    String matricule,
    String nomComplet,
    Role role,
    long expirationMs
) {}
