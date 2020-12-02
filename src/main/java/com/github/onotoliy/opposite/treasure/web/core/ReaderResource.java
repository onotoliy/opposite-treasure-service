package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Интерфейс базового WEB сервиса чтения данных.
 *
 * @param <E> Объект.
 * @author Anatoliy Pokhresnyi
 */
public interface ReaderResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor> {


    /**
     * Получение версии сущности.
     *
     * @return Версия сущности.
     */
    @GetMapping(value = "/version")
    Option version();

    /**
     * Данные, которые необходимо синхронизировать.
     *
     * @param version Версия объекта.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Данные, которые необходимо синхронизировать.
     */
    @GetMapping(value = "/sync")
    Page<E> sync(
            @RequestParam(value = "version")
            long version,
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            int numberOfRows
    );

    /**
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     * @return Объект.
     */
    @GetMapping(value = "/{uuid}")
    E get(@PathVariable("uuid") UUID uuid);

}
