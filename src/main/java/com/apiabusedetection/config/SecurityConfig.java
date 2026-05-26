package com.apiabusedetection.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    String[] PUBLIC_ENDPOINTS = {"/users",
            "/auth/token", "/auth/introspect"};
    JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                                .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    //define interface
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtProperties.getSignerKey().getBytes(StandardCharsets.UTF_8), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
