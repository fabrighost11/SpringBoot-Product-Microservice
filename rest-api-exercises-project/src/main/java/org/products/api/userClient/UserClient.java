package org.products.api.userClient;

import org.products.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public UserResponse getUserById(Long id) {
        return  webClient.get()
                .uri("http://localhost:8081/api/user/" + id)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
}
