package com.shdatalink.eventuatetramcore.event;

import com.shdatalink.eventuatetramcore.event.domain.AccountDebited;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventHandler {
    private BlockingQueue<AccountDebited> queue = new LinkedBlockingDeque<>();
    private String aggregateType;

    public EventHandler(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(aggregateType)
                .onEvent(AccountDebited.class, this::handleAccountDebited)
                .build();
    }

    public void handleAccountDebited(DomainEventEnvelope<AccountDebited> event) {
        System.out.println("EventHandler 事件消费者收到消息");
        System.out.println("EventHandler aggregateType：" + aggregateType);
        System.out.println("EventHandler event：" + event);
        queue.add(event.getEvent());
    }

    public BlockingQueue<AccountDebited> getQueue() {
        return queue;
    }
}
