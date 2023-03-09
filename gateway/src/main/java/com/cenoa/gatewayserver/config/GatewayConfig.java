package com.cenoa.gatewayserver.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GatewayConfig {

    AuthenticationPrefilter authFilter;

    /**
     * This project has 2 internal microservices right now, auth and transaction,
     * gateway redirects accordingly using regex to parse path
     * @param builder route locator builder
     * @return route locator
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .filters(f -> f
                                .rewritePath("/auth/(?<segment>.*)","/api/auth/${segment}")
                                .filter(authFilter.apply(new AuthenticationPrefilter.Config()))
                        )
                        .uri("http://auth:8081"))
                .route("transaction", r -> r.path("/transaction/**")
                        .filters(f -> f
                                .rewritePath("/transaction/(?<segment>.*)","/api/transaction/${segment}")
                                .filter(authFilter.apply(new AuthenticationPrefilter.Config()))
                        )
                        .uri("http://transaction:8082"))
                .build();
    }

}
