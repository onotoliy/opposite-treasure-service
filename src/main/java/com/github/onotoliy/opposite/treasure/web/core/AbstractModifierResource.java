package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.SyncResponse;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;

import java.util.UUID;

/**
 * Базовый WEB сервис управления данными.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры объекта.
 * @param <S> Сервис чтения записей.
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractModifierResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    S extends ModifierService<E, P>>
extends AbstractReaderResource<E, P, S>
implements ModifierResource<E> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    public AbstractModifierResource(final S service) {
        super(service);
    }

    @Override
    public E create(final E dto) {
        return service.create(dto);
    }

    @Override
    public E update(final E dto) {
        return service.update(dto);
    }

    @Override
    public void delete(final UUID uuid) {
        service.delete(uuid);
    }

    @Override
    public SyncResponse sync(final E dto) {
        return service.sync(dto);
    }
}
