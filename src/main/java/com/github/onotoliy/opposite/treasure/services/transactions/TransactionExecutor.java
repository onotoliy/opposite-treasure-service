package com.github.onotoliy.opposite.treasure.services.transactions;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.bpp.log.Log;

import org.jooq.Configuration;

/**
 * Описание бизнес логики транзакции.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface TransactionExecutor {

    /**
     * Описание бизнес логики создания транзакции.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     */
    @Log(db = true)
    void create(Configuration configuration, Transaction dto);

    /**
     * Описание бизнес логики удаления транзакции.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     */
    @Log(db = true)
    void delete(Configuration configuration, Transaction dto);

    /**
     * Возвращает тип транзакции.
     *
     * @return тип транзакции.
     */
    TransactionType type();

}
