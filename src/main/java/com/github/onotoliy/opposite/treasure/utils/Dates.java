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
    public static Timestamp now() {
        return parse(new Date());
    }


    /**
     * Преобразование {@link String} в {@link Timestamp}.
     *
     * @param value Значение в формате {@link String}.
     * @return Значение в формате {@link Timestamp}.
     */
    public static Timestamp parse(final String value) {
        if (Strings.isEmpty(value)) {
            return null;
        }

        String v = value.endsWith("Z")
            ? value.substring(0, value.length() - 1)
            : value;

        try {
            return value.endsWith("Z")
                ? parse(ISO_WITHOUT_TIMEZONE.parse(v))
                : parse(ISO_WITH_TIMEZONE.parse(v));
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Преобразование {@link Timestamp} в {@link String}.
     *
     * @param value Значение в формате {@link Timestamp}.
     * @return Значение в формате {@link String}.
     */
    @NotNull
    public static String format(final Timestamp value) {
        return value == null ? "—" : ISO_WITH_TIMEZONE.format(value);
    }

    /**
     * Преобразование {@link Instant} в {@link Timestamp}.
     *
     * @param value Значение в формате {@link Instant}.
     * @return Значение в формате {@link Timestamp}.
     */
    private static Timestamp parse(final Instant value) {
        return value == null ? null : new Timestamp(value.toEpochMilli());
    }

    /**
     * Преобразование {@link Date} в {@link Timestamp}.
     *
     * @param value Значение в формате {@link Date}.
     * @return Значение в формате {@link Timestamp}.
     */
    private static Timestamp parse(final Date value) {
        return value == null ? null : parse(value.toInstant());
    }

    /**
     * Чтение из {@link Record} значения колонки в формате {@link Timestamp}
     * и преобразование его в {@link String}.
     *
     * @param record Запись.
     * @param field Колонка.
     * @return Значение в формате {@link String}.
     */
    public static String format(final Record record,
                                final Field<Timestamp> field) {
        return format(record.getValue(field, Timestamp.class));
    }
}
