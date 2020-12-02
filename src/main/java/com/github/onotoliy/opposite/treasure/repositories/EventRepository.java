package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureEventRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
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
    public EventRepository(final DSLContext dsl, final KeycloakRPC user) {
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

    @Override
    public List<Condition> where(final EventSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasName()) {
            conditions.add(TREASURE_EVENT.NAME.likeIgnoreCase(
                    "%" + parameter.getName() + "%"));
        }

        return conditions;
    }

    @Override
    public InsertSetMoreStep<TreasureEventRecord> insertQuery(
            final Configuration configuration,
            final Event dto) {
        return super.insertQuery(configuration, dto)
                    .set(table.CONTRIBUTION,
                         Numbers.parse(dto.getContribution()))
                    .set(table.TOTAL, Numbers.parse(dto.getTotal()))
                    .set(table.DEADLINE, Dates.parse(dto.getDeadline()));
    }

    @Override
    public UpdateSetMoreStep<TreasureEventRecord> updateQuery(
            final Configuration configuration,
            final Event dto) {
        return super.updateQuery(configuration, dto)
                    .set(table.CONTRIBUTION,
                         Numbers.parse(dto.getContribution()))
                    .set(table.TOTAL, Numbers.parse(dto.getTotal()))
                    .set(table.DEADLINE, Dates.parse(dto.getDeadline()));
    }

    @Override
    protected Event toDTO(final Record record) {
        return toDTO(record, formatUser(record, author));
    }

    /**
     * Преобзазование записи из БД в объект.
     *
     * @param record Запись из БД.
     * @param author Пользователь.
     * @return Объект.
     */
    public static Event toDTO(final Record record, final Option author) {
        String deletionDate =
            Dates.format(record, TREASURE_EVENT.DELETION_DATE);

        return new Event(
            GUIDs.format(record, TREASURE_EVENT.GUID),
            Strings.format(record, TREASURE_EVENT.NAME),
            Numbers.format(record, TREASURE_EVENT.CONTRIBUTION),
            Numbers.format(record, TREASURE_EVENT.TOTAL),
            Dates.format(record, TREASURE_EVENT.DEADLINE),
            Dates.format(record, TREASURE_EVENT.CREATION_DATE),
            author,
            deletionDate.equals("—") ? null : deletionDate
        );
    }

    /**
     * Преобзазование записи из БД в короткий объект.
     *
     * @param record Запись из БД.
     * @return Объект.
     */
    public static Option toOption(final Record record) {
        String uuid = GUIDs.format(record, TREASURE_EVENT.GUID);

        return Strings.isEmpty(uuid)
            ? null
            : new Option(uuid, Strings.format(record, TREASURE_EVENT.NAME));
    }
}
