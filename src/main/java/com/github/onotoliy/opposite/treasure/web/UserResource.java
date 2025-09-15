package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.User;
import com.github.onotoliy.opposite.treasure.data.core.ExceptionDevice;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.IExceptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
     * Logger.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserResource.class);

    /**
     * Сервис чтения пользователей системы.
     */
    private final KeycloakRPC user;

    /**
     * Сервис ошибок устройств.
     */
    private final IExceptionService exception;

    /**
     * Конструктор.
     *
     * @param user Сервис чтения пользователей системы.
     * @param exception Сервис ошибок устройств.
     */
    @Autowired
    public UserResource(final KeycloakRPC user,
                        final IExceptionService exception) {
        this.user = user;
        this.exception = exception;
    }

    /**
     * Получение текущего пользователя.
     *
     * @return Текущий пльзователь.
     */
    @GetMapping(value = "/current")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получение объекта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
            }
    )
    public Option getCurrentUser() {
        return user.getCurrentUser();
    }

    /**
     * Получение ролей текущего пользователя.
     *
     * @return Роли.
     */
    @GetMapping(value = "/current/roles")
    @Operation(
            summary = "Получение объекта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
            }
    )
    public Set<String> getCurrentUserRoles() {
        return user.getCurrentUserRoles();
    }

    /**
     * Получение списка всех пользователей, зарегистрированных в системе.
     *
     * @return Пользователи.
     */
    @GetMapping(value = "/list")
    @Operation(
            summary = "Получение объекта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
            }
    )
    public List<Option> getAll() {
        return user.getAll()
                   .stream()
                   .map(e -> new Option(e.uuid(), e.name()))
                   .collect(Collectors.toList());
    }

    /**
     * Получение списка всех пользователей, зарегистрированных в системе.
     *
     * @return Пользователи.
     */
    @GetMapping(value = "/list/full")
    @Operation(
            summary = "Получение объекта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
            }
    )
    public List<User> getFullDTOAll() {
        return user.getAll();
    }

    /**
     * Регистрация ошибки на клиенте.
     *
     * @param exception Описание ошибки устройства.
     */
    @PostMapping(value = "/register/exception")
    @Operation(
            summary = "Получение объекта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
            }
    )
    public void registration(@RequestBody final ExceptionDevice exception) {
        this.exception.registration(exception);
    }
}
