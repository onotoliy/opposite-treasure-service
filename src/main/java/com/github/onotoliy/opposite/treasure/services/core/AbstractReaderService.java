package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.core.ReaderRepository;

import java.util.List;
import java.util.UUID;

/**
 * Базовый сервис чтения объектов.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @param <R> Запись из БД
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractReaderService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends ReaderRepository<E, P>>
implements ReaderService<E, P> {

    /**
     * Репозиторий.
     */
    protected final R repository;

    /**
     * Констрктор.
     *
     * @param repository Репозиторий.
     */
    public AbstractReaderService(final R repository) {
        this.repository = repository;
    }

    @Override
    public E get(final UUID uuid) {
        return repository.get(uuid);
    }

    @Override
    public List<Option> getAll() {
        return repository.getAll();
    }

    @Override
    public Page<E> getAll(final P parameter) {
        return repository.getAll(parameter);
    }

    @Override
    public Option version() {
        return repository.version();
    }

    @Override
    public Page<E> sync(final long version,
                        final int offset,
                        final int numberOfRows) {
        return repository.sync(version, offset, numberOfRows);
    }
}
