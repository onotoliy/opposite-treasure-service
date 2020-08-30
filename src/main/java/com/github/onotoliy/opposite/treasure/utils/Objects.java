package com.github.onotoliy.opposite.treasure.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Утилитарный класс работы с объектами.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Objects {

    /**
     * Конструктор.
     */
    private Objects() {

    }

    /**
     * Проверка двух объектов на неравенство.
     *
     * @param <T> Тип объекта
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static <T> boolean nonEqually(final T x, final T y) {
        return !isEqually(x, y);
    }

    /**
     * Проверка двух объектов на равенство.
     *
     * @param <T> Тип объекта
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static <T> boolean isEqually(final T x, final T y) {
        return isEqually(x, y, (o1, o2) -> o1.equals(o2) ? 0 : -1);
    }

    /**
     * Проверка двух объектов на неравенство.
     *
     * @param <T> Тип объекта
     * @param x X
     * @param y Y
     * @param equal Описание метода сравнения.
     * @return Результат проверки.
     */
    public static <T> boolean nonEqually(final T x,
                                         final T y,
                                         final Comparator<T> equal) {
        return !isEqually(x, y, equal);
    }

    /**
     * Проверка двух объектов на равенство.
     *
     * @param <T> Тип объекта
     * @param x X
     * @param y Y
     * @param equal Описание метода сравнения.
     * @return Результат проверки.
     */
    public static <T> boolean isEqually(final T x,
                                        final T y,
                                        final Comparator<T> equal) {
        if (x == null && y == null) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return equal.compare(x, y) == 0;
    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final Object value) {
        return !isEmpty(value);
    }

    /**
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final Object value) {
        return value == null;
    }

    /**
     * Проверяет список объектов на пустоту.
     *
     * @param value Список объектов.
     * @param <T> Тип объекта.
     * @return Результат проверки.
     */
    public static <T> boolean isEmpty(final List<T> value) {
        return value == null || value.isEmpty();
    }

    /**
     * Проверка содержит ли список объектов какое-либо значение.
     *
     * @param value Список объектов.
     * @param <T> Тип объекта.
     * @return Результат проверки.
     */
    public static <T> boolean nonEmpty(final List<T> value) {
        return !isEmpty(value);
    }

    /**
     * Проверяет список объектов на пустоту.
     *
     * @param value Список объектов.
     * @param <T> Тип объекта.
     * @return Результат проверки.
     */
    public static <T> boolean isEmpty(final Set<T> value) {
        return value == null || value.isEmpty();
    }

    /**
     * Проверка содержит ли список объектов какое-либо значение.
     *
     * @param value Список объектов.
     * @param <T> Тип объекта.
     * @return Результат проверки.
     */
    public static <T> boolean nonEmpty(final Set<T> value) {
        return !isEmpty(value);
    }
}
