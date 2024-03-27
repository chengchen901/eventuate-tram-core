package com.shdatalink.eventuatetramcore.message;

import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.MessageBuilder;
import io.eventuate.tram.messaging.producer.MessageProducer;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Import({TramJdbcKafkaConfiguration.class})
@SpringBootApplication
public class MessageApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    private long uniqueId = System.currentTimeMillis();

    private String subscriberId = "subscriberId" + uniqueId;
    /**
     * topic 名称
     */
    private String destination = "destination" + uniqueId;
    private String payload = "{" + "\"Hello\":" + uniqueId + "}";

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MessageConsumer messageConsumer;

    private BlockingQueue<Message> queue = new LinkedBlockingDeque<>();

    private void handleMessage(Message message) {
        queue.add(message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
         * 1、消费者订阅【destination】topic
         * 2、生产者发送消息到【destination】topic，携带【payload】数据
         * 3、消费者收到消息进行处理
         */
        messageConsumer.subscribe(subscriberId, Collections.singleton(destination), this::handleMessage);
        messageProducer.send(destination, MessageBuilder.withPayload(payload).build());

        System.out.println("准备接受消息");
        Message m = queue.poll(10, TimeUnit.SECONDS);
        System.out.println("收到消息：" + m);
    }
}
