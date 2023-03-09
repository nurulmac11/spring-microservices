package com.cenoa.authentication.service;

import com.cenoa.authentication.dto.*;
import com.cenoa.authentication.model.Token;
import com.cenoa.authentication.model.User;
import com.cenoa.authentication.repository.TokenRepository;
import com.cenoa.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;

  public void deposit(double amount, int user_id) {
    repository.deposit(amount, user_id);
  }

  public void withdraw(double amount, int user_id) {
    repository.withdraw(amount, user_id);
  }

  public void transfer(double amount, int user_id, int to_user_id) {
    // withdraw from
    repository.withdraw(amount, user_id);
    // deposit to other account
    repository.deposit(amount, to_user_id);
  }
}
