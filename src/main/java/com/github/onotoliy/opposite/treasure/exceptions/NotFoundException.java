package com.github.onotoliy.opposite.treasure.exceptions;

import org.jooq.Named;

import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> template, UUID uuid) {
        super("");
    }

    public NotFoundException(Named table, UUID uuid) {
        super(String.format(
            "Запись с унакльный идентификатором %s в таблице %s не найдена",
            table.getName(), GUID.format(uuid)));
    }
}
