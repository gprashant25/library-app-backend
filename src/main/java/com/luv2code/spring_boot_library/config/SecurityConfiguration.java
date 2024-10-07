package com.luv2code.spring_boot_library.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

// here below we're adding the Spring security feature to our springboot backend application using Okta which allows us to add protected routes on the backend.
// here below Okta will verify the token before we use the token

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Step1: disable Cross site request forgery
        http.csrf(AbstractHttpConfigurer::disable);

        // step2: Protect endpoints at /api/<type>/secure
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/books/secure/**",
                                "/api/reviews/secure/**",
                                "/api/messages/secure/**",
                                "/api/admin/secure/**")  // here we need to add this api so that our backend Okta will verify the token before we use the token
                        .authenticated()
                        .anyRequest().permitAll()   // please note: this code means allowing all the api to use the Okta token directly without adding the api's into SecurityConfiguration class. if you don't add this line then you'll get 401 unauthorized or 403 forbidden error in postman client bcos to access all other API's we have to permit any request coming in Spring security
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                .jwt(Customizer.withDefaults())
                );

        // Adding the CORS filter ie Spring security filter component
        http.cors(Customizer.withDefaults());

        // Adding content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        // Force a non-empty response body for 401 (unauthorized status code) to make the response friendly
        Okta.configureResourceServer401ResponseBody(http);


        return http.build();    // bcos the security configuration is using the build design pattern.
    }
}
