package io.springkubed.service.gateway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class DefaultController {

    @GetMapping("/")
    public Mono<ResponseEntity<Void>> index() {
        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://127.0.0.1:4200")
                .build();
        return Mono.just(responseEntity);
    }
}
