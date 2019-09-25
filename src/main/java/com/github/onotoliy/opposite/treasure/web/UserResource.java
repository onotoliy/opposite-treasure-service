package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final UserRPC user;

    /**
     * Конструктор.
     *
     * @param user Сервис чтения пользователей системы.
     */
    @Autowired
    public UserResource(final UserRPC user) {
        this.user = user;
    }

    /**
     * Получение текущего пользователя.
     *
     * @return Текущий пльзователь.
     */
    @GetMapping(value = "/current")
    public Option getCurrentUser() {
        return user.find(GUIDs.parse("8098207c-6fbb-45ec-ae10-184607262a1a"));
    }

    /**
     * Получение списка всех пользователей, зарегистрированных в системе.
     *
     * @return Пользователи.
     */
    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return user.getAll();
    }
}
