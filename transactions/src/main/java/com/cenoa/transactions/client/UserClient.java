package com.cenoa.transactions.client;

import com.cenoa.transactions.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserClient {

    private final String base_url = "http://auth:8081/api/";
    WebClient webClient = WebClient.create(base_url);


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
                                .user_id(mqMessage.user_id())
                                        .build();
        webClient.post()
                .uri("/user/deposit")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

    public void withdraw(MqMessage mqMessage) {
        var request = WithdrawRequestClient.builder()
                .amount(mqMessage.amount())
                .user_id(mqMessage.user_id())
                .build();
        webClient.post()
                .uri("/user/withdraw")
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

    public void transfer(MqMessage mqMessage) {
        var request = TransferRequestClient.builder()
                .amount(mqMessage.amount())
                .user_id(mqMessage.user_id())
                .to_user_id(mqMessage.to_user_id())
                .build();
        webClient.post()
                .uri("/user/transfer")
                .body(Mono.just(request), TransferRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            return clientResponse.bodyToMono(Void.class);
                        }
                )
                .block();
    }

}
