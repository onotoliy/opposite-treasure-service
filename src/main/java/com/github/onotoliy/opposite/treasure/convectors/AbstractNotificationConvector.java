package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.data.Cashbox;

/**
 * Базовый класс описывающий логику преобразования объекта в текстовое
 * уведомление.
 *
 * @param <T> Тип объекта.
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractNotificationConvector<T> {

    /**
     * Использовать HTML верстку.
     */
    private final boolean html;

    /**
     * Текстовое сообщение.
     */
    private final StringBuilder message;

    /**
     * Конструктор.
     *
     * @param html Использовать HTML верстку.
     */
    public AbstractNotificationConvector(final boolean html) {
        this.html = html;
        this.message = new StringBuilder();
    }

    /**
     * Преобразование объекта в текстовое уведомление.
     *
     * @param dto Объект.
     * @param cashbox Данные о кассе.
     * @return Текстовое уведомление.
     */
    public String toNotification(final T dto, final Cashbox cashbox) {
        message.setLength(0);

        append(dto);

        append("В кассе", cashbox.getDeposit());

        return message.toString();
    }

    /**
     * Преобразование объекта в текстовое уведомление.
     *
     * @param dto Объект.
     */
    protected abstract void append(T dto);

    /**
     * Добавление строки в текстовое уведомление.
     *
     * @param title Заголовок.
     * @param value Значение.
     */
    protected void append(final String title, final String value) {
        if (html) {
            message.append("<b>");
        }

        message.append(title);

        if (html) {
            message.append("</b>");
        }

        message.append(": ").append(value).append(".");

        if (html) {
            message.append("<br/>");
        } else {
            message.append("\n");
        }
    }
}
