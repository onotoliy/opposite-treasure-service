package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WEB сервис чтения пользователей системы.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/user")
public class UserResource {

    /**
     * Сервис чтения пользователей системы.
     */
    private final KeycloakRPC user;

    /**
     * Сервис уведомлений.
     */
    private final NotificationService notification;

    /**
     * Конструктор.
     *
     * @param user Сервис чтения пользователей системы.
     * @param notification Сервис уведомлений.
     */
    @Autowired
    public UserResource(final KeycloakRPC user,
                        final NotificationService notification) {
        this.user = user;
        this.notification = notification;
    }

    /**
     * Получение текущего пользователя.
     *
     * @return Текущий пльзователь.
     */
    @GetMapping(value = "/current")
    public Option getCurrentUser() {
        return user.getCurrentUser();
    }

    /**
     * Получение ролей текущего пользователя.
     *
     * @return Роли.
     */
    @GetMapping(value = "/current/roles")
    public Set<String> getCurrentUserRoles() {
        return user.getCurrentUserRoles();
    }

    /**
     * Получение списка всех пользователей, зарегистрированных в системе.
     *
     * @return Пользователи.
     */
    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return user.getAll()
                   .stream()
                   .map(e -> new Option(e.getUuid(), e.getName()))
                   .collect(Collectors.toList());
    }

    /**
     * Получение списка всех пользователей, зарегистрированных в системе.
     *
     * @return Пользователи.
     */
    @GetMapping(value = "/list/full")
    public List<User> getFullDTOAll() {
        return user.getAll();
    }

    /**
     * Отправка отчета о долгах.
     */
    @PostMapping(value = "/notification")
    public void notification() {
        notification.debts();
        notification.deposit();
    }
}
