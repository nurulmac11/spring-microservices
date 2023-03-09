package com.cenoa.transactions.client;

import com.cenoa.transactions.dto.DepositRequest;
import com.cenoa.transactions.dto.DepositRequestClient;
import com.cenoa.transactions.dto.MqMessage;
import com.cenoa.transactions.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
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
        System.out.println("Deposit called");
        var request = DepositRequestClient.builder()
                        .amount(mqMessage.amount())
                                .user_id(mqMessage.user_id())
                                        .build();
        System.out.println("request built, will call user");
        webClient.post()
                .uri("/user/deposit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), DepositRequestClient.class)
                .exchangeToMono(
                        clientResponse -> {
                            System.out.println(clientResponse.statusCode());
                            if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
                                System.out.println("error from client call");
                                System.out.println(clientResponse.statusCode());
                                return clientResponse.createException().flatMap( Mono::error );
                            }
                            return clientResponse.bodyToMono(Void.class);
                        }
                );
    }

}
