package com.cenoa.transactions.service;

import com.cenoa.transactions.dto.NewDeposit;
import com.cenoa.transactions.model.Deposit;
import com.cenoa.transactions.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final DepositRepository depositRepository;

  public Deposit new_deposit(NewDeposit deposit) {
    var depo = Deposit.builder()
            .preDeposit(deposit.getPreDeposit())
            .amount(deposit.getAmount())
            .completed(deposit.isCompleted()).build();
    return depositRepository.save(depo);
  }

}
