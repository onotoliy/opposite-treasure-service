package com.github.onotoliy.opposite.treasure.data.core;

/**
 * Информация об ошибке.
 *
 * @param status Статус.
 * @param message Сообщение об ошибке
 * @author Anatoliy Pokhresnyi
 */
public record ExceptionInformation(
        HTTPStatus status,
        String message
) {

}
