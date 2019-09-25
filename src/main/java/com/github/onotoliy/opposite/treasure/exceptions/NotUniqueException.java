package com.github.onotoliy.opposite.treasure.exceptions;

import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.util.UUID;

import org.jooq.Table;

/**
 * Объект с указанным уникальным идентификатором уже существует.
 *
 * @author Anatoliy Pokhresnyi
 */
public class NotUniqueException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param table Название таблицы.
     * @param uuid Уникальный идентификатор.
     */
    public <T extends Table<?>> NotUniqueException(final T table, final UUID uuid) {
        super(String.format(
            "Запись с уникальный идентификатором %s в таблице %s уже существует",
            table.getName(), GUIDs.format(uuid)));
    }
}
