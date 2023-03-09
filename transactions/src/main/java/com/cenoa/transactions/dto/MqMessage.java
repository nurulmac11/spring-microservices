package com.cenoa.transactions.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MqMessage(
        @JsonProperty("operation") String operation,
        @JsonProperty("amount") double amount,
        @JsonProperty("to") int to,
        @JsonProperty("db_id") int db_id
        )
        implements Serializable {
}