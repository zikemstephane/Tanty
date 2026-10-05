package com.NTFOODS.Api_tanty.shared.kernel.event;

import java.time.Instant;

public interface DomainEvent {
    String getEventId();
    Instant getOccurredOn();
}
