package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    @JsonCreator
    public NotificationObject(
        final @JsonProperty("type") NotificationType type,
        final @JsonProperty("object") UUID object
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

    /**
     * Преобразование объекта в Json.
     *
     * @param object Объект уведомления.
     * @return Объект уведомления в формате JSON.
     */
    public static String toJSON(final NotificationObject object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Преобразование объекта в объект.
     *
     * @param json Объект уведомления в формате JSON.
     * @return Объект уведомления.
     */
    public static NotificationObject fromJSON(final String json) {
        try {
            return new ObjectMapper().readValue(json, NotificationObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
