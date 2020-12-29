package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.SyncResponse;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.bpp.log.Log;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;

import java.util.UUID;

/**
 * Интерфейс базового сервиса управления объектами.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ModifierService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter>
extends ReaderService<E, P> {

    /**
     * Создание объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    @Log(db = true)
    E create(E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    @Log(db = true)
    E update(E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор.
     */
    @Log(db = true)
    void delete(UUID uuid);

    /**
     * Синхронизация объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    @Log(db = true)
    SyncResponse sync(E dto);
}
