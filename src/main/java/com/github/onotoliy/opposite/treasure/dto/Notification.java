package com.github.onotoliy.opposite.treasure.dto;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasDeletionDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public class Notification
implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

    /**
     * Уникальный идентификатор.
     */
    private final String uuid;

    /**
     * Заголовок.
     */
    private final String name;

    /**
     * Уведомление.
     */
    private final String message;

    /**
     * Уведомления тип.
     */
    private final NotificationType notificationType;

    /**
     * Тип доставки.
     */
    private final String executor;

    /**
     * Доствленно.
     */
    private final String deliveryDate;

    /**
     * Дата создания.
     */
    private final String creationDate;

    /**
     * Автор.
     */
    private final Option author;

    /**
     * Дата удаления.
     */
    private final String deletionDate;

    /**
     * Конструтктор.
     *
     * @param uuid Уникальный идентификатор.
     * @param name Заголовок.
     * @param message Уведомление.
     * @param notificationType Тип.
     * @param executor Тип доставки.
     * @param deliveryDate Дата доставки.
     * @param creationDate Дата создания.
     * @param author Автор.
     * @param deletionDate Дата удаления.
     */
    public Notification(final String uuid,
                        final String name,
                        final String message,
                        final NotificationType notificationType,
                        final String executor,
                        final String deliveryDate,
                        final String creationDate,
                        final Option author,
                        final String deletionDate) {
        this.uuid = uuid;
        this.name = name;
        this.message = message;
        this.notificationType = notificationType;
        this.executor = executor;
        this.deliveryDate = deliveryDate;
        this.creationDate = creationDate;
        this.author = author;
        this.deletionDate = deletionDate;
    }

    /**
     * Получение уведомления.
     *
     * @return Уведомление.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Получение типа уведомления.
     *
     * @return Тип уведомления.
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Получение типа доставки.
     *
     * @return Тип Доствки.
     */
    public String getExecutor() {
        return executor;
    }

    /**
     * Получение даты доставки.
     *
     * @return Дата доставки.
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    @NotNull
    @Override
    public Option getAuthor() {
        return author;
    }

    @NotNull
    @Override
    public String getCreationDate() {
        return creationDate;
    }

    @Nullable
    @Override
    public String getDeletionDate() {
        return deletionDate;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getUuid() {
        return uuid;
    }
}
