package com.cenoa.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTransfer {
  private int user_id;
  private int to_user_id;
  private double amount;
  private double preTransfer;
  private boolean completed;
}
