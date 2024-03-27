package com.shdatalink.eventuatetramcore.event;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventsPublisherConfiguration.class, TramEventSubscriberConfiguration.class})
public class EventConfiguration {
    @Bean
    public EventConfig eventConfig() {
        return new EventConfig();
    }

    @Bean
    public EventHandler eventHandler(EventConfig eventConfig) {
        return new EventHandler(eventConfig.getAggregateType());
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(DomainEventDispatcherFactory domainEventDispatcherFactory
            , EventConfig eventConfig, EventHandler eventHandler) {
        return domainEventDispatcherFactory.make("eventDispatcherId" + eventConfig.get(), eventHandler.domainEventHandlers());
    }
}
