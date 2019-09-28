package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики транзакции "Взнос".
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class ContributionTransactionExecutor
extends AbstractTransactionExecutor {

    /**
     * Репозиторий данных о кассе.
     */
    private final CashboxRepository cashbox;

    /**
     * Репозиторий долгов.
     */
    private final DebtRepository debt;

    /**
     * Конструктор.
     *
     * @param cashbox Репозиторий данных о кассе.
     * @param debt Репозиторий долгов.
     */
    @Autowired
    public ContributionTransactionExecutor(final CashboxRepository cashbox,
                                           final DebtRepository debt) {
        this.cashbox = cashbox;
        this.debt = debt;
    }

    @Override
    public void create(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        cashbox.contribution(configuration, money);
        debt.contribution(configuration,
                          GUIDs.parse(dto.getPerson()),
                          GUIDs.parse(dto.getEvent()));
    }

    @Override
    public void delete(final Configuration configuration,
                       final Transaction dto,
                       final BigDecimal money) {
        cashbox.cost(configuration, money);
        debt.cost(configuration,
                  GUIDs.parse(dto.getPerson()),
                  GUIDs.parse(dto.getEvent()));
    }

    @Override
    public TransactionType type() {
        return TransactionType.CONTRIBUTION;
    }
}
