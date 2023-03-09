package com.cenoa.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewWithdraw {
  private int userId;
  private double amount;
  private double preDeposit;
  private boolean completed;
}
