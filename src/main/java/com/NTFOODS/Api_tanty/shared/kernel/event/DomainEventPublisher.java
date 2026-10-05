package com.NTFOODS.Api_tanty.shared.kernel.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
