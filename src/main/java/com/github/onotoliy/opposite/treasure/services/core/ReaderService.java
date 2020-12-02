package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.bpp.log.Log;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;

import java.util.List;
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

    /**
     * Получение версии сущности.
     *
     * @return Версия сущности.
     */
    @Log(db = true)
    Option version();

    /**
     * Данные, которые необходимо синхронизировать.
     *
     * @param version Версия объекта.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Данные, которые необходимо синхронизировать.
     */
    @Log(db = true)
    Page<E> sync(long version, int offset, int numberOfRows);
}
