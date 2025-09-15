package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
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
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание объекта")
    E create(@RequestBody E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Измененный объект.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Изменение объекта")
    E update(@RequestBody E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     */
    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Удаление объекта")
    void delete(@PathVariable("uuid") UUID uuid);

}
