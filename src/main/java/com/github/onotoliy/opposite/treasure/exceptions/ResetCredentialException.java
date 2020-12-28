package com.github.onotoliy.opposite.treasure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка сброса пароля.
 *
 * @author Anatoliy Pokhresnyi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResetCredentialException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message Сообщение.
     */
    public ResetCredentialException(final String message) {
        super(message);
    }

}
