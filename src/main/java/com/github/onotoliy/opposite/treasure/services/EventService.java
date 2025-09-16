package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Meta;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.data.page.Paging;
import com.github.onotoliy.opposite.treasure.exceptions.ModificationException;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureEventRecord;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.repositories.TransactionRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jooq.Configuration;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;
import static com.github.onotoliy.opposite.treasure.utils.Numbers.isEmpty;

/**
 * Сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class EventService
extends AbstractModifierService<Event, TreasureEventRecord, EventSearchParameter, EventRepository>
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
    private final DepositService deposit;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий событий.
     * @param transaction Репозиторий транзакций.
     * @param debt Репозиторий долгов.
     * @param deposit Сервис чтения депозитов.
     */
    @Autowired
    public EventService(final EventRepository repository,
                        final TransactionRepository transaction,
                        final DebtRepository debt,
                        final DepositService deposit) {
        super(repository);
        this.transaction = transaction;
        this.debt = debt;
        this.deposit = deposit;
    }

    @Override
    protected void create(final Configuration configuration, final Event dto) {
        super.create(configuration, dto);

        if (isEmpty(dto.contribution())) {
            return;
        }

        deposit
            .getAll(DepositSearchParameter.ALL_ENABLE)
            .context()
            .forEach(e ->
                debt.cost(configuration, GUIDs.parse(e), GUIDs.parse(dto))
            );
    }

    @Override
    protected void update(final Configuration configuration, final Event dto) {
        Event previous = get(GUIDs.parse(dto));

        if (Numbers.nonEqually(dto.contribution(),
                               previous.contribution())) {
            throw new ModificationException(
                "Нельзя менять сумму взноса с одного человека");
        }

        super.update(configuration, dto);
    }

    @Override
    protected void delete(final Configuration configuration, final UUID uuid) {
        final int count = transaction.count(
            new TransactionSearchParameter(null, null, uuid, null, 0, 1));

        if (count > 0) {
            throw new ModificationException(
                String.format("К событию %s привязаны транзакции", uuid));
        }

        debt.contribution(configuration, uuid);

        super.delete(configuration, uuid);
    }

    @Override
    public Page<Deposit> getDebtors(
        final UUID event,
        final int offset,
        final int numberOfRows
    ) {
        var deposits = deposit
            .getAll(DepositSearchParameter.ALL)
            .context()
            .stream()
            .collect(Collectors.toMap(
                deposit -> deposit.uuid(),
                deposit -> deposit
            ));
        var debtors = debt
            .getDebtors(event, offset, numberOfRows)
            .stream().map(debtor -> deposits.get(debtor))
            .collect(Collectors.toList());
        var count = debt.countDebtors(event);

        return new Page<>(
            new Meta(count, new Paging(offset, numberOfRows)),
            debtors
        );
    }

    @Override
    protected Event toDTO(final Record record) {
        return toDTO(
            record,
            author -> Optional
                .ofNullable(author)
                .map(deposit::get)
                .map(athr -> new Option(athr.uuid(), athr.name()))
                .orElse(null)
        );
    }

    @Override
    protected TreasureEventRecord toRecord(final Event dto) {
        TreasureEventRecord record = new TreasureEventRecord();

        record.setGuid(dto.uuid() == null ? UUID.randomUUID() : dto.uuid());
        record.setName(dto.name());
        record.setContribution(dto.contribution());
        record.setDeadline(dto.deadline());
        record.setCreationDate(dto.creationDate());
        record.setAuthor(deposit.me().uuid());

        return record;
    }

    /**
     * Преобразование записи из БД {@link Record} в объект {@link E}.
     *
     * @param record Запись.
     * @param toAuthor Метод получения автора.
     * @return Объект.
     */
    public static Event toDTO(final Record record, final Function<UUID, Option> toAuthor) {
        return new Event(
            record.getValue(TREASURE_EVENT.GUID),
            record.getValue(TREASURE_EVENT.NAME),
            record.getValue(TREASURE_EVENT.CONTRIBUTION),
            record.getValue(TREASURE_EVENT.DEADLINE),
            record.getValue(TREASURE_EVENT.CREATION_DATE),
            toAuthor.apply(record.getValue(TREASURE_EVENT.AUTHOR)),
            record.getValue(TREASURE_EVENT.DELETION_DATE)
        );
    }
}
