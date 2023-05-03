package io.springkubed.service.gateway.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/userinfo")
    public Mono<Map<String, String>> userInfo(Authentication authentication) {
        return Mono.just(Collections.singletonMap("name", authentication.getName()));
    }

}