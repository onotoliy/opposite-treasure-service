package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import java.util.UUID;

/**
 * Объект уведомления.
 */
public class NotificationObject {

    /**
     * Тип.
     */
    private final NotificationType type;

    /**
     * Уникальный идентификатор объекта.
     */
    private final UUID object;

    /**
     * Конструктор.
     *
     * @param type Тип.
     * @param object Уникальный идентификатор объекта.
     */
    public NotificationObject(
        final NotificationType type,
        final UUID object
    ) {
        this.type = type;
        this.object = object;
    }

    /**
     * Получение типа.
     *
     * @return Тип.
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Получение уникального идентификатора объекта.
     * @return Уникальный идентификатор объекта.
     */
    public UUID getObject() {
        return object;
    }
}
