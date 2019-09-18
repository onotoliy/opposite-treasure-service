package com.github.onotoliy.opposite.treasure.exceptions;

import org.jooq.Table;

import java.util.UUID;

public class NotUniqueException extends RuntimeException {

    public <T extends Table<?>> NotUniqueException(T table, UUID uuid) {

    }
}
