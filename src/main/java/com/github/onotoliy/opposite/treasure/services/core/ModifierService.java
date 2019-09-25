package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
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
    E create(E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    E update(E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор.
     */
    void delete(UUID uuid);
}
