package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.data.Transaction;
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
        append("Тип", dto.getType().getLabel());
        append("Название", dto.getName());
        append("Сумма", dto.getCash());
        append("Дата транзации", Dates.toShortFormat(dto.getTransactionDate()));

        if (Objects.nonEmpty(dto.getEvent())) {
            append("Событие", dto.getEvent().getName());
        }

        if (Objects.nonEmpty(dto.getPerson())) {
            append("Член клуба", dto.getPerson().getName());
        }
    }

}
