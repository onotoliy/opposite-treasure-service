package com.github.onotoliy.opposite.treasure.web.core;


import com.github.onotoliy.opposite.treasure.data.SearchParameter;
import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Интерфейс базового WEB сервиса чтения данных.
 *
 * @param <E> Объект.
 * @param <P> Поисковые параметры объекта.
 * @author Anatoliy Pokhresnyi
 */
public interface ReaderResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter> {

    /**
     * Получение объекта.
     *
     * @param uuid Уникальный идентификатор объекта.
     * @return Объект.
     */
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объекта")
    E get(
            @Parameter(
                    description = "Уникальный идентификатор объекта",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable("uuid") UUID uuid
    );

    /**
     * Поиск транзакций.
     *
     * @param parameter Поисковые параметры.
     * @return Транзакции.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Поиск транзакций")
    Page<E> getAll(@ParameterObject P parameter);

}
