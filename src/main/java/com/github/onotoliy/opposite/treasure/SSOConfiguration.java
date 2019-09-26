package com.github.onotoliy.opposite.treasure;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * Настройка доступности URL в зависимости от роли пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
@Configuration
@EnableWebSecurity
@KeycloakConfiguration
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SSOConfiguration
extends KeycloakWebSecurityConfigurerAdapter {

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
            @Value("${treasure.modification.roles}")
            final String[] modification,
            @Value("${treasure.reading.roles}")
            final String[] reading) {
        this.modification = modification;
        this.reading = reading;
    }

    /**
     * Настрока авторизации.
     *
     * @param auth Настройки авторизации.
     */
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider provider =
            keycloakAuthenticationProvider();

        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        auth.authenticationProvider(provider);
    }

    /**
     * Подключение Keycloak.
     *
     * @return Keycloak перехватчик.
     */
    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
            new SessionRegistryImpl());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);

        http.cors();
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST).hasAnyRole(modification)
            .antMatchers(HttpMethod.PUT).hasAnyRole(modification)
            .antMatchers(HttpMethod.DELETE).hasAnyRole(modification)
            .antMatchers(HttpMethod.GET).hasAnyRole(reading);
    }
}
