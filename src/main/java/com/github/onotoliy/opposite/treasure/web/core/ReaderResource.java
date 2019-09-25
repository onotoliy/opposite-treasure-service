package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     * @return Объект.
     */
    @GetMapping(value = "/{uuid}")
    E get(@PathVariable("uuid") UUID uuid);

}
