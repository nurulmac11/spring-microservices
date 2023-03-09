package com.cenoa.transactions.controller;

import com.cenoa.transactions.dto.MqMessage;
import com.cenoa.transactions.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

  private final AmqpTemplate rabbitTemplate;
  private final RabbitMQService mqService;

  @GetMapping("/demo")
  public ResponseEntity<String> sayHello() {
    mqService.sendMessage();
    return ResponseEntity.ok("Hello from transaction service");
  }


}
