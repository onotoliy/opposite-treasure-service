package com.github.onotoliy.opposite.treasure.rpc;

import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.User;
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
     * Конструктор.
     *
     */
    @Autowired
    public KeycloakRPC() {
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
