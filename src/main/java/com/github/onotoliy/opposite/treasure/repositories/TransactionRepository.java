package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.data.TransactionSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.Tables;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureTransactionRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
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
    TransactionSearchParameter,
    TreasureTransactionRecord,
    TreasureTransaction> {

    /**
     * Конструктор.
     *
     * @param dsl  Контекст подключения к БД.
     */
    @Autowired
    public TransactionRepository(
        final DSLContext dsl
    ) {
        super(
            TREASURE_TRANSACTION,
            TREASURE_TRANSACTION.GUID,
            TREASURE_TRANSACTION.CREATION_DATE,
            TREASURE_TRANSACTION.DELETION_DATE,
            dsl
        );
    }

    @Override
    protected SelectJoinStep<Record> findQuery() {
        return super.findQuery()
                    .leftJoin(Tables.TREASURE_EVENT)
                    .on(Tables.TREASURE_EVENT.GUID.eq(table.EVENT_GUID));
    }

    @Override
    protected List<Condition> where(
        final TransactionSearchParameter parameter
    ) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasEvent()) {
            conditions.add(
                TREASURE_TRANSACTION.EVENT_GUID.eq(parameter.event()));
        }

        if (parameter.hasUser()) {
            conditions.add(
                TREASURE_TRANSACTION.USER_GUID.eq(parameter.user()));
        }

        if (parameter.hasName()) {
            conditions.add(
                TREASURE_TRANSACTION.NAME.likeIgnoreCase(
                    "%" + parameter.name() + "%"));
        }

        if (parameter.hasType()) {
            conditions.add(
                TREASURE_TRANSACTION.TYPE.eq(parameter.type().name()));
        }

        return conditions;
    }

    @Override
    protected List<? extends OrderField<?>> orderBy() {
        return new LinkedList<>(
            Collections.singleton(table.TRANSACTION_DATE.desc())
        );
    }

}
