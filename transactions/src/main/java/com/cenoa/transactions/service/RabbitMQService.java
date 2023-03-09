package com.cenoa.transactions.service;

import com.cenoa.transactions.dto.MqMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RabbitMQService {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage() {
        final var message = new MqMessage("Hello there!", 999);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend("myqueue", message);
    }

    @RabbitListener(queues = "myqueue")
    public void listen(MqMessage in) {
        System.out.println(in.message());
        System.out.println(in.balance());
    }
}
