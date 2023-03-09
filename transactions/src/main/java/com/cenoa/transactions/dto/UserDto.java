package com.cenoa.transactions.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

  private Integer id;
  private String firstname;
  private String lastname;
  private String email;

  private Double balance;

  private Date created = new Date();
  private Date updated = new Date();

  private String role;

}
