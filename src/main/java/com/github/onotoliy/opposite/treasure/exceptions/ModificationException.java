package com.github.onotoliy.opposite.treasure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка изменения данных.
 *
 * @author Anatoliy Pokhresnyi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ModificationException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message Сообщение об ошибке.
     */
    public ModificationException(final String message) {
        super(message);
    }
}
