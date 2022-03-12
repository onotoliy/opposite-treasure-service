package com.github.onotoliy.opposite.treasure.rpc;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.dto.Contact;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return GUIDs.parse(getKeycloakPrincipal().getName());
    }

    /**
     * Получение текущего пользователя в формате {@link KeycloakPrincipal} из
     * контекста.
     *
     * @return Пользователь в формате {@link KeycloakPrincipal}
     */
    public KeycloakPrincipal getKeycloakPrincipal() {
        return (KeycloakPrincipal) SecurityContextHolder.getContext()
                                                        .getAuthentication()
                                                        .getPrincipal();
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
        return getKeycloakPrincipal().getKeycloakSecurityContext()
                                     .getToken()
                                     .getRealmAccess()
                                     .getRoles();
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
        try {
            Option user = cache.get(uuid);

            if (Objects.nonEmpty(user)) {
                return Optional.of(user);
            }

            Optional<Option> optional = Optional
                .of(keycloak().realm(realm)
                              .users()
                              .get(GUIDs.format(uuid))
                              .toRepresentation())
                .map(this::toDTO)
                .map(e -> new Option(e.getUuid(), e.getName()));

            cache.put(uuid, optional.get());

            return optional;
        } catch (Exception e) {
            return Optional.of(emptyDTO(uuid));
        }
    }

    /**
     * Получение Username-ов в телеграмме.
     *
     * @return Username-ы в телеграмме.
     */
    public Set<String> getTelegramUsernames() {
        if (users.isEmpty()) {
            getAll();
        }

        return telegram.keySet();
    }

    /**
     * Получение всех пользователей зарегистрированных в системе.
     *
     * @return Пользователи
     */
    public List<User> getAll() {
        if (users.isEmpty()) {
            final Set<UserRepresentation> representations = keycloak()
                .realm(realm)
                .roles()
                .get(role)
                .getRoleUserMembers();

            for (UserRepresentation representation: representations) {
                Map<String, List<String>> attributes =
                    representation.getAttributes() == null
                        ? Collections.emptyMap()
                        : representation.getAttributes();
                List<String> values = attributes
                    .getOrDefault("telegram", Collections.emptyList());

                if (!values.isEmpty()) {
                    telegram.put(values.get(0), toDTO(representation));
                }
            }

            users.addAll(
                representations
                    .stream()
                    .map(this::toDTO)
                    .sorted(Comparator.comparing(User::getName))
                    .collect(Collectors.toList())
            );
        }

        return users;
    }

    /**
     * Получение контактной информации пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @return Контактная информация пользователя.
     */
    public Contact getContact(final String uuid) {
        return Optional.of(keycloak().realm(realm)
                                     .users()
                                     .get(uuid)
                                     .toRepresentation())
                       .map(this::toContactDTO)
                       .orElse(emptyContactDTO(uuid));
    }

    /**
     * Получение всех ролей пользователя.
     *
     * @param uuid Уникальный идентификатор.
     * @return Список ролей.
     */
    private Set<String> getAllRoles(final String uuid) {
        return keycloak().realm(realm)
                         .users()
                         .get(uuid)
                         .roles()
                         .getAll()
                         .getRealmMappings()
                         .stream()
                         .map(RoleRepresentation::getName)
                         .collect(Collectors.toSet());
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
     * Получение пустого (удаленного) пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @return Пользователь
     */
    private Option emptyDTO(final UUID uuid) {
        return new Option(GUIDs.format(uuid), "Удаленный пользователь");
    }

    /**
     * Получение пустой (удаленной) контактной информации пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     * @return Контактная информация пользователя.
     */
    private Contact emptyContactDTO(final String uuid) {
        return new Contact(
            uuid, null, false, null, false, null, false, null, false
        );
    }

    /**
     * Преобразование пользователя из {@link UserRepresentation} в
     * {@link User}.
     *
     * @param user Пользователь.
     * @return Пользователь.
     */
    private User toDTO(final UserRepresentation user) {
        return new User(
            user.getId(),
            toName(user.getFirstName(), user.getLastName(), user.getUsername()),
            user.getUsername(),
            Strings.isEmpty(user.getEmail()) ? "" : user.getEmail(),
            toFirstAttribute("phone", user.getAttributes(), ""),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByPhone", user.getAttributes(), "false")),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByEmail", user.getAttributes(), "true")),
            getAllRoles(user.getId())
        );
    }

    /**
     * Преобразование контактной информации пользователя из
     * {@link UserRepresentation} в {@link Contact}.
     *
     * @param user Пользователь.
     * @return Контактная информация пользователя.
     */
    private Contact toContactDTO(final UserRepresentation user) {
        return new Contact(
            user.getId(),
            user.getEmail(),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByEmail", user.getAttributes(), "true")),
            toFirstAttribute("phone", user.getAttributes(), ""),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByPhone", user.getAttributes(), "false")),
            toFirstAttribute("telegram", user.getAttributes(), ""),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByTelegram", user.getAttributes(), "false")),
            toFirstAttribute("firebase", user.getAttributes(), ""),
            Boolean.parseBoolean(toFirstAttribute(
                "notifyByFirebase", user.getAttributes(), "false"))
        );
    }

    /**
     * Получение первого атрибута из списка.
     *
     * @param key          Ключ атрибута.
     * @param attributes   Список атрибутов.
     * @param defaultValue Значение по умолчанию.
     * @return Значение атрибута или если его нет значение по умолчанию.
     */
    private String toFirstAttribute(final String key,
                                    final Map<String, List<String>> attributes,
                                    final String defaultValue) {
        if (Objects.isEmpty(attributes)) {
            return defaultValue;
        }

        List<String> list = attributes.get(key);

        if (Objects.isEmpty(list)) {
            return defaultValue;
        }

        String value = list.get(0);

        return Strings.isEmpty(value) ? defaultValue : value;
    }

    /**
     * Получение имени пользователя.
     *
     * @param firstName Имя.
     * @param lastName  Фамилия.
     * @param username  Логин.
     * @return Имя пользователя.
     */
    private String toName(final String firstName,
                          final String lastName,
                          final String username) {
        if (Strings.nonEmpty(firstName) && Strings.nonEmpty(lastName)) {
            return firstName + " " + lastName;
        }

        if (Strings.nonEmpty(firstName) || Strings.nonEmpty(lastName)) {
            return Strings.nonEmpty(firstName) ? firstName : lastName;
        }

        return username;
    }
}
