package com.github.onotoliy.opposite.treasure.data.core;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Информация об ошибке.
 *
 * @param status  Статус.
 * @param message Сообщение об ошибке
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Информация об ошибке")
public record ExceptionInformation(
    @Schema(description = "Статус", requiredMode = Schema.RequiredMode.REQUIRED)
    HTTPStatus status,
    @Schema(description = "Сообщение об ошибке", requiredMode = Schema.RequiredMode.REQUIRED)
    String message
) {

}
