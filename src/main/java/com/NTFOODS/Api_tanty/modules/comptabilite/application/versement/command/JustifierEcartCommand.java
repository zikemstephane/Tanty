package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command;

public record JustifierEcartCommand(
        String referenceVersement,
        String justification
) {}
