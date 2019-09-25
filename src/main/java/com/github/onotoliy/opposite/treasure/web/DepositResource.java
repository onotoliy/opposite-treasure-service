package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.services.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
     * Получение депозита пользователя.
     *
     * @param uuid Пользователь.
     * @return Депозит.
     */
    @GetMapping(value = "/{uuid}")
    public Deposit get(@PathVariable("uuid") final UUID uuid) {
        return service.get(uuid);
    }

    /**
     * Поиск депозитов.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return События.
     */
    @GetMapping
    public Page<Deposit> getAll(
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            final int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            final int numberOfRows) {
        return service.getAll(new DepositSearchParameter(offset, numberOfRows));
    }
}
