package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

import java.util.Random;

/**
 * Утилитарный класс работы со строками.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Strings {

    /**
     * Индекс символа 'a'.
     */
    private static final int A_INDEX = 97;

    /**
     * Индекс символа 'z'.
     */
    private static final int Z_INDEX = 122;

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
     * Генерация случайной строки указанной длины.
     *
     * @param length Длина строки.
     * @return Сдучайная строка.
     */
    public static String random(final int length) {
        return new Random()
            .ints(A_INDEX, Z_INDEX + 1)
            .limit(length)
            .collect(StringBuilder::new,
                     StringBuilder::appendCodePoint,
                     StringBuilder::append)
            .toString();
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
