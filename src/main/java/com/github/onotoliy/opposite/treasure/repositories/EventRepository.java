package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.Paging;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureEventRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;
import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;
import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.TimestampUtil.TIMESTAMP;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

@Repository
public class EventRepository
extends AbstractModifierRepository<
    Event,
    EventSearchParameter,
    TreasureEventRecord,
    TreasureEvent> {

    @Autowired
    public EventRepository(DSLContext dsl, UserRPC user) {
        super(
            TREASURE_EVENT,
            TREASURE_EVENT.GUID,
            TREASURE_EVENT.NAME,
            TREASURE_EVENT.AUTHOR,
            TREASURE_EVENT.CREATION_DATE,
            TREASURE_EVENT.DELETION_DATE,
            dsl,
            user);
    }

    public Page<Event> getDebts(java.util.UUID person) {
        List<Event> list = dsl.select()
                              .from(TREASURE_DEBT)
                              .join(TREASURE_EVENT)
                              .on(TREASURE_EVENT.GUID.eq(TREASURE_DEBT.EVENT_GUID))
                              .where(TREASURE_DEBT.USER_UUID.eq(person))
                              .fetch(this::toDTO);

        return new Page<>(new Meta(list.size(), new Paging(0, list.size())), list);
    }

    @Override
    public List<Condition> where(EventSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasName()) {
            conditions.add(TREASURE_EVENT.NAME.eq(parameter.getName()));
        }

        return conditions;
    }

    @Override
    public InsertSetMoreStep<TreasureEventRecord> insertQuery(Configuration configuration,
                                                              Event dto) {
        return super.insertQuery(configuration, dto)
                    .set(TABLE.CONTRIBUTION, MONEY.parse(dto.getContribution()))
                    .set(TABLE.TOTAL, MONEY.parse(dto.getTotal()))
                    .set(TABLE.DEADLINE, TIMESTAMP.parse(dto.getDeadline()));
    }

    @Override
    public UpdateSetMoreStep<TreasureEventRecord> updateQuery(Configuration configuration,
                                                              Event dto) {
        return super.updateQuery(configuration, dto)
                    .set(TABLE.CONTRIBUTION, MONEY.parse(dto.getContribution()))
                    .set(TABLE.TOTAL, MONEY.parse(dto.getTotal()))
                    .set(TABLE.DEADLINE, TIMESTAMP.parse(dto.getDeadline()));
    }

    @Override
    protected Event toDTO(Record record) {
        return new Event(
            GUID.format(record, UUID),
            STRING.format(record, NAME),
            MONEY.format(record, TABLE.CONTRIBUTION),
            MONEY.format(record, TABLE.TOTAL),
            TIMESTAMP.format(record, TABLE.DEADLINE),
            TIMESTAMP.format(record, CREATION_DATE),
            formatUser(record, AUTHOR));
    }

    public static Option toOption(Record record) {
        if (record.getValue(TREASURE_EVENT.GUID, java.util.UUID.class) == null) {
            return null;
        }

        return new Option(
            GUID.format(record, TREASURE_EVENT.GUID),
            STRING.format(record, TREASURE_EVENT.NAME));
    }
}
