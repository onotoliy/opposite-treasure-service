package com.github.onotoliy.opposite.treasure.data.page;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Мета данные о странице.
 *
 * @param total Общее количество записей.
 * @param paging Описание страницы.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Мета данные о странице")
public record Meta(
    @Schema(description = "Общее количество записей", requiredMode = Schema.RequiredMode.REQUIRED)
    int total,
    @Schema(description = "Описание страницы", requiredMode = Schema.RequiredMode.REQUIRED)
    Paging paging
) {

}
