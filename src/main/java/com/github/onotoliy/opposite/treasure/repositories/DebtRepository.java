package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Debt;
import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.data.page.Paging;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Numbers;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEBT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEPOSIT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EVENT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_VERSION;

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
     * Получение версии сущности.
     *
     * @return Версия сущности.
     */
    public Option version() {
        return dsl.select()
                  .from(TREASURE_VERSION)
                  .where(TREASURE_VERSION.NAME.eq(TREASURE_DEBT.getName()))
                  .fetchOptional(record -> new Option(
                      Strings.format(record, TREASURE_VERSION.NAME),
                      Numbers.format(record, TREASURE_VERSION.VERSION)
                  ))
                  .orElse(new Option(TREASURE_DEBT.getName(), "0"));
    }

    /**
     * Данные, которые необходимо синхронизировать.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Данные, которые необходимо синхронизировать.
     */
    public Page<Debt> sync(final int offset, final int numberOfRows) {
        return new Page<>(
            new Meta(
                dsl.selectCount()
                   .from(TREASURE_DEBT)
                   .fetchOptional(0, int.class)
                   .orElse(0),
                new Paging(offset, numberOfRows)),
            dsl.select()
               .from(TREASURE_DEBT)
               .join(TREASURE_EVENT)
               .on(TREASURE_EVENT.GUID.eq(TREASURE_DEBT.EVENT_GUID))
               .join(TREASURE_DEPOSIT)
               .on(TREASURE_DEPOSIT.USER_UUID.eq(TREASURE_DEBT.USER_UUID))
               .offset(offset)
               .limit(numberOfRows)
               .fetch(record -> new Debt(
                   EventRepository.toDTO(
                       record,
                       user.find(record.getValue(TREASURE_EVENT.AUTHOR))
                   ),
                   DepositRepository.toDTO(user, record)
               )));
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
            .orderBy(Collections.singleton(TREASURE_EVENT.DEADLINE.desc()))
            .fetch(record -> EventRepository.toDTO(
                record, user.find(record.getValue(TREASURE_EVENT.AUTHOR))));

        return new Page<>(new Meta(list.size(),
                                   new Paging(0, list.size())),
                          list);
    }

    /**
     * Получение должников.
     *
     * @param event Событие.
     * @return Список должников.
     */
    public Page<Deposit> getDebtors(final UUID event) {
        List<Deposit> list = dsl
            .select()
            .from(TREASURE_DEBT)
            .join(TREASURE_DEPOSIT)
            .on(TREASURE_DEPOSIT.USER_UUID.eq(TREASURE_DEBT.USER_UUID))
            .where(TREASURE_DEBT.EVENT_GUID.eq(event))
            .fetch(record -> DepositRepository.toDTO(user, record));

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
        setVersion(configuration);

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
        setVersion(configuration);

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
        setVersion(configuration);

        DSL.using(configuration)
           .deleteFrom(TREASURE_DEBT)
           .where(TREASURE_DEBT.EVENT_GUID.eq(event))
           .execute();
    }

    /**
     * Изменение версии справочника.
     *
     * @param configuration Настройка транзакции.
     */
    private void setVersion(
        final Configuration configuration
    ) {
        BigDecimal version = BigDecimal.valueOf(Dates.now().getTime());

        DSL.using(configuration)
           .update(TREASURE_VERSION)
           .set(TREASURE_VERSION.VERSION, version)
           .where(TREASURE_VERSION.NAME.eq(TREASURE_DEBT.getName()))
           .execute();
    }
}
