package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.services.transactions.TransactionExecutor;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import com.github.onotoliy.opposite.treasure.utils.Objects;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class TransactionService
extends AbstractModifierService<
    Transaction,
    TransactionSearchParameter,
    TransactionRepository>
implements ITransactionService {

    /**
     * Репозиторий событий.
     */
    private final EventRepository event;

    /**
     * Сервис уведомлений.
     */
    private final NotificationService notification;

    /**
     * Сервисы описывающие бизнес логику тразакций.
     */
    private final Map<TransactionType, TransactionExecutor> executors;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий транзакций.
     * @param event Репозиторий событий.
     * @param notification Сервис уведомлений.
     * @param executors Список сервисов описывающие бизнес логику тразакций.
     */
    @Autowired
    public TransactionService(final TransactionRepository repository,
                              final EventRepository event,
                              final NotificationService notification,
                              final List<TransactionExecutor> executors) {
        super(repository);
        this.event = event;
        this.notification = notification;
        this.executors = executors
            .stream()
            .collect(Collectors.toMap(TransactionExecutor::type,
                                      Function.identity()));
    }

    @Override
    protected void create(final Configuration configuration,
                          final Transaction dto) {
        validation(dto);

        execute(dto, executor -> executor.create(configuration, dto));

        repository.create(configuration, dto);
        notification.notify(dto);
    }

    @Override
    protected void update(final Configuration configuration,
                          final Transaction dto) {
        Transaction previous = get(GUIDs.parse(dto));

        if (Objects.nonEqually(dto.getType(), previous.getType())) {
            throw new ModificationException("Нельзя менять тип транзакции");
        }

        if (Numbers.nonEqually(dto.getCash(), previous.getCash())) {
            throw new ModificationException("Нельзя менять сумму транзакции");
        }

        if (GUIDs.nonEqually(dto.getPerson(), previous.getPerson())) {
            throw new ModificationException("Нельзя менять члена клуба");
        }

        if (GUIDs.nonEqually(dto.getEvent(), previous.getEvent())) {
            throw new ModificationException("Нельзя менять мероприятие");
        }

        validation(dto);

        repository.update(configuration, dto);
        notification.notify(dto);
    }

    @Override
    protected void delete(final Configuration configuration, final UUID uuid) {
        execute(get(uuid),
                executor -> executor.delete(configuration, get(uuid)));

        repository.delete(configuration, uuid);
    }

    /**
     * Проверка полноты данных.
     *
     * @param dto Транзакция.
     */
    private void validation(final Transaction dto) {
        if (dto.getType() == TransactionType.NONE) {
            throw new ModificationException(
                "У транзакции должен быть указан тип");
        }

        if (dto.getType() != TransactionType.CONTRIBUTION) {
            return;
        }

        if (GUIDs.isEmpty(dto.getPerson())) {
            throw new ModificationException(
                "У взноса должен быть задан пользователь");
        }

        if (GUIDs.nonEmpty(dto.getEvent())) {
            Event e = event.get(GUIDs.parse(dto.getEvent()));
            if (Numbers.nonEqually(e.getContribution(), dto.getCash())) {
                throw new ModificationException(
                     "Внесенный взнос не равен взносу с человека");
            }
        }
    }

    /**
     * Исполнение бизнес логики транзакции.
     *
     * @param dto Транзакция.
     * @param consumer Бизнес логика.
     */
    private void execute(final Transaction dto,
                         final Consumer<TransactionExecutor> consumer) {
        TransactionExecutor executor = executors.get(dto.getType());

        if (executor == null) {
            throw new ModificationException(
                "Не удалось найти описание бизнес логики транзакции");
        }

        consumer.accept(executor);
    }
}
