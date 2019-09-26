package com.github.onotoliy.opposite.treasure.rpc;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Сервис чтения данных о пользвателях из Keycloak.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class KeycloakRPC {

    /**
     * Количество подключений к Keycloak.
     */
    private static final int POOL_SIZE = 10;

    /**
     * Таймаут.
     */
    private static final int TIMEOUT = 10;

    /**
     * Название realm.
     */
    private final String realm;

    /**
     * URL на котором развернут Keycloak.
     */
    private final String url;

    /**
     * Название клиента.
     */
    private final String client;

    /**
     * Имя пользователя.
     */
    private final String username;

    /**
     * Пароль.
     */
    private final String password;

    /**
     * Конструктор.
     *
     * @param url URL на котором развернут Keycloak.
     * @param realm Название realm.
     * @param client Название клиента.
     * @param username Имя пользователя.
     * @param password Пароль.
     */
    public KeycloakRPC(
            @Value("${treasure.keycloak.url}") final String url,
            @Value("${treasure.keycloak.realm}") final String realm,
            @Value("${treasure.keycloak.client}") final String client,
            @Value("${treasure.keycloak.username}") final String username,
            @Value("${treasure.keycloak.password}") final String password) {

        this.realm = realm;
        this.url = url;
        this.client = client;
        this.username = username;
        this.password = password;
    }

    /**
     * Получение пользователя.
     *
     * @param uuid Уникальный идентификатор.
     * @return Пользователь.
     */
    public Option find(final UUID uuid) {
        return findOption(uuid).orElse(emptyDTO(uuid));
    }

    /**
     * Получение опционального пользователя.
     *
     * @param uuid Уникальный идентификатор.
     * @return Пользователь.
     */
    public Optional<Option> findOption(final UUID uuid) {
        try {
            return Optional.of(users().get(GUIDs.format(uuid))
                                      .toRepresentation())
                           .map(this::toDTO);
        } catch (Exception e) {
            return Optional.of(emptyDTO(uuid));
        }
    }

    /**
     * Получение всех пользователей зарегистрированных в системе.
     *
     * @return Пользователи
     */
    public List<Option> getAll() {
        return users().list()
                      .stream()
                      .map(this::toDTO)
                      .collect(Collectors.toList());
    }

    /**
     * Подключение к Keycloak.
     *
     * @return WEB сервис Keycloak-а.
     */
    private Keycloak keycloak() {
        return KeycloakBuilder.builder()
                              .serverUrl(url)
                              .realm(realm)
                              .username(username)
                              .password(password)
                              .clientId(client)
                              .resteasyClient(new ResteasyClientBuilder()
                                              .connectionPoolSize(POOL_SIZE)
                                              .readTimeout(TIMEOUT, SECONDS)
                                              .build())
                              .build();
    }

    /**
     * Получение WEB сервиса управления пользователями.
     *
     * @return WEB сервис управления пользователями.
     */
    private UsersResource users() {
        return keycloak().realm(realm).users();
    }

    /**
     * Получение пустого (удаленного) пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @return Пользователь
     */
    private Option emptyDTO(final UUID uuid) {
        return new Option(GUIDs.format(uuid), "Удаленный пользователь");
    }

    /**
     * Преобразование пользователя из {@link UserRepresentation} в
     * {@link Option}.
     *
     * @param user Пользователь.
     * @return Пользователь.
     */
    private Option toDTO(final UserRepresentation user) {
        return new Option(user.getId(),
                          user.getFirstName() + " " + user.getLastName());
    }
}
