package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.exceptions.NotUniqueException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

/**
 * Базовый репозиторий управления записями из БД.
 *
 * @param <P> Поисковые параметры.
 * @param <R> Запись из БД
 * @param <T> Таблица в БД
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractModifierRepository<
    P extends SearchParameter,
    R extends Record,
    T extends Table<R>>
extends AbstractReaderRepository<P, R, T>
implements ModifierRepository<R, P> {

    /**
     * Конструктор.
     *
     * @param table        Таблица.
     * @param uuid         Уникальный идентификатор.
     * @param creationDate Дата создания.
     * @param deletionDate Дата удаления.
     * @param dsl          Контекст подключения к БД.
     */
    protected AbstractModifierRepository(
        final T table,
        final TableField<R, UUID> uuid,
        final TableField<R, Instant> creationDate,
        final TableField<R, Instant> deletionDate,
        final DSLContext dsl
    ) {
        super(table, uuid, creationDate, deletionDate, dsl);
    }

    @Override
    public void transaction(final Consumer<Configuration> consumer) {
        dsl.transaction(consumer::accept);
    }

    @Override
    public R create(final Configuration configuration, final R dto) {
        return execute(dto, insertQuery(configuration, dto));
    }

    @Override
    public R update(final Configuration configuration, final R dto) {
        return execute(dto, updateQuery(configuration, dto).where(
            uuid.eq(dto.get(uuid))));
    }

    @Override
    public void delete(final Configuration configuration, final UUID uuid) {
        execute(uuid, deleteQuery(configuration, uuid));
    }

    /**
     * Получение delete from запроса из таблицы.
     *
     * @param configuration Настройка транзакции.
     * @param uuid          Уникальный идентификатор.
     * @return Запрос.
     */
    protected UpdateConditionStep<R> deleteQuery(
        final Configuration configuration,
        final UUID uuid
    ) {
        return DSL.using(configuration)
                  .update(table)
                  .set(deletionDate, Instant.now())
                  .where(this.uuid.eq(uuid));
    }

    /**
     * Получение insert into запроса из таблицы.
     *
     * @param configuration Настройка транзакции.
     * @param dto           Объект.
     * @return Запрос.
     */
    protected InsertSetMoreStep<R> insertQuery(
        final Configuration configuration,
        final R dto
    ) {
        return DSL.using(configuration)
                  .insertInto(table)
                  .set(dto);
    }

    /**
     * Получение update запроса из таблицы.
     *
     * @param configuration Настройка транзакции.
     * @param dto           Объект.
     * @return Запрос.
     */
    protected UpdateSetMoreStep<R> updateQuery(
        final Configuration configuration,
        final R dto
    ) {
        return DSL.using(configuration)
                  .update(table)
                  .set(dto);
    }

    /**
     * Исполнение запроса на обновление данных.
     *
     * @param dto   Объект.
     * @param query Запрос.
     * @return Объект.
     */
    private R execute(final R dto, final Query query) {
        execute(dto.get(uuid), query);

        return dto;
    }

    /**
     * Исполнение запроса на обновление данных.
     *
     * @param uuid  Уникальный идентификатор объекта.
     * @param query Запрос.
     */
    private void execute(final UUID uuid, final Query query) {
        int count = query.execute();

        if (count == 0) {
            throw new NotFoundException(table, uuid);
        }

        if (count > 1) {
            throw new NotUniqueException(table, uuid);
        }
    }
}
