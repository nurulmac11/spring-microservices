package com.cenoa.authentication.controller;

import com.cenoa.authentication.dto.Deposit;
import com.cenoa.authentication.dto.Transfer;
import com.cenoa.authentication.dto.Withdraw;
import com.cenoa.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestBody Deposit deposit
    ) {
        userService.deposit(deposit.getAmount(), deposit.getUserId());
        return ResponseEntity.ok("");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> deposit(
            @RequestBody Withdraw withdraw
    ) {
        userService.withdraw(withdraw.getAmount(), withdraw.getUserId());
        return ResponseEntity.ok("");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestBody Transfer transfer
    ) {
        userService.transfer(transfer.getAmount(), transfer.getUserId(), transfer.getToUserId());
        return ResponseEntity.ok("");
    }
}
