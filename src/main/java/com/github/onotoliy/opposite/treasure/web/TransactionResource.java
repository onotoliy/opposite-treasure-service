package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.treasure.data.Transaction;
import com.github.onotoliy.opposite.treasure.data.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.services.ITransactionService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/transaction")
public class TransactionResource
    extends AbstractModifierResource<
        Transaction,
        TransactionSearchParameter,
        ITransactionService> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public TransactionResource(final ITransactionService service) {
        super(service);
    }

}
