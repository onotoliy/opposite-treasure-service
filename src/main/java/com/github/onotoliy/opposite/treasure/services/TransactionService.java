package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.Transaction;
import com.github.onotoliy.opposite.treasure.data.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.data.TransactionType;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureTransactionRecord;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.services.transactions.TransactionExecutor;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jooq.Configuration;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_TRANSACTION;

/**
 * Сервис управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class TransactionService
extends AbstractModifierService<
    Transaction,
    TreasureTransactionRecord,
    TransactionSearchParameter,
    TransactionRepository>
implements ITransactionService {

    /**
     * Репозиторий событий.
     */
    private final EventService event;

    /**
     * Сервис чтения пользователей.
     */
    private final DepositService deposit;

    /**
     * Сервисы описывающие бизнес логику тразакций.
     */
    private final Map<TransactionType, TransactionExecutor> executors;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий транзакций.
     * @param event Репозиторий событий.
     * @param deposit Сервис чтения депозитов.
     * @param executors Список сервисов описывающие бизнес логику тразакций.
     */
    @Autowired
    public TransactionService(final TransactionRepository repository,
                              final EventService event,
                              final DepositService deposit,
                              final List<TransactionExecutor> executors) {
        super(repository);

        this.deposit = deposit;
        this.event = event;
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

        super.create(configuration, dto);
    }

    @Override
    protected void update(final Configuration configuration,
                          final Transaction dto) {
        Transaction previous = get(GUIDs.parse(dto));

        if (Objects.nonEqually(dto.type(), previous.type())) {
            throw new ModificationException("Нельзя менять тип транзакции");
        }

        if (Numbers.nonEqually(dto.cash(), previous.cash())) {
            throw new ModificationException("Нельзя менять сумму транзакции");
        }

        if (GUIDs.nonEqually(dto.person(), previous.person())) {
            throw new ModificationException("Нельзя менять члена клуба");
        }

        if (GUIDs.nonEqually(dto.event(), previous.event())) {
            throw new ModificationException("Нельзя менять мероприятие");
        }

        validation(dto);

        super.update(configuration, dto);
    }

    @Override
    protected void delete(final Configuration configuration, final UUID uuid) {
        execute(get(uuid),
                executor -> executor.delete(configuration, get(uuid)));

        super.delete(configuration, uuid);
    }

    /**
     * Проверка полноты данных.
     *
     * @param dto Транзакция.
     */
    private void validation(final Transaction dto) {
        if (dto.type() == TransactionType.NONE) {
            throw new ModificationException(
                "У транзакции должен быть указан тип");
        }

        if (dto.type() != TransactionType.CONTRIBUTION) {
            return;
        }

        if (GUIDs.isEmpty(dto.person())) {
            throw new ModificationException(
                "У взноса должен быть задан пользователь");
        }

        if (GUIDs.nonEmpty(dto.event())) {
            Event e = event.get(GUIDs.parse(dto.event()));
            if (Numbers.nonEqually(e.contribution(), dto.cash())) {
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
        TransactionExecutor executor = executors.get(dto.type());

        if (executor == null) {
            throw new ModificationException(
                "Не удалось найти описание бизнес логики транзакции");
        }

        consumer.accept(executor);
    }

    @Override
    protected Transaction toDTO(final Record record) {
        return new Transaction(
            record.getValue(TREASURE_TRANSACTION.GUID),
            record.getValue(TREASURE_TRANSACTION.NAME),
            record.getValue(TREASURE_TRANSACTION.CASH),
            TransactionType.valueOf(
                record.getValue(TREASURE_TRANSACTION.TYPE)
            ),
            Optional
                .ofNullable(record.getValue(TREASURE_TRANSACTION.USER_GUID))
                .map(deposit::get)
                .map(author -> new Option(author.uuid(), author.name()))
                .orElse(null),
            Optional
                .ofNullable(record.getValue(TREASURE_TRANSACTION.EVENT_GUID))
                .map(event ->
                    new Option(event, record.getValue(TREASURE_EVENT.NAME))
                )
                .orElse(null),
            record.getValue(TREASURE_TRANSACTION.TRANSACTION_DATE),
            record.getValue(TREASURE_TRANSACTION.CREATION_DATE),
            Optional
                .ofNullable(record.getValue(TREASURE_TRANSACTION.AUTHOR))
                .map(deposit::get)
                .map(author -> new Option(author.uuid(), author.name()))
                .orElse(null),
            record.getValue(TREASURE_TRANSACTION.DELETION_DATE)
        );
    }
    @Override
    protected TreasureTransactionRecord toRecord(final Transaction dto) {
        TreasureTransactionRecord record = new TreasureTransactionRecord();

        record.setGuid(dto.uuid() == null ? UUID.randomUUID() : dto.uuid());
        record.setEventGuid(
            GUIDs.isEmpty(dto.event()) ? null : GUIDs.parse(dto.event())
        );
        record.setUserGuid(
            GUIDs.isEmpty(dto.person()) ? null : GUIDs.parse(dto.person())
        );
        record.setName(dto.name());
        record.setCash(dto.cash());
        record.setType(dto.type().name());
        record.setTransactionDate(dto.transactionDate());
        record.setCreationDate(dto.creationDate());
        record.setAuthor(deposit.me().uuid());

        return record;
    }

}
