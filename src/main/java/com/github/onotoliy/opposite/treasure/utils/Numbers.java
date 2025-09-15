package com.github.onotoliy.opposite.treasure.utils;

import java.math.BigDecimal;


/**
 * Утилитарный класс работы с числами и деньгами.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class Numbers {

    /**
     * Конструктор.
     */
    private Numbers() {

    }

    /**
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final BigDecimal value) {
        return value == null || BigDecimal.ZERO.equals(value);
    }

    /**
     * Проверка двух объектов на неравенство.
     *
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static boolean nonEqually(final BigDecimal x, final BigDecimal y) {
        return Objects.nonEqually(x, y, BigDecimal::compareTo);
    }
}
