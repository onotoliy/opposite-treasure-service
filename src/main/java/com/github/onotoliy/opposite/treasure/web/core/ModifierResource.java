package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.treasure.data.SearchParameter;
import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Интерфейс базового WEB сервиса управления данными.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры объекта.
 * @author Anatoliy Pokhresnyi
 */
public interface ModifierResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter> extends ReaderResource<E, P> {

    /**
     * Создание объекта.
     *
     * @param dto Объект.
     * @return Созданный объект.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание объекта")
    E create(@Parameter(description = "Объекта") @RequestBody E dto);

    /**
     * Изменение объекта.
     *
     * @param dto Объект.
     * @return Измененный объект.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Изменение объекта")
    E update(@Parameter(description = "Объекта") @RequestBody E dto);

    /**
     * Удаление объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     */
    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Удаление объекта")
    void delete(
        @Parameter(
            description = "Уникальный идентификатор объекта",
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("uuid") UUID uuid
    );

}
