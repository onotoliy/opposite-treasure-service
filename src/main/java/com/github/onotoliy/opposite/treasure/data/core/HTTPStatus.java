package com.github.onotoliy.opposite.treasure.data.core;

/**
 * Cтатус ответа сервера.
 *
 * @author Anatoliy Pokhresnyi
 */
public enum HTTPStatus {

    /**
     * 500. Internal Server Error.
     */
    INTERNAL_SERVER_ERROR,

    /**
     * 400. Bad Request.
     */
    BAD_REQUEST,

    /**
     * 404. Not Found.
     */
    NOT_FOUND,

    /**
     * 409. Conflict.
     */
    CONFLICT
}
