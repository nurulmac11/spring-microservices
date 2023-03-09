package com.cenoa.transactions.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MqMessage(@JsonProperty("message") String message,
                        @JsonProperty("balance") int balance)
        implements Serializable {
}