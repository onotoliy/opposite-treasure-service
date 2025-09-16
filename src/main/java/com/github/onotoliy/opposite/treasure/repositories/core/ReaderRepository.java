package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.Record;

/**
 * Базовый репозиторий чтения записей из БД.
 *
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ReaderRepository<P extends SearchParameter> {

    /**
     * Получение опционального объекта.
     *
     * @param uuid Уникальный идентификатор.
     * @return Опциональный объект.
     */
    Optional<Record> getOptional(UUID uuid);

    /**
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор.
     * @return Объект
     */
    Record get(UUID uuid);

    /**
     * Поиск объектов.
     *
     * @param parameter Поисковые параметры.
     * @return Объекты.
     */
    List<Record> getAll(P parameter);

    /**
     * Количество найденых объектов.
     *
     * @param parameter Поисковые параметры.
     * @return Объекты.
     */
    int count(P parameter);
}
