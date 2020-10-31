package com.github.onotoliy.opposite.treasure.exceptions;

/**
 * Ошибка сброса пароля.
 *
 * @author Anatoliy Pokhresnyi
 */
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
