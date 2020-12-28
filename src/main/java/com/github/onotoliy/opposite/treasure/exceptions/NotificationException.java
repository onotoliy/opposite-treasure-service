package com.github.onotoliy.opposite.treasure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка отправки уведомления.
 *
 * @author Anatoliy Pokhresnyi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
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
