package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Конструктор.
     */
    @Autowired
    public KeycloakService() {
        final Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("https://91.201.41.66/auth")
                .realm("treasure")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username("rest-admin")
                .password("rest-admin-password")
                .build();

        users = keycloak.realm("treasure").users();
    }

    /**
     * Получение пользователя по уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор.
     * @return Пользователь.
     */
    public UserRepresentation get(final UUID uuid) {
        return users.get(uuid.toString()).toRepresentation();
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
     * @return Пользователи.
     */
    public List<UserRepresentation> getAll(
        final DepositSearchParameter parameter
    ) {
        return users.search(
            parameter.q(),
            parameter.enable(),
            parameter.offset(),
            parameter.numberOfRows()
        );
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

        return deposit.uuid();
    }

    /**
     * Создание временного пароля пользователя.
     *
     * @param uuid Уникальный идентификатор пользователя.
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
     * @param key Ключ.
     * @param value Значение.
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
     * @param key Ключ.
     * @param value Значение.
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
     * Преобразование пользователя в UserRepresentation.
     *
     * @param uuid Уникальный идентификатор пользователя.
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

        return representation;
    }

}
