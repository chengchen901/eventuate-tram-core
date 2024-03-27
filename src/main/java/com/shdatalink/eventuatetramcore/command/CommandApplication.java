package com.shdatalink.eventuatetramcore.command;

import io.eventuate.tram.commands.common.ReplyMessageHeaders;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
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
public class CommandApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

    @Autowired
    private CommandProducer commandProducer;

    @Autowired
    private  CommandConfig config;

    @Autowired
    private MessageConsumer messageConsumer;

    private BlockingQueue<Message> queue = new LinkedBlockingDeque<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        * 命令发送和响应处理流程如下
        * 1、命令消费者订阅命令响应topic，对应值为【config.getReplyChannel()】
        * 2、命令生成者发送命令到【config.getCommandChannel()】，传递类型为【Command】的对象，指定响应topic为【config.getReplyChannel()】
        * 3、CommandHandler 处理命令也就是具体的业务逻辑操作，完成后将命令发送到命令响应topic，对应值为【config.getReplyChannel()】
        * 4、命令生成者订阅了【config.getReplyChannel()】topic，收到 CommandHandler 执行返回的结果
        * */
        System.out.println("============================执行开始============================");
        subscribeToReplyChannel();

        String commandId = sendCommand();
        System.out.println("发送消息完成，消息id：" + commandId);

        assertReplyReceived(commandId);
        System.out.println("============================执行结束============================");
    }

    private void assertReplyReceived(String commandId) throws InterruptedException {
        Message m = queue.poll(10, TimeUnit.SECONDS);
        System.out.println("Got message = " + m);
        if (m == null) {
            throw new RuntimeException("Expected reply by deadline");
        }

        if (!commandId.equals(m.getRequiredHeader(ReplyMessageHeaders.IN_REPLY_TO))) {
            throw new RuntimeException("发送消息返回的id和接收消息响应的id不同");
        }
    }

    private String sendCommand() {
        return commandProducer.send(config.getCommandChannel(),
                new ReserveCreditCommand("这是我自定义的用户id"),
                config.getReplyChannel(),
                Collections.emptyMap());
    }

    private void subscribeToReplyChannel() {
        String subscriberId = "subscriberId" + config.getUniqueId();
        messageConsumer.subscribe(subscriberId, Collections.singleton(config.getReplyChannel()), this::handleMessage);
    }

    private void handleMessage(Message message) {
        System.out.println("消息订阅者收到消息：" + message);
        System.out.println("消息订阅者收到消息内容：" + message.getPayload());
        queue.add(message);
    }
}
