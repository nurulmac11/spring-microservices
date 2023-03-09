package com.cenoa.transactions.service;

import com.cenoa.transactions.dto.NewDeposit;
import com.cenoa.transactions.dto.NewTransfer;
import com.cenoa.transactions.dto.NewWithdraw;
import com.cenoa.transactions.dto.TransferRequest;
import com.cenoa.transactions.model.Deposit;
import com.cenoa.transactions.model.Transfer;
import com.cenoa.transactions.model.Withdraw;
import com.cenoa.transactions.repository.DepositRepository;
import com.cenoa.transactions.repository.TransferRepository;
import com.cenoa.transactions.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final DepositRepository depositRepository;
    private final WithdrawRepository withdrawRepository;
    private final TransferRepository transferRepository;

    public void markDepositCompleted(int deposit_id) {
        Optional<Deposit> optionalEntity = depositRepository.findById(deposit_id);
        if (optionalEntity.isPresent()) {
            var depositToUpdate = optionalEntity.get();
            depositToUpdate.setCompleted(true);
            depositRepository.save(depositToUpdate);
        }
    }

    public void markWithdrawCompleted(int withdraw_id) {
        Optional<Withdraw> optionalEntity = withdrawRepository.findById(withdraw_id);
        if (optionalEntity.isPresent()) {
            var withdrawToUpdate = optionalEntity.get();
            withdrawToUpdate.setCompleted(true);
            withdrawRepository.save(withdrawToUpdate);
        }
    }


    public void markTransferCompleted(int transfer) {
        Optional<Transfer> optionalEntity = transferRepository.findById(transfer);
        if (optionalEntity.isPresent()) {
            var transferToUpdate = optionalEntity.get();
            transferToUpdate.setCompleted(true);
            transferRepository.save(transferToUpdate);
        }
    }

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

    public Transfer new_transfer(NewTransfer transfer) {
        var _withdraw = Transfer.builder()
                .preAmountFrom(transfer.getPreTransfer())
                .amount(transfer.getAmount())
                .from_user_id(transfer.getUser_id())
                .to_user_id(transfer.getTo_user_id())
                .completed(transfer.isCompleted()).build();
        return transferRepository.save(_withdraw);
    }

}
