package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.Option;
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
import java.util.UUID;

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
                    .set(table.CONTRIBUTION, dto.contribution())
                    .set(table.TOTAL, dto.total())
                    .set(table.DEADLINE, dto.deadline());
    }

    @Override
    public UpdateSetMoreStep<TreasureEventRecord> updateQuery(
            final Configuration configuration,
            final Event dto) {
        return super.updateQuery(configuration, dto)
                    .set(table.CONTRIBUTION, dto.contribution())
                    .set(table.TOTAL, dto.total())
                    .set(table.DEADLINE, dto.deadline());
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
        return new Event(
                record.getValue(  TREASURE_EVENT.GUID),
            Strings.format(record, TREASURE_EVENT.NAME),
                record.getValue(  TREASURE_EVENT.CONTRIBUTION),
                record.getValue( TREASURE_EVENT.TOTAL),
            record.getValue(TREASURE_EVENT.DEADLINE),
                record.getValue( TREASURE_EVENT.CREATION_DATE),
            author,
                record.getValue(TREASURE_EVENT.DELETION_DATE)
        );
    }

    /**
     * Преобзазование записи из БД в короткий объект.
     *
     * @param record Запись из БД.
     * @return Объект.
     */
    public static Option toOption(final Record record) {
        UUID uuid = record.get(TREASURE_EVENT.GUID);

        return uuid == null
            ? null
            : new Option(uuid, Strings.format(record, TREASURE_EVENT.NAME));
    }
}
