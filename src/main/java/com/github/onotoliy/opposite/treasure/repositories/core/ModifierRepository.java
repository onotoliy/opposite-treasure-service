package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import org.jooq.Configuration;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Интерфейс базового репозитория управления записями из БД.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры.
 * @author Anatoliy Pokhresnyi
 */
public interface ModifierRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter>
extends ReaderRepository<E, P> {

    /**
     * Выполение запроса в транзакции.
     *
     * @param consumer Запрос.
     */
    void transaction(Consumer<Configuration> consumer);

    /**
     * Создание объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    E create(E dto);

    /**
     * Создание объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     * @return Объект.
     */
    E create(Configuration configuration, E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Объект.
     */
    E update(E dto);

    /**
     * Изменение объекта.
     *
     * @param configuration Настройки транзакции.
     * @param dto Объект.
     * @return Объект.
     */
    E update(Configuration configuration, E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор.
     */
    void delete(UUID uuid);

    /**
     * Удаление объекта.
     *
     * @param configuration Настройки транзакции.
     * @param uuid Уникальный идентификатор.
     */
    void delete(Configuration configuration, UUID uuid);
}
