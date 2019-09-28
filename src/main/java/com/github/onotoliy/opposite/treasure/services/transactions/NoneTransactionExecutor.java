package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики транзакции "Не выбрано".
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NoneTransactionExecutor extends AbstractTransactionExecutor {

    /**
     * Конструктор.
     */
    public NoneTransactionExecutor() {

    }

    @Override
    public void create(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        throw new ModificationException("Тип транзакции указан не корректно");
    }

    @Override
    public void delete(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        throw new ModificationException("Тип транзакции указан не корректно");
    }

    @Override
    public TransactionType type() {
        return TransactionType.NONE;
    }
}
