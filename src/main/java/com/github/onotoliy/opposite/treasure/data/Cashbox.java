package com.github.onotoliy.opposite.treasure.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Информация о кассе.
 *
 * @param deposit Сумма в кассе.
 * @param lastUpdateDate Дата последнего изменения.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Информация о кассе")
public record Cashbox(
    @Schema(type = "string", description = "Сумма в кассе")
    BigDecimal deposit,
    @Schema(description = "Дата последнего изменения")
    Instant lastUpdateDate
) {

}
