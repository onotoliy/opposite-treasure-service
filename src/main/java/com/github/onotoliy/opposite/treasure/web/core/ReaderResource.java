package com.github.onotoliy.opposite.treasure.web.core;


import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
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
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объекта")
    E get(@PathVariable("uuid") UUID uuid);

}
