package com.github.onotoliy.opposite.treasure.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

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
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final BigDecimal value) {
        return !isEmpty(value);
    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final String value) {
        return Strings.nonEmpty(value) && nonEmpty(parse(value));
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
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final String value) {
        return Strings.isEmpty(value) || isEmpty(parse(value));
    }

    /**
     * Преобразование {@link String} в {@link BigDecimal}.
     *
     * @param value Значение в формате {@link String}.
     * @return Значение в формате {@link BigDecimal}.
     */
    public static BigDecimal parse(final String value) {
        return Strings.isEmpty(value) ? null : new BigDecimal(value);
    }

    /**
     * Преобразование {@link BigDecimal} в {@link String}.
     *
     * @param value Значение в формате {@link BigDecimal}.
     * @return Значение в формате {@link String}.
     */
    @NotNull
    public static String format(final BigDecimal value) {
        if (isEmpty(value)) {
            return "0.0";
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return df.format(value.setScale(2, RoundingMode.HALF_DOWN));
    }

    /**
     * Чтение из {@link Record} значения колонки в формате {@link BigDecimal}
     * и преобразование его в {@link String}.
     *
     * @param record Запись.
     * @param field Колонка.
     * @return Значение в формате {@link java.lang.String}.
     */
    public static String format(final Record record,
                                final Field<BigDecimal> field) {
        return format(record.getValue(field, BigDecimal.class));
    }

    /**
     * Проверка двух объектов на неравенство.
     *
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static boolean nonEqually(final String x, final String y) {
        return nonEqually(parse(x), parse(y));
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
