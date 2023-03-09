package com.cenoa.transactions.controller;

import com.cenoa.transactions.client.UserClient;
import com.cenoa.transactions.dto.DepositRequest;
import com.cenoa.transactions.dto.MqMessage;
import com.cenoa.transactions.dto.NewDeposit;
import com.cenoa.transactions.dto.UserDto;
import com.cenoa.transactions.model.Deposit;
import com.cenoa.transactions.service.RabbitMQService;
import com.cenoa.transactions.service.TransactionService;
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
    private final TransactionService transactionService;
    private final UserClient userClient;

    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from transaction service");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestHeader("username") String username,
            @RequestHeader("user_id") String user_id,
            @RequestHeader("Authorization") String token,
            @RequestBody DepositRequest request
    ) {
        int id = Integer.parseInt(user_id);
        UserDto user = userClient.getUserDetails(token);
        double balance = user.getBalance();
        double depositAmount = request.getAmount();

        // Create deposit dto
        var deposit = NewDeposit.builder()
                .preDeposit(balance)
                .amount(depositAmount)
                .completed(false)
                .user_id(id)
                .build();

        // Create deposit record on db
        Deposit depositObject = transactionService.new_deposit(deposit);

        // Send deposit details to queue to processed later
        var msg = MqMessage.builder()
                .amount(depositAmount)
                .operation("deposit")
                .db_id(depositObject.getId())
                .build();
        mqService.sendMessage(msg);
        return ResponseEntity.ok("Deposit will made.");
    }


}
