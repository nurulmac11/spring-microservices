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
  private int userId;
  private int toUserId;
  private double amount;
  private double preTransfer;
  private boolean completed;
}
