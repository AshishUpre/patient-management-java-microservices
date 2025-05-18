package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.stereotype.Component;

/**
 * filter class, Intercepts http req, applies some custom logic and decides if it should pass or not.
 * Because we extended AbstractGatewayFilterFactory and overrided the method, spring cloud gateway will automatically
 * apply the filter
 */
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    /**
     * Building final vars in constructor -
     * building the webclient using webclient builder, and passing the auth service url which is got from
     * env variables and using it to create a webclient
     */
    public JwtValidationGatewayFilterFactory(WebClient.Builder webclientBuilder,
                                             @Value("${auth-service.url}") String authServiceUrl) {
        this.webClient = webclientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        // exchange variable represents the current http request
        // in chain variable we have access to the next filter as we may have multiple filters defined
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // early return if token not proper format
            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // now need to validate the token, we do it by making call to auth service
            // using a webclient (webclient is http client [other http clients eg: are browser and curl])
            return webClient.get().uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }
}
