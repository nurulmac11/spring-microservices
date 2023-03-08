package com.cenoa.transactions.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Deposit {

  @Id
  @GeneratedValue
  private Integer id;

  private Integer user_id;

  private Double amount;
  private Double preDeposit;

  private Date created = new Date();

  @PrePersist
  protected void prePersist() {
    if (this.created == null) created = new Date();
  }

}
