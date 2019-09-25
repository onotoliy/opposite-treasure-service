package com.github.onotoliy.opposite.treasure.repositories;

import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;

/**
 * Репозиторий управления долгами пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class DebtRepository {

    /**
     * Назначение долга пользователя.
     *
     * @param configuration Настройки транзакции.
     * @param person Пользователь.
     * @param event Событие
     */
    public void cost(final Configuration configuration,
                     final UUID person,
                     final UUID event) {
        DSL.using(configuration)
           .insertInto(TREASURE_DEBT)
           .set(TREASURE_DEBT.USER_UUID, person)
           .set(TREASURE_DEBT.EVENT_GUID, event)
           .execute();
    }

    /**
     * Списание долга пользователя.
     *
     * @param configuration Настройки транзакции.
     * @param person Пользователь.
     * @param event Событие
     */
    public void contribution(final Configuration configuration,
                             final UUID person,
                             final UUID event) {
        DSL.using(configuration)
           .deleteFrom(TREASURE_DEBT)
           .where(
               TREASURE_DEBT.USER_UUID.eq(person),
               TREASURE_DEBT.EVENT_GUID.eq(event))
           .execute();
    }
}
