package com.cenoa.transactions.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MqMessage(
        @JsonProperty("operation") String operation,
        @JsonProperty("amount") double amount,
        @JsonProperty("toUserId") int toUserId,
        @JsonProperty("dbId") int dbId,
        @JsonProperty("userId") int userId
)
        implements Serializable {
}