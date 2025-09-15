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
 * Транзакция.
 *
 * @param uuid Уникальный иденитификатор.
 * @param name Название.
 * @param cash Сумма.
 * @param type Тип.
 * @param person Пользователь.
 * @param event Событие.
 * @param transactionDate Дата транзакии.
 * @param creationDate Дата создания.
 * @param author Автор.
 * @param deletionDate Дата удаления.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Транзакция")
public record Transaction(
    @Schema(description = "Уникальный иденитификатор")
    UUID uuid,
    @Schema(description = "Название")
    String name,
    @Schema(type = "string", description = "Сумма")
    BigDecimal cash,
    @Schema(description = "Тип")
    TransactionType type,
    @Schema(description = "Пользователь")
    Option person,
    @Schema(description = "Событие")
    Option event,
    @Schema(description = "Дата транзакии")
    Instant transactionDate,
    @Schema(description = "Дата создания")
    Instant creationDate,
    @Schema(description = "Автор")
    Option author,
    @Schema(description = "Дата удаления")
    Instant deletionDate
) implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

}
