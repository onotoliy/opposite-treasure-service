package com.github.onotoliy.opposite.treasure.data.page;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Описание страницы.
 *
 * @param start Начальный элемент.
 * @param size Размер страницы.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Описание страницы")
public record Paging(
    @Schema(description = "Начальный элемент", requiredMode = Schema.RequiredMode.REQUIRED)
    int start,
    @Schema(description = "Размер страницы", requiredMode = Schema.RequiredMode.REQUIRED)
    int size
) {

}
