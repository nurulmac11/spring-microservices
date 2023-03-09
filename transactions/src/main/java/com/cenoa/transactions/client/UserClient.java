package com.cenoa.transactions.client;

import com.cenoa.transactions.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {

    private final String base_url = "http://auth:8081/api/auth/";
    WebClient webClient = WebClient.create(base_url);

    public UserDto getUserDetails(String token) {
        UserDto user = webClient.get()
                .uri("/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UserDto.class).block();

        return user;
    }
}
