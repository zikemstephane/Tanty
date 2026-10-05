package com.NTFOODS.Api_tanty.modules.administration.application.dto;

import java.util.List;

public record ParametresResponse(
    double tvaTaux,
    double primeMontant,
    double alerteStockSeuil,
    double seuilClientStrategique,
    List<String> zones,
    List<String> gammeProduits,
    List<String> villes
) {}
