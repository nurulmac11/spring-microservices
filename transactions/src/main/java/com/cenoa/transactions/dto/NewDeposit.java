package com.cenoa.transactions.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewDeposit {
  private int user_id;
  private double amount;
  private double preDeposit;
  private boolean completed;
}
