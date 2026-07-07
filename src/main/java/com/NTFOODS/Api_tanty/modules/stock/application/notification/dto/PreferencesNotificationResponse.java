package com.NTFOODS.Api_tanty.modules.stock.application.notification.dto;

import java.time.LocalTime;
import java.util.Set;

public record PreferencesNotificationResponse(
    String matriculeUtilisateur,
    boolean notificationInApp,
    boolean notificationEmail,
    boolean notificationSms,
    boolean notificationPush,
    String prioriteMinimale,
    LocalTime heureDebutReception,
    LocalTime heureFinReception,
    Set<String> typesFiltres
) {}
