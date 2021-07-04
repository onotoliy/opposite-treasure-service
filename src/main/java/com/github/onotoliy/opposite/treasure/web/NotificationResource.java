package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.Delivery;
import com.github.onotoliy.opposite.treasure.dto.Notification;
import com.github.onotoliy.opposite.treasure.dto.NotificationSearchParameter;
import com.github.onotoliy.opposite.treasure.dto.NotificationType;
import com.github.onotoliy.opposite.treasure.services.INotificationService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис управления уведомлениями.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/notification")
public class NotificationResource
extends AbstractModifierResource<
    Notification,
    NotificationSearchParameter,
    INotificationService> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public NotificationResource(final INotificationService service) {
        super(service);
    }

    /**
     * Поиск уведомлений.
     *
     * @param type Тип уведомления.
     * @param delivery Доставленно.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Транзакции.
     */
    @GetMapping
    public Page<Notification> getAll(
            @RequestParam(value = "type",
                          required = false)
            final NotificationType type,
            @RequestParam(value = "delivery",
                          required = false,
                          defaultValue = "ALL")
            final Delivery delivery,
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            final int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            final int numberOfRows
    ) {
        final NotificationSearchParameter parameter =
            new NotificationSearchParameter(
                    type, delivery, offset, numberOfRows
            );

        return service.getAll(parameter);
    }
}
