package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.Tables;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureTransactionRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_TRANSACTION;

/**
 * Репозиторий управления транзакциями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class TransactionRepository
extends AbstractModifierRepository<
    Transaction,
    TransactionSearchParameter,
    TreasureTransactionRecord,
    TreasureTransaction> {

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    @Autowired
    public TransactionRepository(final DSLContext dsl, final UserRPC user) {
        super(
            TREASURE_TRANSACTION,
            TREASURE_TRANSACTION.GUID,
            TREASURE_TRANSACTION.NAME,
            TREASURE_TRANSACTION.AUTHOR,
            TREASURE_TRANSACTION.CREATION_DATE,
            TREASURE_TRANSACTION.DELETION_DATE,
            dsl,
            user);
    }

    @Override
    protected SelectJoinStep<Record> findQuery() {
        return super.findQuery()
                    .leftJoin(Tables.TREASURE_EVENT)
                    .on(Tables.TREASURE_EVENT.GUID.eq(TABLE.EVENT_GUID));
    }

    @Override
    protected List<Condition> where(final TransactionSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasEvent()) {
            conditions.add(
                TREASURE_TRANSACTION.EVENT_GUID.eq(parameter.getEvent()));
        }

        if (parameter.hasUser()) {
            conditions.add(
                TREASURE_TRANSACTION.USER_GUID.eq(parameter.getUser()));
        }

        if (parameter.hasName()) {
            conditions.add(
                TREASURE_TRANSACTION.NAME.likeIgnoreCase("%" + parameter.getName() + "%"));
        }

        if (parameter.hasType()) {
            conditions.add(
                TREASURE_TRANSACTION.TYPE.eq(parameter.getType().name()));
        }

        return conditions;
    }

    @Override
    public InsertSetMoreStep<TreasureTransactionRecord> insertQuery(final Configuration configuration,
                                                                    final Transaction dto) {
        return super.insertQuery(configuration, dto)
                    .set(TABLE.CASH, Numbers.parse(dto.getCash()))
                    .set(TABLE.USER_GUID, GUIDs.parse(dto.getPerson()))
                    .set(TABLE.EVENT_GUID, GUIDs.parse(dto.getEvent()))
                    .set(TABLE.TYPE, Strings.parse(dto.getType().name()));
    }

    @Override
    public UpdateSetMoreStep<TreasureTransactionRecord> updateQuery(final Configuration configuration,
                                                                    final Transaction dto) {
        return super.updateQuery(configuration, dto)
                    .set(TABLE.CASH, Numbers.parse(dto.getCash()))
                    .set(TABLE.USER_GUID, GUIDs.parse(dto.getPerson()))
                    .set(TABLE.EVENT_GUID, GUIDs.parse(dto.getEvent()))
                    .set(TABLE.TYPE, Strings.parse(dto.getType().name()));
    }

    @Override
    protected Transaction toDTO(final Record record) {
        return new Transaction(
            GUIDs.format(record, UUID),
            Strings.format(record, NAME),
            Numbers.format(record, TABLE.CASH),
            TransactionType.valueOf(Strings.format(record, TABLE.TYPE)),
            formatUser(record, TABLE.USER_GUID),
            EventRepository.toOption(record),
            Dates.format(record, CREATION_DATE),
            formatUser(record, AUTHOR)
        );
    }
}
