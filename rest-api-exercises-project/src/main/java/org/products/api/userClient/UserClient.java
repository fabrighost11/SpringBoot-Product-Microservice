package org.products.api.userClient;

import org.products.dto.response.UserResponse;
import org.products.exception.ForbiddenAccessException;
import org.products.exception.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public UserResponse getUserById(Long id, String token) {
        return  webClient.get()
                .uri("http://localhost:8081/api/user/" + id)
                .header(HttpHeaders.AUTHORIZATION,token)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED) {
                        throw new UnauthorizedException("Unauthorized access - Invalid token");
                    }
                    if (clientResponse.statusCode() == HttpStatus.FORBIDDEN) {
                        throw new ForbiddenAccessException("Forbidden - Insufficient permissions");
                    }
                    return Mono.error(new RuntimeException("Client error"));
                })
                .bodyToMono(UserResponse.class)
                .block();
    }
}
