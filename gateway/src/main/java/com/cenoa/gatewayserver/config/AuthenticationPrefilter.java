package com.cenoa.gatewayserver.config;

import com.cenoa.gatewayserver.model.Authorities;
import com.cenoa.gatewayserver.model.ConnValidationResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {

    private final WebClient.Builder webClientBuilder;
    private final RouterValidator routerValidator;

    /**
     * Applied for each request to gateway
     * Validates requestbefore rerouting to internal microservices
     * @param config config
     * @return filter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("**************************************************************************");
            log.info("URL is - " + request.getURI().getPath());
            String bearerToken = request.getHeaders().getFirst("Authorization");
            log.info("Bearer Token: "+ bearerToken);

            if(routerValidator.isSecured.test(request)) {
                return webClientBuilder.build().get()
                        .uri("http://auth:8081/api/auth/validate_token")
                        .header("Authorization", bearerToken)
                        .retrieve().bodyToMono(ConnValidationResponse.class)
                        .map(response -> {
                            exchange.getRequest().mutate().header("username", response.getEmail());
                            exchange.getRequest().mutate().header("authorities", response.getAuthorities().stream().map(Authorities::getAuthority).reduce("", (a, b) -> a + "," + b));
                            exchange.getRequest().mutate().header("user_id", String.valueOf(response.getId()));

                            return exchange;
                        }).flatMap(chain::filter).onErrorResume(error -> {
                            log.info("Error Happened");
                            log.info(error.getMessage());
                            HttpStatusCode errorCode = null;
                            String errorMsg = "";
                            if (error instanceof WebClientResponseException) {
                                errorMsg = error.getMessage();
                                errorCode = ((WebClientResponseException) error).getStatusCode();
                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                                errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
                            }
                            return onError(exchange, String.valueOf(errorCode) ,errorMsg, "JWT Authentication Failed", errorCode);
                        });
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errCode, String err, String errDetails, HttpStatusCode httpStatus) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        byte[] byteArr = (err + errDetails).getBytes();
        return response.writeWith(Mono.just(byteArr).map(dataBufferFactory::wrap));

    }

    @NoArgsConstructor
    public static class Config {
    }
}