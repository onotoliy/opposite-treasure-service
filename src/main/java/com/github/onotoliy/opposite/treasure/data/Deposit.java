package com.github.onotoliy.opposite.treasure.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Депозит.
 *
 * @param uuid        Уникальный иденитификатор.
 * @param username    Имя пользователя.
 * @param firstName   Имя.
 * @param lastName    Фамилия.
 * @param patronymic  Отчество.
 * @param deposit     Депозит.
 * @param logo        Аватар.
 * @param email       Адрес электронной почты.
 * @param birthday    День рождения.
 * @param joiningDate Дата вступления.
 * @param position    Должность.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Депозит")
public record Deposit(
    @Schema(description = "Уникальный иденитификатор", requiredMode = Schema.RequiredMode.REQUIRED)
    UUID uuid,
    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    String username,
    @Schema(description = "Имя", requiredMode = Schema.RequiredMode.REQUIRED)
    String firstName,
    @Schema(description = "Фамилия", requiredMode = Schema.RequiredMode.REQUIRED)
    String lastName,
    @Schema(description = "Отчество", requiredMode = Schema.RequiredMode.REQUIRED)
    String patronymic,
    @Schema(type = "string", description = "Депозит", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal deposit,
    @Schema(description = "Аватар")
    String logo,
    @Schema(description = "Адрес электронной почты", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,
    @Schema(description = "День рождения", requiredMode = Schema.RequiredMode.REQUIRED)
    Instant birthday,
    @Schema(description = "Дата вступления", requiredMode = Schema.RequiredMode.REQUIRED)
    Instant joiningDate,
    @Schema(description = "Должность", requiredMode = Schema.RequiredMode.REQUIRED)
    Position position
) implements HasUUID, HasName {

    @Override
    public String name() {
        return List
            .of(
                Optional.ofNullable(lastName)
                        .orElse(""),
                Optional.ofNullable(firstName)
                        .filter(it -> !it.isBlank())
                        .map(it -> it.substring(0, 1).toUpperCase() + ".")
                        .orElse(""),
                Optional.ofNullable(patronymic)
                        .filter(it -> !it.isBlank())
                        .map(it -> it.substring(0, 1).toUpperCase() + ".")
                        .orElse("")
            )
            .stream()
            .filter(it -> !it.isBlank())
            .collect(Collectors.joining(" "));
    }
}
