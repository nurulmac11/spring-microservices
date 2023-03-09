package com.cenoa.transactions.service;

import com.cenoa.transactions.client.UserClient;
import com.cenoa.transactions.dto.MqMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class RabbitMQService {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName = "operations";
    private final UserClient userClient;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    public void sendMessage(MqMessage message) {
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @RabbitListener(queues = queueName)
    public void listen(MqMessage in) {
        String operation = in.operation();
        if (Objects.equals(operation, "deposit")) {
            userClient.deposit(in);
        } else if (Objects.equals(operation, "withdraw")) {
            userClient.withdraw(in);
        } else if (Objects.equals(operation, "transfer")) {
            userClient.transfer(in);
        }
    }
}
