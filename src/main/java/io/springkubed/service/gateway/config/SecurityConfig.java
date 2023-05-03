package io.springkubed.service.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .authorizeExchange(authorizeExchange ->
                        authorizeExchange.anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint())
                )
                .csrf(csrf ->
                        csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                )
                .cors(Customizer.withDefaults())
                .oauth2Login(oauth2 ->
                        oauth2.authenticationSuccessHandler(authenticationSuccessHandler())
                )
                .oauth2Client(Customizer.withDefaults());
        // @formatter:on
        return http.build();
    }

    private ServerAuthenticationEntryPoint authenticationEntryPoint() {
        RedirectServerAuthenticationEntryPoint webAuthenticationEntryPoint =
                new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/messaging-client-oidc");
        MediaTypeServerWebExchangeMatcher textHtmlMatcher =
                new MediaTypeServerWebExchangeMatcher(MediaType.TEXT_HTML);
        textHtmlMatcher.setUseEquals(true);

        return new DelegatingServerAuthenticationEntryPoint(
                new DelegatingServerAuthenticationEntryPoint.DelegateEntry(textHtmlMatcher, webAuthenticationEntryPoint));
    }

    private RedirectServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RedirectServerAuthenticationSuccessHandler("http://127.0.0.1:4200");
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        // @formatter:off
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("USER")
                .build();
        // @formatter:on
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public WebFilter csrfTokenSubscriberWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfAttribute = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
            return csrfAttribute.then(chain.filter(exchange));
        };
    }

}
