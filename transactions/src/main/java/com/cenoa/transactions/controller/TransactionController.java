package com.cenoa.transactions.controller;

import com.cenoa.transactions.client.UserClient;
import com.cenoa.transactions.dto.*;
import com.cenoa.transactions.model.Deposit;
import com.cenoa.transactions.model.Withdraw;
import com.cenoa.transactions.service.RabbitMQService;
import com.cenoa.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
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
            @RequestHeader("user_id") String user_id,
            @RequestHeader("Authorization") String token,
            @RequestBody DepositRequest request
    ) {
        int id = Integer.parseInt(user_id);
        // Get current balance of user
        UserDto user = userClient.getUserDetails(token);
        double balance = user.getBalance();
        double depositAmount = request.getAmount();

        if (depositAmount <= 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Amount has to be a positive number.");
        }

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
                .user_id(id)
                .build();
        mqService.sendMessage(msg);
        return ResponseEntity.ok("Deposit will made. Current balance: " + balance);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @RequestHeader("user_id") String user_id,
            @RequestHeader("Authorization") String token,
            @RequestBody WithdrawRequest request
    ) {
        int id = Integer.parseInt(user_id);
        // Get current balance of user
        UserDto user = userClient.getUserDetails(token);
        double balance = user.getBalance();
        double withdrawAmount = request.getAmount();

        if (withdrawAmount <= 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Amount has to be a positive number.");
        }

        // Create deposit dto
        var withdraw = NewWithdraw.builder()
                .preDeposit(balance)
                .amount(withdrawAmount)
                .completed(false)
                .user_id(id)
                .build();

        // Create withdraw record on db
        Withdraw withdrawObject = transactionService.new_withdraw(withdraw);

        // Send deposit details to queue to processed later
        var msg = MqMessage.builder()
                .amount(withdrawAmount)
                .operation("withdraw")
                .db_id(withdrawObject.getId())
                .user_id(id)
                .build();
        mqService.sendMessage(msg);
        return ResponseEntity.ok("Withdraw will made. Current balance: " + balance);
    }

}
