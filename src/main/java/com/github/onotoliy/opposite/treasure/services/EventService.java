package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.Transaction;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.data.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.data.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.util.UUID;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.onotoliy.opposite.treasure.utils.Numbers.isEmpty;

/**
 * Сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class EventService
extends AbstractModifierService<Event, EventSearchParameter, EventRepository>
implements IEventService {

    /**
     * Репозиторий транзакций.
     */
    private final TransactionRepository transaction;

    /**
     * Репозиторий долгов.
     */
    private final DebtRepository debt;

    /**
     * Сервис чтения пользователей.
     */
    private final KeycloakRPC user;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий событий.
     * @param transaction Репозиторий транзакций.
     * @param debt Репозиторий долгов.
     * @param user Сервис пользователей.
     */
    @Autowired
    public EventService(final EventRepository repository,
                        final TransactionRepository transaction,
                        final DebtRepository debt,
                        final KeycloakRPC user) {
        super(repository);
        this.transaction = transaction;
        this.debt = debt;
        this.user = user;
    }

    @Override
    protected void create(final Configuration configuration, final Event dto) {
        repository.create(configuration, dto);

        if (isEmpty(dto.contribution()) && isEmpty(dto.total())) {
            return;
        }

        user.getAll().forEach(e ->
            debt.cost(configuration, GUIDs.parse(e), GUIDs.parse(dto)));
    }

    @Override
    protected void update(final Configuration configuration, final Event dto) {
        Event previous = get(GUIDs.parse(dto));

        if (Numbers.nonEqually(dto.contribution(),
                               previous.contribution())) {
            throw new ModificationException(
                "Нельзя менять сумму взноса с одного человека");
        }

        if (Numbers.nonEqually(dto.total(), previous.total())) {
            throw new ModificationException("Нельзя менять общую сумму");
        }

        repository.update(configuration, dto);
    }

    @Override
    protected void delete(final Configuration configuration, final UUID uuid) {
        Page<Transaction> page = transaction.getAll(
            new TransactionSearchParameter(null, null, uuid, null, 0, 1));

        if (page.meta().total() > 0) {
            throw new ModificationException(
                String.format("К событию %s привязаны транзакции", uuid));
        }

        debt.contribution(configuration, uuid);

        repository.delete(configuration, uuid);
    }
}
