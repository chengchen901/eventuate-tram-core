package com.shdatalink.eventuatetramcore.event;

import com.shdatalink.eventuatetramcore.event.domain.AccountDebited;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Import({TramJdbcKafkaConfiguration.class})
@SpringBootApplication
public class EventApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private EventConfig config;

    @Autowired
    private EventHandler eventHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
         * 执行顺序如下
         * 1、事件消费者 EventHandler 订阅【config.getAggregateType()】topic
         * 2、事件发布者发布事件到【config.getAggregateType()】topic，并传递【事件对象】和【AggregateId（业务id）】
         * 3、事件消费者收到时间进行处理
         */
        System.out.println("=========================开始执行=========================");
        long uniqueId = config.get();

        DomainEvent domainEvent = new AccountDebited(uniqueId);

        System.out.println("事件发送内容：" + domainEvent + "，AggregateType：" + config.getAggregateType() + "，AggregateId：" + config.getAggregateId());
        domainEventPublisher.publish(config.getAggregateType(), config.getAggregateId(), Collections.singletonList(domainEvent));
        System.out.println("事件发送完成");

        AccountDebited event = eventHandler.getQueue().poll(10, TimeUnit.SECONDS);

        if (event == null) {
            throw new RuntimeException("未获取到event数据");
        }
        if (uniqueId != event.getAmount()) {
            throw new RuntimeException("消息发送时的金额与xx不一致");
        }
        System.out.println("=========================执行结束=========================");
    }
}
