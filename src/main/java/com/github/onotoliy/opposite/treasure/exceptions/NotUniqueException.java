package com.github.onotoliy.opposite.treasure.exceptions;

import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.util.UUID;

import org.jooq.Named;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Объект с указанным уникальным идентификатором уже существует.
 *
 * @author Anatoliy Pokhresnyi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotUniqueException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param table Название таблицы.
     * @param uuid Уникальный идентификатор.
     */
    public NotUniqueException(final Named table, final UUID uuid) {

        super(String.format(
            "Запись с уникальный идентификатором %s в таблице %s уже "
            + "существует", table.getName(), GUIDs.format(uuid)));
    }
}
