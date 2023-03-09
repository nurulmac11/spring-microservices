package com.cenoa.authentication.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Data
public class UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private double balance;
}
