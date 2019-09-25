package com.github.onotoliy.opposite.treasure.exceptions;

import org.jooq.Named;

import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

/**
 * Объект с указанным уникальным идентификатором не был найден.
 *
 * @author Anatoliy Pokhresnyi
 */
public class NotFoundException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param table Название таблицы.
     * @param uuid Уникальный идентификатор.
     */
    public NotFoundException(final Named table, final UUID uuid) {
        super(String.format(
            "Запись с уникальный идентификатором %s в таблице %s не найдена",
            table.getName(), GUID.format(uuid)));
    }
}
