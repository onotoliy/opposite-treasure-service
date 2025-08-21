package com.github.onotoliy.opposite.treasure.rpc;

import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * Роль по умолчанию.
     */
    private final String role;

    /**
     * Cache пользователь.
     */
    private static Map<UUID, Option> cache = new HashMap<>();

    /**
     * Cache пользователей.
     */
    private static Map<String, User> telegram = new HashMap<>();

    /**
     * Cache списка пользователей.
     */
    private static List<User> users = new LinkedList<>();

    /**
     * Конструктор.
     *
     * @param url      URL на котором развернут Keycloak.
     * @param realm    Название realm.
     * @param client   Название клиента.
     * @param username Имя пользователя.
     * @param password Пароль.
     * @param role     Роль по умолчанию.
     */
    @Autowired
    public KeycloakRPC(
        @Value("${treasure.keycloak.url}") final String url,
        @Value("${treasure.keycloak.realm}") final String realm,
        @Value("${treasure.keycloak.client}") final String client,
        @Value("${treasure.keycloak.username}") final String username,
        @Value("${treasure.keycloak.password}") final String password,
        @Value("${treasure.roles.default}") final String role) {

        this.realm = realm;
        this.url = url;
        this.client = client;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Получение уникального идентификатора  текущего пользователя из контекста.
     *
     * @return Уникальный идентификатор пользователя.
     */
    public UUID getAuthenticationUUID() {
        return UUID.randomUUID();
    }

    /**
     * Получение текущего пользователя.
     *
     * @return Пользователь.
     */
    public Option getCurrentUser() {
        return find(getAuthenticationUUID());
    }

    /**
     * Получение ролей текущего пользователя.
     *
     * @return Роли.
     */
    public Set<String> getCurrentUserRoles() {
        return Collections.emptySet();
    }

    /**
     * Получение пользователя.
     *
     * @param uuid Уникальный идентификатор.
     * @return Пользователь.
     */
    @NotNull
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
        return Optional.of(emptyDTO(uuid));
    }

    /**
     * Получение всех пользователей зарегистрированных в системе.
     *
     * @return Пользователи
     */
    public List<User> getAll() {
        return Collections.emptyList();
    }

    /**
     * Получение пустого (удаленного) пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @return Пользователь
     */
    private Option emptyDTO(final UUID uuid) {
        return new Option(uuid, "Удаленный пользователь");
    }

}
