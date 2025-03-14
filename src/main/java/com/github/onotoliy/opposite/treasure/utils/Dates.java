package com.github.onotoliy.opposite.treasure.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

/**
 * Утилитарный класс работы с датами.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Dates {

    /**
     * Короткий формат даты.
     */
    private static final SimpleDateFormat SHORT =
        new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Формат даты с timezone.
     */
    private static final SimpleDateFormat ISO_WITHOUT_TIMEZONE =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");


    /**
     * Формат даты без timezone.
     */
    private static final SimpleDateFormat ISO_WITH_TIMEZONE =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     * Конструктор.
     */
    private Dates() {

    }

    /**
     * Получает текущую дату.
     *
     * @return Текущая дата.
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * Преобразование даты из ISO формата в которкий формат даты.
     *
     * @param value Дата.
     * @return Дата в коротком формате.
     */
    public static String toShortFormat(final Instant value) {
        return format(value, SHORT);
    }

    /**
     * Преобразование даты в определенный формат даты.
     *
     * @param value Дата.
     * @param format Фотмат даты.
     * @return Дата в коротком формате.
     */
    public static String format(final Instant value,
                                final SimpleDateFormat format) {
        return format.format(value);
    }
}
