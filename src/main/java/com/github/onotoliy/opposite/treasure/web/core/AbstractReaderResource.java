package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ReaderService;

import java.util.UUID;

/**
 * Базовый WEB сервис чтения данных.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры объекта.
 * @param <S> Сервис чтения записей.
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractReaderResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    S extends ReaderService<E, P>>
implements ReaderResource<E> {

    /**
     * Сервис чтения записей.
     */
    protected final S service;

    /**
     * Конструктор.
     *
     * @param service Сервис чтения записей.
     */
    public AbstractReaderResource(final S service) {
        this.service = service;
    }

    @Override
    public E get(final UUID uuid) {
        return service.get(uuid);
    }

    @Override
    public Option version() {
        return service.version();
    }

    @Override
    public Page<E> sync(final long version,
                        final int offset,
                        final int numberOfRows) {
        return service.sync(version, offset, numberOfRows);
    }
}
