package com.shdatalink.eventuatetramcore.command;

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandHandlersBuilder;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;

public class CommandHandler {
    private String commandChannel;
    public CommandHandler(String commandChannel) {
        this.commandChannel = commandChannel;
    }

    public CommandHandlers getCommandHandlers() {
        return CommandHandlersBuilder
                .fromChannel(commandChannel)
                .onMessage(ReserveCreditCommand.class, this::reserveCredit)
                .build();

    }
    public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
        System.out.println("CommandHandler 收到下发命令开始处理业务逻辑");
        System.out.println("CommandHandler 收到消息：" + cm);
        System.out.println("CommandHandler 业务数据-消费者id：" + cm.getCommand().getCustomerId());
        return CommandHandlerReplyBuilder.withSuccess(new ReserveCreditCommand(cm.getCommand().getCustomerId() + "：处理后的消费者id"));
    }
}
