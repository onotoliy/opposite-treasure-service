package com.github.onotoliy.opposite.treasure.repositories;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
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
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     */
    @Autowired
    public DebtRepository(final DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Получение событий по которым пользователь должен.
     *
     * @param person Пользователь.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return События.
     */
    public List<Record> getDebts(final UUID person, final int offset, final int numberOfRows) {
        return dsl
            .select()
            .from(TREASURE_DEBT)
            .join(TREASURE_EVENT)
            .on(TREASURE_EVENT.GUID.eq(TREASURE_DEBT.EVENT_GUID))
            .where(TREASURE_DEBT.USER_UUID.eq(person))
            .orderBy(TREASURE_EVENT.DEADLINE.desc())
            .offset(offset)
            .limit(numberOfRows)
            .fetch();
    }

    /**
     * Количество событий по которым пользователь должен.
     *
     * @param person Пользователь.
     * @return Количество событий по которым пользователь должен.
     */
    public int countDebts(final UUID person) {
        return dsl.selectCount()
            .from(TREASURE_DEBT)
            .where(TREASURE_DEBT.USER_UUID.eq(person))
            .fetchOptional(0, int.class)
            .orElse(0);
    }

    /**
     * Получение должников.
     *
     * @param event Событие.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Список должников.
     */
    public List<UUID> getDebtors(final UUID event, final int offset, final int numberOfRows) {
        return dsl
            .select()
            .from(TREASURE_DEBT)
            .where(TREASURE_DEBT.EVENT_GUID.eq(event))
            .orderBy(Collections.singleton(TREASURE_DEBT.USER_UUID.desc()))
            .offset(offset)
            .limit(numberOfRows)
            .fetch(record -> record.getValue(TREASURE_DEBT.USER_UUID));
    }

    /**
     * Количество должников.
     *
     * @param event Событие.
     * @return Количество должников.
     */
    public int countDebtors(final UUID event) {
        return dsl.selectCount()
                  .from(TREASURE_DEBT)
                  .where(TREASURE_DEBT.EVENT_GUID.eq(event))
                  .fetchOptional(0, int.class)
                  .orElse(0);
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
