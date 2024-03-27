package com.shdatalink.eventuatetramcore.command;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.commands.consumer.CommandDispatcherFactory;
import io.eventuate.tram.spring.commands.consumer.TramCommandConsumerConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramCommandProducerConfiguration.class, TramCommandConsumerConfiguration.class})
public class CommandConfiguration {
    @Bean
    public CommandConfig commandConfig() {
        return  new CommandConfig();
    }

    @Bean
    public CommandHandler commandHandler(CommandConfig config) {
        return new CommandHandler(config.getCommandChannel());
    }

    @Bean
    public CommandDispatcher commandDispatcher(CommandDispatcherFactory commandDispatcherFactory,
                                               CommandConfig config, CommandHandler target) {
        return commandDispatcherFactory.make(config.getCommandDispatcheId(), target.getCommandHandlers());
    }
}
