package com.NTFOODS.Api_tanty.shared.kernel.event;

public interface EventPublisher {
    void publish(Object event);
    void publishAll(Iterable<?> events);
}
