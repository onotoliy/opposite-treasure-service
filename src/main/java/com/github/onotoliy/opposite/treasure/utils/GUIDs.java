package com.github.onotoliy.opposite.treasure.utils;


import com.github.onotoliy.opposite.treasure.data.core.HasUUID;

import java.util.UUID;

/**
 * Утилитарный класс работы с уникальными идентификаторами.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class GUIDs {

    /**
     * Конструктор.
     */
    private GUIDs() {

    }

    /**
     * Преобразование {@link UUID} в {@link String}.
     *
     * @param value Значение в формате {@link UUID}.
     * @return Значение в формате {@link String}.
     */
    public static String format(final UUID value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Преобразование {@link HasUUID} в {@link UUID}.
     *
     * @param value Значение в формате {@link HasUUID}.
     * @return Значение в формате {@link UUID}.
     */
    public static UUID parse(final HasUUID value) {
        return value == null ? null : value.uuid();
    }

    /**
     * Преобразование {@link String} в {@link UUID}.
     *
     * @param value Значение в формате {@link String}.
     * @return Значение в формате {@link UUID}.
     */
    public static UUID parse(final String value) {
        return UUID.fromString(value);
    }

    /**
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final UUID value) {
        return value == null;
    }

    /**
     * Проверяет объект на пустоту.
     *
     * @param value Объект
     * @return Результат проверки.
     */
    public static boolean isEmpty(final HasUUID value) {
        return value == null || GUIDs.isEmpty(value.uuid());
    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final UUID value) {
        return !isEmpty(value);
    }

    /**
     * Проверка содержит ли объект какое-либо значение.
     *
     * @param value Объект.
     * @return Результат проверки.
     */
    public static boolean nonEmpty(final HasUUID value) {
        return !isEmpty(value);
    }

    /**
     * Проверка двух объектов на неравенство.
     *
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static boolean nonEqually(final HasUUID x, final HasUUID y) {
        return !isEqually(x, y);
    }

    /**
     * Проверка двух объектов на равенство.
     *
     * @param x X
     * @param y Y
     * @return Результат проверки.
     */
    public static boolean isEqually(final HasUUID x, final HasUUID y) {
        return Objects.isEqually(parse(x), parse(y), UUID::compareTo);
    }
}
