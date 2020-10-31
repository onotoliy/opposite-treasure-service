package com.github.onotoliy.opposite.treasure.exceptions;

/**
 * Ошибка отправки уведомления.
 *
 * @author Anatoliy Pokhresnyi
 */
public class NotificationException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param throwable Ошибка.
     */
    public NotificationException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Конструктор.
     *
     * @param message Сообщение от ошибке.
     */
    public NotificationException(final String message) {
        super(message);
    }
}
