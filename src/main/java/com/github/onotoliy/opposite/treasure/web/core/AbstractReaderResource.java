package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.data.SearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Page;
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
    S extends ReaderService<E, P>> implements ReaderResource<E, P> {

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
    public Page<E> getAll(final P parameter) {
        return service.getAll(parameter);
    }

}
