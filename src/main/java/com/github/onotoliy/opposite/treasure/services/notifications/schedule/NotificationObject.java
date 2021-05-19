package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import com.github.onotoliy.opposite.data.core.HasUUID;

/**
 * Объект уведомления.
 */
public class NotificationObject {

    /**
     * Тип.
     */
    private final NotificationType type;

    /**
     * Объект.
     */
    private final HasUUID object;

    /**
     * Конструктор.
     *
     * @param type Тип.
     * @param object Уникальный идентификатор объекта.
     */
    public NotificationObject(
        final NotificationType type,
        final HasUUID object
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
    public HasUUID getObject() {
        return object;
    }

}
