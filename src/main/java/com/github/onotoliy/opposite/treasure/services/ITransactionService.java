package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;

/**
 * Сервис управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface ITransactionService
extends ModifierService<Transaction, TransactionSearchParameter> {

}
