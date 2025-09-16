package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Поисковые параметры для транзакции.
 *
 * @param name Название.
 * @param user Уникальный идентификатор пользователя.
 * @param event Уникальный идентификатор события.
 * @param type Тип.
 * @param offset Количество записей которое необходимо пропустить.
 * @param numberOfRows Размер страницы.
 * @author Anatoliy Pokhresnyi
 */
public record TransactionSearchParameter(
    @Parameter(description = "Название")
    @RequestParam(value = "name", required = false)
    String name,
    @Parameter(
        description = "Уникальный идентификатор пользователя",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @RequestParam(value = "user", required = false)
    UUID user,
    @Parameter(
        description = "Уникальный идентификатор события",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @RequestParam(value = "event", required = false)
    UUID event,
    @Parameter(description = "Тип")
    @RequestParam(value = "type", required = false)
    TransactionType type,
    @Parameter(description = "Количество записей которое необходимо пропустить")
    @RequestParam(
        value = "offset",
        required = false,
        defaultValue = "0"
    )
    int offset,
    @Parameter(description = "Размер страницы")
    @RequestParam(
        value = "numberOfRows",
        required = false,
        defaultValue = "10"
    )
    int numberOfRows
) implements SearchParameter {



    /**
     * Проверяет необходимо ли производить поиск по названию.

     * @return Результат проверки.
     */
    public boolean hasName() {
        return Strings.nonEmpty(name);
    }

    /**
     * Проверяет необходимо ли производить поиск по пользователю.

     * @return Результат проверки.
     */
    public boolean hasUser() {
        return GUIDs.nonEmpty(user);
    }

    /**
     * Проверяет необходимо ли производить поиск по событию.

     * @return Результат проверки.
     */
    public boolean hasEvent() {
        return GUIDs.nonEmpty(event);
    }

    /**
     * Проверяет необходимо ли производить поиск по типу транзакции.

     * @return Результат проверки.
     */
    public boolean hasType() {
        return Objects.nonEmpty(type)
            && Objects.nonEqually(type, TransactionType.NONE);
    }
}
