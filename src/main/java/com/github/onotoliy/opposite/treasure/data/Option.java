package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Короткая информация об объекте.
 *
 * @param uuid Уникальный иденитификатор.
 * @param name Название.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Короткая информация об объекте")
public record Option(
    @Schema(description = "Уникальный иденитификатор")
    UUID uuid,
    @Schema(description = "Название")
    String name
) implements HasUUID, HasName {

}
