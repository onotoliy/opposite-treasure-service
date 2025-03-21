package com.github.onotoliy.opposite.treasure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Настройка доступности URL в зависимости от роли пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
@Configuration
@EnableWebSecurity
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
        });
        return http.build();
    }
}
