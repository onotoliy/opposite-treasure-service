package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.services.IEventService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/event")
public class EventResource
    extends AbstractModifierResource<
        Event,
        EventSearchParameter,
        IEventService> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public EventResource(final IEventService service) {
        super(service);
    }

    /**
     * Получение списка должников.
     *
     * @param uuid Уникальный идентификатор события.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Списк должников.
     */
    @GetMapping(value = "/{uuid}/debtors", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение списка должников")
    public Page<Deposit> getDebtors(
        @Parameter(
            description = "Уникальный идентификатор события",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("uuid") final UUID uuid,
        @Parameter(description = "Количество записей которое необходимо пропустить")
        @RequestParam(value = "offset", required = false, defaultValue = "10")
        final int offset,
        @Parameter(description = "Размер страницы")
        @RequestParam(value = "numberOfRows", required = false, defaultValue = "10")
        final int numberOfRows
    ) {
        return service.getDebtors(uuid, offset, numberOfRows);
    }
}
