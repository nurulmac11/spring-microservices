package com.cenoa.transactions.controller;

import com.cenoa.transactions.dto.DepositRequest;
import com.cenoa.transactions.dto.MqMessage;
import com.cenoa.transactions.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final AmqpTemplate rabbitTemplate;
    private final RabbitMQService mqService;

    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from transaction service");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestHeader("username") String username,
            @RequestHeader("user_id") String user_id,
            @RequestBody DepositRequest request
    ) {
        int id = Integer.parseInt(user_id);
        // Create deposit record on db
        var msg = MqMessage.builder()
                .amount(request.getAmount())
                .operation("deposit")
                .build();
        mqService.sendMessage(msg);
        return ResponseEntity.ok("Deposit will made.");
    }


}
