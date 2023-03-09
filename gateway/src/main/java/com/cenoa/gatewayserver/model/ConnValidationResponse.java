package com.cenoa.gatewayserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ConnValidationResponse {
    private Integer id;
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String email;
    private String token;
    private List<Authorities> authorities;
}
