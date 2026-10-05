package com.NTFOODS.Api_tanty.shared.infrastructure.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherImpl implements EventPublisher, DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAll(Iterable<?> events) {
        events.forEach(this::publish);
    }
}
