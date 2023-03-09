package com.cenoa.authentication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Used as dto object in between auth and gateway service for token validation for each request
 */
@Getter
@Builder
@ToString
public class ConnValidationResponse {
    private Integer id;
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String username;
    private String token;
    private List<GrantedAuthority> authorities;
}
