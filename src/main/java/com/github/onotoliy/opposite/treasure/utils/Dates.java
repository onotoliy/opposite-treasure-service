package com.github.onotoliy.opposite.treasure.utils;

import java.time.Instant;

/**
 * Утилитарный класс работы с датами.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Dates {

    /**
     * Конструктор.
     */
    private Dates() {

    }

    /**
     * Преобразование строки в момент времени.
     *
     * @param value Строка.
     * @return Момент всремени.
     */
    public static Instant toInstant(final String value) {
        return Strings.isEmpty(value) ? null : Instant.parse(value);

    }

    /**
     * Преобразование момента времени в строку.
     *
     * @param value Момент всремени
     * @return Строка.
     */
    public static String toString(final Instant value) {
        return value == null ? null : value.toString();
    }
}
