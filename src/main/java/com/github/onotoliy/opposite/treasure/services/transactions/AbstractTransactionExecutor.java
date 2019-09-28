package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.math.BigDecimal;

import org.jooq.Configuration;

/**
 * Описание бизнес логики транзакции.
 *
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractTransactionExecutor
implements TransactionExecutor {

    /**
     * Описание бизнес логики создания транзакции.
     *
     * @param configuration Настройки транзакции.
     * @param dto Транзакция.
     * @param money Сумма транзакции.
     */
    protected abstract void create(Configuration configuration,
                                   Transaction dto,
                                   BigDecimal money);

    /**
     * Описание бизнес логики удаления транзакции.
     *
     * @param configuration Настройки транзакции.
     * @param dto Транзакция.
     * @param money Сумма транзакции.
     */
    protected abstract void delete(Configuration configuration,
                                   Transaction dto,
                                   BigDecimal money);

    @Override
    public final void create(final Configuration configuration,
                             final Transaction dto) {
        create(configuration, dto, money(dto));
    }

    @Override
    public final void delete(final Configuration configuration,
                             final Transaction dto) {
        delete(configuration, dto, money(dto));
    }

    /**
     * Получение суммы транзакции и ее проверка.
     *
     * @param dto Транзакция
     * @return Сумма транзакции.
     */
    private BigDecimal money(final Transaction dto) {
        BigDecimal money = Numbers.parse(dto.getCash());

        if (money == null) {
            throw new ModificationException("Денежные средсва не заполнены");
        }

        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ModificationException("Денежные средсва не заполнены");
        }

        return money;
    }
}
