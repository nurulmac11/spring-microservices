package com.cenoa.authentication.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Builder
@ToString
@Data
public class UserDetails {
    private String first_name;
    private String last_name;
    private String email;
    private double balance;
}
