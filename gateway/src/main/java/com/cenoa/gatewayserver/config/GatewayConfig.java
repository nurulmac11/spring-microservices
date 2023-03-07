package com.cenoa.gatewayserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

//    @Autowired
//    AuthenticationFilter filter;

    @Autowired
    AuthenticationPrefilter authFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .filters(f -> f
                                .rewritePath("/auth/(?<segment>.*)","/api/auth/${segment}")
                                .filter(authFilter.apply(new AuthenticationPrefilter.Config()))
//                                .filter(filter)
                        )
                        .uri("http://auth:8081"))
                .build();
    }

}
