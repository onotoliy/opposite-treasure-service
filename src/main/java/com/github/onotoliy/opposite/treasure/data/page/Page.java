package com.github.onotoliy.opposite.treasure.data.page;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Страница.
 *
 * @param meta Мета данные о странице.
 * @param context Содержимое страницы.
 * @param <T> Тип объектов на стринице.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Страница")
public record Page<T>(
    @Schema(description = "Мета данные о странице", requiredMode = Schema.RequiredMode.REQUIRED)
    Meta meta,
    @Schema(description = "Содержимое страницы", requiredMode = Schema.RequiredMode.REQUIRED)
    List<T> context
) {

}
