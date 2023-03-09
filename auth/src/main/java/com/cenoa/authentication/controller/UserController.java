package com.cenoa.authentication.controller;

import com.cenoa.authentication.dto.*;
import com.cenoa.authentication.model.User;
import com.cenoa.authentication.repository.UserRepository;
import com.cenoa.authentication.service.AuthenticationService;
import com.cenoa.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/deposit")
    public ResponseEntity deposit(
            @RequestBody Deposit deposit
    ) {
        System.out.println("USer controller called");
        userService.deposit(deposit.getAmount(), deposit.getUser_id());
        return ResponseEntity.ok("");
    }

}
