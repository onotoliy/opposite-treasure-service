package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.data.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureEventRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;

/**
 * Репозиторий управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class EventRepository extends AbstractModifierRepository<
    EventSearchParameter,
    TreasureEventRecord,
    TreasureEvent
> {

    /**
     * Конструктор.
     *
     * @param dsl  Контекст подключения к БД.
     */
    @Autowired
    public EventRepository(final DSLContext dsl) {
        super(
            TREASURE_EVENT,
            TREASURE_EVENT.GUID,
            TREASURE_EVENT.CREATION_DATE,
            TREASURE_EVENT.DELETION_DATE,
            dsl
        );
    }

    @Override
    public List<Condition> where(final EventSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasName()) {
            conditions.add(TREASURE_EVENT.NAME.likeIgnoreCase(
                "%" + parameter.name() + "%"));
        }

        return conditions;
    }
}
