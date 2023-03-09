package com.cenoa.transactions.client;

import com.cenoa.transactions.dto.*;
import com.cenoa.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserClient {

    private final String base_url = "http://auth:8081/api/";
    WebClient webClient = WebClient.create(base_url);

    private final TransactionService transactionService;

    public UserDto getUserDetails(String token) {
        UserDto user = webClient.get()
                .uri("/auth/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UserDto.class).block();

        return user;
    }

    public void deposit(MqMessage mqMessage) {
        var request = DepositRequestClient.builder()
                        .amount(mqMessage.amount())
                                .userId(mqMessage.user_id())
                                        .build();
        webClient.post()
                .uri("/user/deposit")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markDepositCompleted(mqMessage.db_id());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

    public void withdraw(MqMessage mqMessage) {
        var request = WithdrawRequestClient.builder()
                .amount(mqMessage.amount())
                .userId(mqMessage.user_id())
                .build();
        webClient.post()
                .uri("/user/withdraw")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markWithdrawCompleted(mqMessage.db_id());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

    public void transfer(MqMessage mqMessage) {
        var request = TransferRequestClient.builder()
                .amount(mqMessage.amount())
                .userId(mqMessage.user_id())
                .toUserId(mqMessage.to_user_id())
                .build();
        webClient.post()
                .uri("/user/transfer")
                .body(Mono.just(request), TransferRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            transactionService.markTransferCompleted(mqMessage.db_id());
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

}
