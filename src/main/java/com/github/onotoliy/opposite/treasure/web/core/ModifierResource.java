package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Интерфейс базового WEB сервиса управления данными.
 *
 * @param <E> Объект.
 * @author Anatoliy Pokhresnyi
 */
public interface ModifierResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor>
extends ReaderResource<E> {

    /**
     * Создание объекта.
     *
     * @param dto Объект.
     * @return Созданный объект.
     */
    @PostMapping
    E create(@RequestBody E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Измененный объект.
     */
    @PutMapping
    E update(@RequestBody E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     */
    @DeleteMapping(value = "/{uuid}")
    void delete(@PathVariable("uuid") UUID uuid);

    /**
     * Синхронизация объекта.
     *
     * @param dto Объект.
     * @return Измененный объект.
     */
    @PutMapping
    E sync(@RequestBody E dto);

}
