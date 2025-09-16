package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.core.ModifierRepository;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import org.jooq.Configuration;

import java.util.UUID;
import org.jooq.Record;

/**
 * Базовый сервис управления объектами.
 *
 * @param <E> Объект.
 * @param <Rec> Запись из БД.
 * @param <P> Поисковые параметры.
 * @param <R> Репозиторий.
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractModifierService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    Rec extends Record,
    P extends SearchParameter,
    R extends ModifierRepository<Rec, P>>
extends AbstractReaderService<E, P, R>
implements ModifierService<E, P> {

    /**
     * Конструктор.
     *
     * @param repository Репозиторий.
     */
    public AbstractModifierService(final R repository) {
        super(repository);
    }

    protected abstract Rec toRecord(E dto);

    @Override
    public E create(final E dto) {
        repository.transaction(configuration -> create(configuration, dto));

        return get(GUIDs.parse(dto));
    }

    /**
     * Создание объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     */
    protected void create(final Configuration configuration, final E dto) {
        repository.create(configuration, toRecord(dto));
    }

    @Override
    public E update(final E dto) {
        repository.transaction(configuration -> update(configuration, dto));

        return get(GUIDs.parse(dto));
    }

    /**
     * Изменение объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     */
    protected void update(final Configuration configuration, final E dto) {
        repository.update(configuration, toRecord(dto));
    }

    @Override
    public void delete(final UUID uuid) {
        repository.transaction(configuration -> delete(configuration, uuid));
    }

    /**
     * Удаление объекта.
     *
     * @param configuration Настройки транзакции.
     * @param uuid Уникальный идентификатор.
     */
    protected void delete(final Configuration configuration, final UUID uuid) {
        repository.delete(configuration, uuid);
    }

}
