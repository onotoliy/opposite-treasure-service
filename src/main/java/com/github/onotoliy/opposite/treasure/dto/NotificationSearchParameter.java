package com.github.onotoliy.opposite.treasure.dto;

import com.github.onotoliy.opposite.treasure.utils.Objects;

/**
 * Поисковые параметры для уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
public class NotificationSearchParameter extends SearchParameter {

    /**
     * Тип уведомления.
     */
    private final NotificationType type;

    /**
     * Доставленно.
     */
    private final Delivery delivery;

    /**
     * Конструктор.
     *
     * @param type Тип уведомления.
     * @param delivery Доставленно.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     */
    public NotificationSearchParameter(final NotificationType type,
                                       final Delivery delivery,
                                       final int offset,
                                       final int numberOfRows) {
        super(offset, numberOfRows);

        this.type = type;
        this.delivery = delivery;
    }

    /**
     * Проверяет необходимо ли производить поиск по типу уведомления.
     *
     * @return Результат проверки.
     */
    public boolean hasType() {
        return Objects.nonEmpty(type);
    }

    /**
     * Получает тип уведомления.
     *
     * @return Тип уведомления.
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Проверяет необходимо ли производить поиск по признаку доставки.

     * @return Результат проверки.
     */
    public boolean hasDelivery() {
        return Objects.nonEmpty(delivery);
    }

    /**
     * Получает признак доставки.
     *
     * @return Признак доставки.
     */
    public Delivery getDelivery() {
        return delivery;
    }
}
