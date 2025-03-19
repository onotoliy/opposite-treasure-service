package com.github.onotoliy.opposite.treasure;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Настройка доступности URL в зависимости от роли пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
@Configuration
@EnableWebSecurity
@KeycloakConfiguration
public class SSOConfiguration {

    /**
     * Роли дающие права на изменение данных.
     */
    private final String[] modification;

    /**
     * Роли дающие права на чтение данных.
     */
    private final String[] reading;

    /**
     * Конструтор.
     *
     * @param modification Роли дающие права на изменение данных.
     * @param reading Роли дающие права на чтение данных.
     */
    public SSOConfiguration(
            @Value("${treasure.roles.modification}")
            final String[] modification,
            @Value("${treasure.roles.reading}")
            final String[] reading) {
        this.modification = modification;
        this.reading = reading;
    }

    @Bean
    public AuthenticationProvider keycloakAuthenticationProvider() {
        return new KeycloakAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("keycloak")
                .clientId("myclient")
                .clientSecret("myclientsecret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)  // Указываем grant type
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("http://localhost:8080/realms/myrealm/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/realms/myrealm/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/realms/myrealm/protocol/openid-connect/userinfo")
                .jwkSetUri("http://localhost:8080/realms/myrealm/protocol/openid-connect/certs")
                .build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer. withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(registry -> {
            registry
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/user/register/exception").permitAll()
                    .requestMatchers(HttpMethod.POST).hasAnyRole(modification)
                    .requestMatchers(HttpMethod.PUT).hasAnyRole(modification)
                    .requestMatchers(HttpMethod.DELETE).hasAnyRole(modification)
                    .requestMatchers(HttpMethod.GET).hasAnyRole(reading);
        }).oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
