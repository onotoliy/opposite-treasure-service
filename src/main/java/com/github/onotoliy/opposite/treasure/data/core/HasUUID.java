package com.github.onotoliy.opposite.treasure.data.core;

import java.util.UUID;

/**
 * Класс содержит поле "Уникальеный идентификатор".
 *
 * @author Anatoliy Pokhresnyi
 */
public interface HasUUID {

    /**
     * Возвращает уникальный идентификатор.
     *
     * @return Уникальный идентификатор.
     */
    UUID uuid();
}
