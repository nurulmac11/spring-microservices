package com.cenoa.transactions.client;

import com.cenoa.transactions.dto.*;
import com.cenoa.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Contains methods to communicate with auth microservice
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserClient {

    private final String base_url = "http://auth:8081/api/";
    WebClient webClient = WebClient.create(base_url);

    private final TransactionService transactionService;

    /**
     * Get user details from token
     * @param token jwt token
     * @return user dto object
     */
    public UserDto getUserDetails(String token) {
        UserDto user = webClient.get()
                .uri("/auth/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UserDto.class).block();

        return user;
    }

    /**
     * Retrieves rabbitmq message as parameter,
     * sends a request to auth service for depositing money to account
     * @param mqMessage rabbitmq message
     */
    public void deposit(MqMessage mqMessage) {
        var request = DepositRequestClient.builder()
                        .amount(mqMessage.amount())
                                .userId(mqMessage.userId())
                                        .build();
        webClient.post()
                .uri("/user/deposit")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markDepositCompleted(mqMessage.dbId());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }


    /**
     * retrieves rabbitmq message as parameter,
     * sends a request to auth service to withdraw money from account
     * @param mqMessage rabbitmq message
     */
    public void withdraw(MqMessage mqMessage) {
        var request = WithdrawRequestClient.builder()
                .amount(mqMessage.amount())
                .userId(mqMessage.userId())
                .build();
        webClient.post()
                .uri("/user/withdraw")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markWithdrawCompleted(mqMessage.dbId());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }


    /**
     * retrieves rabbitmq message as parameter,
     * sends a request to auth service to transfer money between accounts
     * @param mqMessage rabbitmq message
     */
    public void transfer(MqMessage mqMessage) {
        var request = TransferRequestClient.builder()
                .amount(mqMessage.amount())
                .userId(mqMessage.userId())
                .toUserId(mqMessage.toUserId())
                .build();
        webClient.post()
                .uri("/user/transfer")
                .body(Mono.just(request), TransferRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markTransferCompleted(mqMessage.dbId());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

}
