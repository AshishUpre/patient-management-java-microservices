package com.pm.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class JwtValidationException {
    /**
     * this is for handling when this service calls auth-service when it has a wrong token
     * then auth-service wil return unauthorized to this service. But as unauthorized was returned to this
     * it will be an error hence the client will be sent 500 (internal server error).
     * but in reality client shouldve been sent 401 (unauthorized). Hence we have this class to make sure
     * when we get Webclientresponseexception from auth-service, it means token is wrong and we should send 401
     * to the client properly
     */
    @ExceptionHandler(WebClientResponseException.Unauthorized.class)
    public Mono<Void> handleUnauthorizedException(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
