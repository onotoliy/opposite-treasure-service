package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики транзакции "Заработано".
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class EarnedTransactionExecutor
extends AbstractTransactionExecutor {

    /**
     * Репозиторий данных о кассе.
     */
    private final CashboxRepository cashbox;

    /**
     * Конструктор.
     *
     * @param cashbox Репозиторий данных о кассе.
     */
    @Autowired
    public EarnedTransactionExecutor(final CashboxRepository cashbox) {
        this.cashbox = cashbox;
    }

    @Override
    public void create(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        this.cashbox.contribution(configuration, money);
    }

    @Override
    public void delete(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        BigDecimal value = cashbox.money();

        if (value == null || value.compareTo(money) < 0) {
            throw new ModificationException(
                "В кассе не может быть отрицательная сумма");
        }

        cashbox.cost(configuration, money);
    }

    @Override
    public TransactionType type() {
        return TransactionType.EARNED;
    }
}
