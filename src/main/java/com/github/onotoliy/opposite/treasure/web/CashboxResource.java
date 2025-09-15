package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.treasure.data.Cashbox;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис чтения данных о кассе.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/cashbox")
public class CashboxResource {

    /**
     * Сервис.
     */
    private final ICashboxService service;

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public CashboxResource(final ICashboxService service) {
        this.service = service;
    }

    /**
     * Получение данных о кассе.
     *
     * @return Данные о кассе.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации о кассе")
    public Cashbox get() {
        return service.get();
    }
}
