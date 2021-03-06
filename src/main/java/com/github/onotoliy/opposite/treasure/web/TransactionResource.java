package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.services.ITransactionService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    /**
     * Поиск транзакций.
     *
     * @param name Название.
     * @param user Пользователь.
     * @param event Событие.
     * @param type Тип.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Транзакции.
     */
    @GetMapping
    public Page<Transaction> getAll(
            @RequestParam(value = "name", required = false)
            final String name,
            @RequestParam(value = "user", required = false)
            final UUID user,
            @RequestParam(value = "event", required = false)
            final UUID event,
            @RequestParam(value = "type", required = false)
            final TransactionType type,
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            final int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            final int numberOfRows) {
        return service.getAll(new TransactionSearchParameter(name,
                                                             user,
                                                             event,
                                                             type,
                                                             offset,
                                                             numberOfRows));
    }
}
