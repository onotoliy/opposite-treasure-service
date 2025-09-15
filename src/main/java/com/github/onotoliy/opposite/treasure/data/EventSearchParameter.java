package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.utils.Strings;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Поисковые параметры для события.
 *
 * @param name Название.
 * @param numberOfRows Размер страницы.
 * @param offset Количество записей которое необходимо пропустить.
 * @author Anatoliy Pokhresnyi
 */
public record EventSearchParameter(
    @Parameter(description = "Название")
    @RequestParam(value = "name", required = false)
    String name,
    @Parameter(description = "Количество записей которое необходимо пропустить")
    @RequestParam(value = "offset", required = false, defaultValue = "10")
    int offset,
    @Parameter(description = "Размер страницы")
    @RequestParam(value = "numberOfRows", required = false, defaultValue = "10")
    int numberOfRows
) implements SearchParameter {

    /**
     * Проверяет необходимо ли производить поиск по названию.

     * @return Результат проверки.
     */
    public boolean hasName() {
        return Strings.nonEmpty(name);
    }

}
