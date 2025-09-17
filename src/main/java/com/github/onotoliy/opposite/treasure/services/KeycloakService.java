package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.data.Position;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис управдения пользвателями из Keycloak.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class KeycloakService {

    /**
     * Сервис управления пользователями в Keycloak.
     */
    private final UsersResource users;

    /**
     * Сервис управления ролями в Keycloak.
     */
    private final RolesResource roles;

    /**
     * Конструктор.
     *
     * @param baseURL  URL до Keycloak.
     * @param realm    Realm.
     * @param clientID Client ID
     * @param username Username системного пользователя.
     * @param password Пароль системного пользователя.
     */
    @Autowired
    public KeycloakService(
        @Value("${treasure.keycloak.base-url}") final String baseURL,
        @Value("${treasure.keycloak.realm}") final String realm,
        @Value("${treasure.keycloak.client-id}") final String clientID,
        @Value("${treasure.keycloak.username}") final String username,
        @Value("${treasure.keycloak.password}") final String password
    ) {
        final Keycloak keycloak = KeycloakBuilder
            .builder()
            .serverUrl(baseURL)
            .realm(realm)
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(clientID)
            .username(username)
            .password(password)
            .build();

        users = keycloak.realm("treasure").users();
        roles = keycloak.realm("treasure").roles();
    }

    /**
     * Получение пользователя по уникальному идентификатору.
     *
     * @param uuid  Уникальный идентификатор.
     * @param money Фукция получения депозита пользователя.
     * @return Пользователь.
     */
    public Deposit get(
        final UUID uuid,
        final Function<UUID, BigDecimal> money
    ) {
        return toDTO(users.get(uuid.toString()).toRepresentation(), money);
    }

    /**
     * Получение количество пользователей.
     *
     * @param parameter Поисковые параметры.
     * @return Количество пользователей.
     */
    public Integer count(final DepositSearchParameter parameter) {
        return users.search(
            parameter.q(),
            parameter.enable(),
            0,
            1000
        ).size();
    }

    /**
     * Поиск пользователей.
     *
     * @param parameter Поисковые параметры.
     * @param money     Фукция получения депозита пользователя.
     * @return Пользователи.
     */
    public List<Deposit> getAll(
        final DepositSearchParameter parameter,
        final Function<UUID, BigDecimal> money
    ) {
        return users
            .search(
                parameter.q(),
                parameter.enable(),
                parameter.offset(),
                parameter.numberOfRows()
            )
            .stream()
            .map(it -> toDTO(it, money))
            .collect(Collectors.toList());
    }

    /**
     * Создание пользователя в Keycloak.
     *
     * @param deposit Пользователь.
     * @return Пользователь.
     */
    public UUID create(final Deposit deposit) {
        users.create(toRepresentation(deposit));

        final UserRepresentation representation = users
            .list()
            .stream()
            .filter(it -> it.getUsername().equals(deposit.username()))
            .findFirst()
            .orElse(null);

        if (representation == null) {
            throw new ModificationException(
                "Не удалось создать пользователя в системе");
        }

        final String username = deposit.username();
        setTemporaryPassword(
            representation.getId(),
            username.substring(Math.max(username.length() - 4, 0))
        );

        setPosition(GUIDs.parse(representation.getId()), deposit.position());

        return UUID.fromString(representation.getId());
    }

    /**
     * Изменение пользователя в Keycloak.
     *
     * @param deposit Пользователь.
     * @return Пользователь.
     */
    public UUID update(final Deposit deposit) {
        if (Objects.isEmpty(deposit.uuid())) {
            throw new ModificationException(
                "Не удалось изменить пользователя в системе");
        }

        final UserRepresentation representation =
            toRepresentation(deposit);

        users.get(deposit.uuid().toString()).update(representation);

        setPosition(deposit.uuid(), deposit.position());

        return deposit.uuid();
    }

    /**
     * Создание временного пароля пользователя.
     *
     * @param uuid     Уникальный идентификатор пользователя.
     * @param password Пароль.
     */
    public void setTemporaryPassword(final String uuid, final String password) {
        final CredentialRepresentation representation =
            new CredentialRepresentation();
        representation.setType(OAuth2Constants.PASSWORD);
        representation.setValue(password);
        representation.setTemporary(true);

        users.get(uuid).resetPassword(representation);
    }

    /**
     * Установка должности пользователя.
     *
     * @param uuid     Уникальный идентификатор пользователя.
     * @param position Должность
     */
    public void setPosition(final UUID uuid, final Position position) {
        users
            .get(uuid.toString())
            .roles()
            .realmLevel()
            .remove(Stream
                        .of(Position.values())
                        .map(Position::name)
                        .map(String::toLowerCase)
                        .map(roles::get)
                        .map(RoleResource::toRepresentation)
                        .collect(Collectors.toUnmodifiableList())
            );


        users
            .get(uuid.toString())
            .roles()
            .realmLevel()
            .add(Collections.singletonList(roles.get(position.name().toLowerCase()).toRepresentation()));
    }

    /**
     * Удаление пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
     */
    public void delete(final UUID uuid) {
        final UserRepresentation representation =
            users.get(uuid.toString()).toRepresentation();

        representation.setEnabled(false);

        users.get(uuid.toString()).update(representation);
    }

    /**
     * Добавление атрибута пользователя.
     *
     * @param attributes Список атрибутов.
     * @param key        Ключ.
     * @param value      Значение.
     */
    private void setSingleAttribute(
        final Map<String, List<String>> attributes,
        final String key,
        final String value
    ) {
        if (Strings.isEmpty(value) || Strings.isEmpty(key)) {
            return;
        }

        attributes.put(key, List.of(value));
    }

    /**
     * Добавление атрибута пользователя.
     *
     * @param attributes Список атрибутов.
     * @param key        Ключ.
     * @param value      Значение.
     */
    private void setSingleAttribute(
        final Map<String, List<String>> attributes,
        final String key,
        final Instant value
    ) {
        if (value == null || Strings.isEmpty(key)) {
            return;
        }

        attributes.put(key, List.of(Dates.toString(value)));
    }

    /**
     * Преобразование UserRepresentation в депозит.
     *
     * @param representation UserRepresentation.
     * @param money          Фукция получения депозита пользователя.
     * @return Депозит.
     */
    private Deposit toDTO(
        final UserRepresentation representation,
        final Function<UUID, BigDecimal> money
    ) {
        final UUID uuid = GUIDs.parse(representation.getId());

        return new Deposit(
            uuid,
            representation.getUsername(),
            representation.getFirstName(),
            representation.getLastName(),
            representation.firstAttribute("patronymic"),
            money.apply(uuid),
            representation.firstAttribute("logo"),
            representation.getEmail(),
            Dates.toInstant(representation.firstAttribute("birthday")),
            Dates.toInstant(representation.firstAttribute("joiningDate")),
            toPosition(users.get(representation.getId()).roles().realmLevel().listAll())
        );
    }

    /**
     * Преобразование роли в должность.
     *
     * @param roles Список ролей.
     * @return Должность.
     */
    private Position toPosition(final List<RoleRepresentation> roles) {
        if (roles == null || roles.isEmpty()) {
            return Position.NONE;
        }

        Predicate<Position> predicate = position -> roles
            .stream().anyMatch(e -> Strings.equals(e.getName(), position.name(), true));

        if (predicate.test(Position.PRESIDENT)) {
            return Position.PRESIDENT;
        }

        if (predicate.test(Position.VICE_PRESIDENT)) {
            return Position.VICE_PRESIDENT;
        }

        if (predicate.test(Position.TREASURER)) {
            return Position.TREASURER;
        }

        if (predicate.test(Position.SECRETARY)) {
            return Position.SECRETARY;
        }

        if (predicate.test(Position.MEMBER)) {
            return Position.MEMBER;
        }

        if (predicate.test(Position.FRIEND)) {
            return Position.FRIEND;
        }

        return Position.NONE;
    }

    /**
     * Преобразование пользователя в UserRepresentation.
     *
     * @param deposit Пользователь.
     * @return UserRepresentation.
     */
    private UserRepresentation toRepresentation(
        final Deposit deposit
    ) {
        final Map<String, List<String>> attributes = new HashMap<>();
        setSingleAttribute(attributes, "patronymic", deposit.patronymic());
        setSingleAttribute(attributes, "logo", deposit.logo());
        setSingleAttribute(attributes, "birthday", deposit.birthday());
        setSingleAttribute(attributes, "joiningDate", deposit.joiningDate());

        final UserRepresentation representation = new UserRepresentation();
        if (Objects.nonEmpty(deposit.uuid())) {
            representation.setId(deposit.uuid().toString());
        }
        representation.setUsername(deposit.username());
        representation.setEmail(deposit.email());
        representation.setEmailVerified(true);
        representation.setEnabled(true);
        representation.setFirstName(deposit.firstName());
        representation.setLastName(deposit.lastName());
        representation.setAttributes(attributes);
        representation.setRealmRoles(
            Collections.singletonList(deposit.position().name().toLowerCase())
        );

        return representation;
    }

}
