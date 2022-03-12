package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.data.Cashbox;

/**
 * Класс описывающий логику преобразования кассы в текстовое уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public class CashboxNotificationConvector
extends AbstractNotificationConvector<Cashbox> {

    /**
     * Конструктор.
     *
     * @param html Использовать HTML верстку.
     */
    public CashboxNotificationConvector(final boolean html) {
        super(html);
    }

    @Override
    protected void append(final Cashbox dto) {

    }

}
