package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import java.util.UUID;

/**
 * Базовый сервис чтения объектов.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ReaderService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter> {

    /**
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор.
     * @return Объект
     */
    E get(UUID uuid);

    /**
     * Поиск объектов.
     *
     * @param parameter Поисковые параметры.
     * @return Объекты.
     */
    Page<E> getAll(P parameter);

}
