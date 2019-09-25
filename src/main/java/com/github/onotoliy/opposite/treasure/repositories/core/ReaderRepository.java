package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Базовый репозиторий чтения записей из БД.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ReaderRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter> {

    /**
     * Получение опционального объекта.
     *
     * @param uuid Уникальный идентификатор.
     * @return Опциональный объект.
     */
    Optional<E> getOptional(UUID uuid);

    /**
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор.
     * @return Объект
     */
    E get(UUID uuid);

    /**
     * Получение списка всех объектов.
     *
     * @return Объекты.
     */
    List<Option> getAll();

    /**
     * Поиск объектов.
     *
     * @param parameter Поисковые параметры.
     * @return Объекты.
     */
    Page<E> getAll(P parameter);
}
