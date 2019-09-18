package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.treasure.dto.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.Tables;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureTransactionRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_TRANSACTION;
import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;
import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.TimestampUtil.TIMESTAMP;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

@Repository
public class TransactionRepository
extends AbstractModifierRepository<
    Transaction,
    TransactionSearchParameter,
    TreasureTransactionRecord,
    TreasureTransaction> {

    @Autowired
    public TransactionRepository(DSLContext dsl, UserRPC user) {
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
    protected List<Condition> where(TransactionSearchParameter parameter) {
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
    public InsertSetMoreStep<TreasureTransactionRecord> insertQuery(Configuration configuration,
                                                                    Transaction dto) {
        return super.insertQuery(configuration, dto)
                    .set(TABLE.CASH, MONEY.parse(dto.getCash()))
                    .set(TABLE.USER_GUID, GUID.parse(dto.getPerson()))
                    .set(TABLE.EVENT_GUID, GUID.parse(dto.getEvent()))
                    .set(TABLE.TYPE, STRING.parse(dto.getType().name()));
    }

    @Override
    public UpdateSetMoreStep<TreasureTransactionRecord> updateQuery(Configuration configuration,
                                                                    Transaction dto) {
        return super.updateQuery(configuration, dto)
                    .set(TABLE.CASH, MONEY.parse(dto.getCash()))
                    .set(TABLE.USER_GUID, GUID.parse(dto.getPerson()))
                    .set(TABLE.EVENT_GUID, GUID.parse(dto.getEvent()))
                    .set(TABLE.TYPE, STRING.parse(dto.getType().name()));
    }

    @Override
    protected Transaction toDTO(Record record) {
        return new Transaction(
            GUID.format(record, UUID),
            STRING.format(record, NAME),
            MONEY.format(record, TABLE.CASH),
            TransactionType.valueOf(STRING.format(record, TABLE.TYPE)),
            formatUser(record, TABLE.USER_GUID),
            EventRepository.toOption(record),
            TIMESTAMP.format(record, CREATION_DATE),
            formatUser(record, AUTHOR)
        );
    }
}
