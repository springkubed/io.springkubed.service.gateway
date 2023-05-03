package io.springkubed.service.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class AuthorizationController {

    private final WebClient webClient;
    private final String messagesBaseUri;
    private final String appBaseUri;

    public AuthorizationController(WebClient webClient,
                                   @Value("${messages.base-uri}") String messagesBaseUri,
                                   @Value("${app.base-uri}") String appBaseUri) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
        this.appBaseUri = appBaseUri;
    }

    @GetMapping(value = "/authorize", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<ResponseEntity<Void>> authorizeClient() {
        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", this.appBaseUri + "/authorize")
                .build();
        return Mono.just(responseEntity);
    }

}
