package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import java.util.UUID;
import java.util.function.Consumer;
import org.jooq.Configuration;
import org.jooq.Record;

/**
 * Интерфейс базового репозитория управления записями из БД.
 *
 * @param <R> Объект.
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ModifierRepository<
    R extends Record,
    P extends SearchParameter>
extends ReaderRepository<P> {

    /**
     * Выполение запроса в транзакции.
     *
     * @param consumer Запрос.
     */
    void transaction(Consumer<Configuration> consumer);

    /**
     * Создание объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     * @return Объект.
     */
    R create(Configuration configuration, R dto);

    /**
     * Изменение объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     * @return Объект.
     */
    R update(Configuration configuration, R dto);

    /**
     * Удаление объекта.
     *
     * @param configuration Настройки транзакции.
     * @param uuid Уникальный идентификатор.
     */
    void delete(Configuration configuration, UUID uuid);
}
