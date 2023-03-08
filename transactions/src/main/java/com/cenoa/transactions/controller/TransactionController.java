package com.cenoa.transactions.controller;

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

  @GetMapping("/demo")
  public ResponseEntity<String> sayHello() {
    rabbitTemplate.convertAndSend("myqueue", "Hello, world from transaction queuee!");
    return ResponseEntity.ok("Hello from transaction service");
  }


}
