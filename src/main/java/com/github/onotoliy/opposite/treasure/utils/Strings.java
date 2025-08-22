package com.github.onotoliy.opposite.treasure.utils;

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

}
