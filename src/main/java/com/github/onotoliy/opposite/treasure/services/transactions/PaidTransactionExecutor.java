package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики транзакции "Платеж".
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class PaidTransactionExecutor
extends AbstractTransactionExecutor {

    /**
     * Репозиторий депозитов.
     */
    private final DepositRepository deposit;

    /**
     * Конструктор.
     *
     * @param deposit Репозиторий депозитов.
     */
    @Autowired
    public PaidTransactionExecutor(final DepositRepository deposit) {
        this.deposit = deposit;
    }

    @Override
    public void create(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        deposit.contribution(configuration,
                             GUIDs.parse(dto.getPerson()),
                             money);
    }

    @Override
    public void delete(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        deposit.cost(configuration, GUIDs.parse(dto.getPerson()), money);
    }

    @Override
    public TransactionType type() {
        return TransactionType.PAID;
    }
}
