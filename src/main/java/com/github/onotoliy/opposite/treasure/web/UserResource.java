package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

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
        return user.find(GUID.parse("8098207c-6fbb-45ec-ae10-184607262a1a"));
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
