package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.data.page.Paging;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;

import java.util.List;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;

/**
 * Репозиторий управления долгами пользователя.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class DebtRepository {

    /**
     * Контекст подключения к БД.
     */
    private final DSLContext dsl;

    /**
     * Сервис чтения пользователей.
     */
    private final KeycloakRPC user;

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    @Autowired
    public DebtRepository(final DSLContext dsl, final KeycloakRPC user) {
        this.dsl = dsl;
        this.user = user;
    }

    /**
     * Получение событий по которым пользователь должен.
     *
     * @param person Пользователь.
     * @return События.
     */
    public Page<Event> getDebts(final UUID person) {
        List<Event> list = dsl
            .select()
            .from(TREASURE_DEBT)
            .join(TREASURE_EVENT)
            .on(TREASURE_EVENT.GUID.eq(TREASURE_DEBT.EVENT_GUID))
            .where(TREASURE_DEBT.USER_UUID.eq(person))
            .fetch(record -> EventRepository.toDTO(
                record, user.find(record.getValue(TREASURE_EVENT.AUTHOR))));

        return new Page<>(new Meta(list.size(),
                                   new Paging(0, list.size())),
                          list);
    }

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

    /**
     * Списание долга пользователей.
     *
     * @param configuration Настройки транзакции.
     * @param event Событие
     */
    public void contribution(final Configuration configuration,
                             final UUID event) {
        DSL.using(configuration)
           .deleteFrom(TREASURE_DEBT)
           .where(TREASURE_DEBT.EVENT_GUID.eq(event))
           .execute();
    }
}
