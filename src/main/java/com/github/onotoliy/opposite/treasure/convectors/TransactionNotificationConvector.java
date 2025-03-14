package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.treasure.data.Transaction;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Objects;

/**
 * Класс описывающий логику преобразования транзакции в текстовое уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public class TransactionNotificationConvector
extends AbstractNotificationConvector<Transaction> {

    /**
     * Конструктор.
     *
     * @param html Использовать HTML верстку.
     */
    public TransactionNotificationConvector(final boolean html) {
        super(html);
    }

    @Override
    protected void append(final Transaction dto) {
        append("Тип", dto.type().getLabel());
        append("Название", dto.name());
        append("Сумма", dto.cash());
        append("Дата транзации", Dates.toShortFormat(dto.transactionDate()));

        if (Objects.nonEmpty(dto.event())) {
            append("Событие", dto.event().name());
        }

        if (Objects.nonEmpty(dto.person())) {
            append("Член клуба", dto.person().name());
        }
    }

}
