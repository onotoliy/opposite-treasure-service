package com.github.onotoliy.opposite.treasure.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Утилитарный класс работы с исключениями.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Throwables {

    /**
     * Конструктор.
     */
    private Throwables() {

    }

    /**
     * Преобразование {@link Throwable} в {@link String}.
     *
     * @param value Значение в формате {@link Throwable}.
     * @return Значение в формате {@link String}.
     */
    public static String format(final Throwable value) {
        if (isEmpty(value)) {
            return null;
        }

        StringWriter writer = new StringWriter();

        value.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }

    /**
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final Throwable value) {
        return value == null;
    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final Throwable value) {
        return !isEmpty(value);
    }
}
