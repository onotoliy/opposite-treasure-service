package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.data.page.Paging;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureEventRecord;
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
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;

/**
 * Репозиторий управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class EventRepository
extends AbstractModifierRepository<
    Event,
    EventSearchParameter,
    TreasureEventRecord,
    TreasureEvent> {

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    @Autowired
    public EventRepository(final DSLContext dsl, final UserRPC user) {
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

    /**
     * Получение событий по которым пользователь должен.
     *
     * @param person Пользователь.
     * @return События.
     */
    public Page<Event> getDebts(final java.util.UUID person) {
        List<Event> list = dsl.select()
                              .from(TREASURE_DEBT)
                              .join(TREASURE_EVENT)
                              .on(TREASURE_EVENT.GUID.eq(TREASURE_DEBT.EVENT_GUID))
                              .where(TREASURE_DEBT.USER_UUID.eq(person))
                              .fetch(this::toDTO);

        return new Page<>(new Meta(list.size(), new Paging(0, list.size())), list);
    }

    @Override
    public List<Condition> where(final EventSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasName()) {
            conditions.add(TREASURE_EVENT.NAME.likeIgnoreCase("%" + parameter.getName() + "%"));
        }

        return conditions;
    }

    @Override
    public InsertSetMoreStep<TreasureEventRecord> insertQuery(final Configuration configuration,
                                                              final Event dto) {
        return super.insertQuery(configuration, dto)
                    .set(TABLE.CONTRIBUTION, Numbers.parse(dto.getContribution()))
                    .set(TABLE.TOTAL, Numbers.parse(dto.getTotal()))
                    .set(TABLE.DEADLINE, Dates.parse(dto.getDeadline()));
    }

    @Override
    public UpdateSetMoreStep<TreasureEventRecord> updateQuery(final Configuration configuration,
                                                              final Event dto) {
        return super.updateQuery(configuration, dto)
                    .set(TABLE.CONTRIBUTION, Numbers.parse(dto.getContribution()))
                    .set(TABLE.TOTAL, Numbers.parse(dto.getTotal()))
                    .set(TABLE.DEADLINE, Dates.parse(dto.getDeadline()));
    }

    @Override
    protected Event toDTO(final Record record) {
        return new Event(
        GUIDs.format(record, UUID),
        Strings.format(record, NAME),
        Numbers.format(record, TABLE.CONTRIBUTION),
        Numbers.format(record, TABLE.TOTAL),
        Dates.format(record, TABLE.DEADLINE),
        Dates.format(record, CREATION_DATE),
        formatUser(record, AUTHOR));
    }

    /**
     * Преобзазование записи из БД в короткий объект.
     *
     * @param record Запись из БД.
     * @return Объект.
     */
    public static Option toOption(final Record record) {
        if (record.getValue(TREASURE_EVENT.GUID, java.util.UUID.class) == null) {
            return null;
        }

        return new Option(
            GUIDs.format(record, TREASURE_EVENT.GUID),
            Strings.format(record, TREASURE_EVENT.NAME));
    }
}
