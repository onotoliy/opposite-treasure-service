package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasAuthor;
import com.github.onotoliy.opposite.treasure.data.core.HasCreationDate;
import com.github.onotoliy.opposite.treasure.data.core.HasDeletionDate;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Событие.
 *
 * @param uuid Уникальный иденитификатор.
 * @param name Название.
 * @param contribution Сумма взноса.
 * @param deadline До какого числа сдать.
 * @param creationDate Дата создания.
 * @param author Автор.
 * @param deletionDate Дата удаления.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Событие")
public record Event(
    @Schema(description = "Уникальный иденитификатор")
    UUID uuid,
    @Schema(description = "Название")
    String name,
    @Schema(type = "string", description = "Сумма взноса")
    BigDecimal contribution,
    @Schema(description = "До какого числа сдать")
    Instant deadline,
    @Schema(description = "Дата создания")
    Instant creationDate,
    @Schema(description = "Автор")
    Option author,
    @Schema(description = "Дата удаления")
    Instant deletionDate
) implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

}
