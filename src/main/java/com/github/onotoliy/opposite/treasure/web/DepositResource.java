package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.services.DepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис чтения депозитов.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/deposit")
public class DepositResource {

    /**
     * Сервис.
     */
    private final DepositService service;

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public DepositResource(final DepositService service) {
        this.service = service;
    }

    /**
     * Получение депозита текущего пользователя.
     *
     * @return Депозит.
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение депозита авторизованного пользователя")
    public Deposit get() {
        return service.me();
    }

    /**
     * Получение депозита пользователя.
     *
     * @param uuid Пользователь.
     * @return Депозит.
     */
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение депозита пользователя")
    public Deposit get(
        @Parameter(
            description = "Уникальный идентификатор депозита",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("uuid") final UUID uuid
    ) {
        return service.get(uuid);
    }

    /**
     * Получение списка долгов пользователя.
     *
     * @param uuid         Уникальный идентификатор депозита.
     * @param offset       Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Список долгов пользователя.
     */
    @GetMapping(
        value = "/{uuid}/debts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Получение списка долгов пользователя")
    public Page<Event> getDebts(
        @Parameter(
            description = "Уникальный идентификатор депозита",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("uuid") final UUID uuid,
        @Parameter(
            description = "Количество записей которое необходимо пропустить"
        )
        @RequestParam(value = "offset", required = false, defaultValue = "10")
        final int offset,
        @Parameter(description = "Размер страницы")
        @RequestParam(
            value = "numberOfRows", required = false, defaultValue = "10"
        )
        final int numberOfRows
    ) {
        return service.getDebts(uuid, offset, numberOfRows);
    }

    /**
     * Поиск депозитов.
     *
     * @param parameter Поисковые параметры.
     * @return События.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Поиск депозитов")
    public Page<Deposit> getAll(
        @ParameterObject final DepositSearchParameter parameter
    ) {
        return service.getAll(parameter);
    }

    /**
     * Создание депозита.
     *
     * @param dto Депозит.
     * @return Депозит.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание депозита")
    public Deposit create(
        @Parameter(description = "Депозит") final Deposit dto
    ) {
        return service.create(dto);
    }

    /**
     * Изменение депозита.
     *
     * @param dto Депозит.
     * @return Депозит.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Изменение депозита")
    public Deposit update(
        @Parameter(description = "Депозит") final Deposit dto
    ) {
        return service.update(dto);
    }

    /**
     * Удаление депозита.
     *
     * @param uuid никальный идентификатор депозита.
     */
    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Удаление депозита")
    public void delete(
        @Parameter(
            description = "Уникальный идентификатор депозита",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("uuid") final UUID uuid
    ) {
        service.delete(uuid);
    }

}
