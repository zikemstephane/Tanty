package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import java.time.LocalDate;

public record ModifierProfilRequest(
    String telephone,
    String residence,
    String cni,
    String photo,
    LocalDate dateNaissance,
    String lieuNaissance,
    String sexe
) {}
