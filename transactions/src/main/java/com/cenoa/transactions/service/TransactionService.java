package com.cenoa.transactions.service;

import com.cenoa.transactions.dto.NewDeposit;
import com.cenoa.transactions.dto.NewWithdraw;
import com.cenoa.transactions.model.Deposit;
import com.cenoa.transactions.model.Withdraw;
import com.cenoa.transactions.repository.DepositRepository;
import com.cenoa.transactions.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final DepositRepository depositRepository;
  private final WithdrawRepository withdrawRepository;

  public Deposit new_deposit(NewDeposit deposit) {
    var depo = Deposit.builder()
            .preDeposit(deposit.getPreDeposit())
            .amount(deposit.getAmount())
            .user_id(deposit.getUser_id())
            .completed(deposit.isCompleted()).build();
    return depositRepository.save(depo);
  }

  public Withdraw new_withdraw(NewWithdraw withdraw) {
    var _withdraw = Withdraw.builder()
            .preWithdraw(withdraw.getPreDeposit())
            .amount(withdraw.getAmount())
            .user_id(withdraw.getUser_id())
            .completed(withdraw.isCompleted()).build();
    return withdrawRepository.save(_withdraw);
  }

}
