package com.vargas.leo.gerenciadorassembleia.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudAMQPConfig {

    public static final String EXCHANGE_NAME = "voting-result-exchange";
    public static final String QUEUE_NAME = "voting-result-queue";
    public static final String DLQ_EXCHANGE_NAME = EXCHANGE_NAME +".dlq";
    public static final String DLQ_NAME = QUEUE_NAME +".dlq";

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(DLQ_NAME).build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    Binding DLQbinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(DLQ_NAME);
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).withArgument("x-dead-letter-exchange",DLQ_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DLQ_NAME).build();
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EXCHANGE_NAME+".#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        listenerAdapter.setMessageConverter(converter());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageConverter converter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ListenerService receiver) {
        return new MessageListenerAdapter(receiver, "listenMessage");
    }
}
