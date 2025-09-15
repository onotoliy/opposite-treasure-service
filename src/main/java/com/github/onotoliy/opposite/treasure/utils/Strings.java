package com.github.onotoliy.opposite.treasure.utils;

/**
 * Утилитарный класс работы со строками.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Strings {

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
        return value == null || value.trim().isEmpty();
    }

    /**
     * Сравнение двух строка.
     *
     * @param v1 Строка.
     * @param v2 Строка
     * @param isIgnoreCase Не учитывать регистр.
     * @return Резщультат сравнения.
     */
    public static boolean equals(
        final String v1,
        final String v2,
        final boolean isIgnoreCase
    ) {
        if (isEmpty(v1) && isEmpty(v2)) {
            return true;
        }

        if (isEmpty(v1) || isEmpty(v2)) {
            return false;
        }

        return isIgnoreCase ? v1.equalsIgnoreCase(v2) : v1.equals(v2);
    }

}
