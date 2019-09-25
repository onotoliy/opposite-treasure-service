package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

/**
 * Утилитарный класс работы со строками.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Strings {

    /**
     * Пустая строка.
     */
    private static final String EMPTY = "—";

    /**
     * Конструктор.
     */
    private Strings() {

    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final String value) {
        return !isEmpty(value);
    }

    /**
     * Проверяет объект на пустату.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final String value) {
        return value == null || value.trim().isEmpty() || value.equals(EMPTY);
    }

    /**
     * Преобразование {@link String} в {@link String}.
     *
     * @param value Значение в формате {@link String}.
     * @return Значение в формате {@link String}.
     */
    public static String parse(final String value) {
        return nonEmpty(value) ? value : null;
    }

    /**
     * Преобразование {@link String} в {@link String}.
     *
     * @param value Значение в формате {@link String}.
     * @return Значение в формате {@link String}.
     */
    @NotNull
    public static String format(final String value) {
        return nonEmpty(value) ? value : EMPTY;
    }

    /**
     * Чтение из {@link Record} значения колонки в формате {@link String} и
     * преобразование его в {@link String}.
     *
     * @param record Запись.
     * @param field Колонка.
     * @return Значение в формате {@link String}.
     */
    public static String format(final Record record,
                                final Field<String> field) {
        return format(record.getValue(field, String.class));
    }
}
