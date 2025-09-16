package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * Базовый репозиторий чтения записей из БД.
 *
 * @param <P> Поисковые параметры.
 * @param <R> Запись из БД
 * @param <T> Таблица в БД
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractReaderRepository<
    P extends SearchParameter,
    R extends Record,
    T extends Table<R>>
implements ReaderRepository<P> {

    /**
     * Таблица.
     */
    protected final T table;

    /**
     * Уникальный идентификатор.
     */
    protected final TableField<R, UUID> uuid;

    /**
     * Дата создания.
     */
    protected final TableField<R, Instant> creationDate;

    /**
     * Дата удаления.
     */
    protected final TableField<R, Instant> deletionDate;

    /**
     * Контекст подключения к БД.
     */
    protected final DSLContext dsl;

    /**
     * Конструктор.
     *
     * @param table        Таблица.
     * @param uuid         Уникальный идентификатор.
     * @param creationDate Дата создания.
     * @param deletionDate Дата удаления.
     * @param dsl          Контекст подключения к БД.
     */
    protected AbstractReaderRepository(
        final T table,
        final TableField<R, UUID> uuid,
        final TableField<R, Instant> creationDate,
        final TableField<R, Instant> deletionDate,
        final DSLContext dsl
    ) {
        this.table = table;
        this.uuid = uuid;
        this.creationDate = creationDate;
        this.deletionDate = deletionDate;
        this.dsl = dsl;
    }

    /**
     * Получение select запроса из таблицы.
     *
     * @return Запрос.
     */
    protected SelectJoinStep<Record> findQuery() {
        return dsl.select().from(table);
    }

    @Override
    public Optional<Record> getOptional(final UUID uuid) {
        return findQuery().where(this.uuid.eq(uuid)).fetchOptional();
    }

    @Override
    public Record get(final UUID uuid) {
        return getOptional(uuid).orElseThrow(
            () -> new NotFoundException(table, uuid));
    }

    @Override
    public List<Record> getAll(final P parameter) {
        return findQuery().where(where(parameter))
                       .orderBy(orderBy())
                       .offset(parameter.offset())
                       .limit(parameter.numberOfRows())
                       .fetch();
    }

    @Override
    public int count(final P parameter) {
        return dsl.selectCount()
                   .from(table)
                   .where(where(parameter))
                   .fetchOptional(0, int.class)
                   .orElse(0);
    }

    /**
     * Получение колонок по которым будет производиться сортировка.
     *
     * @return Колонки по которым будет производиться сортировка.
     */
    protected List<? extends OrderField<?>> orderBy() {
        return new LinkedList<>(Collections.singleton(creationDate.desc()));
    }

    /**
     * Получение условий выборки данных из БД.
     *
     * @param parameter Поисковы параметры.
     * @return Условия выборки данных из БД.
     */
    protected List<Condition> where(final P parameter) {
        return new LinkedList<>(Collections.singleton(deletionDate.isNull()));
    }
}
