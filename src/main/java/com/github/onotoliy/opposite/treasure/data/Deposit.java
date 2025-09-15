package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Депозит.
 *
 * @param uuid Уникальный иденитификатор.
 * @param username Имя пользователя.
 * @param firstName Имя.
 * @param lastName Фамилия.
 * @param patronymic Отчество.
 * @param deposit Депозит.
 * @param logo Аватар.
 * @param email Адрес электронной почты.
 * @param birthday День рождения.
 * @param joiningDate Дата вступления.
 * @param position Должность.
 * @author Anatoliy Pokhresnyi
 */
@Schema(description = "Депозит")
public record Deposit(
    @Schema(description = "Уникальный иденитификатор")
    UUID uuid,
    @Schema(description = "Имя пользователя")
    String username,
    @Schema(description = "Имя")
    String firstName,
    @Schema(description = "Фамилия")
    String lastName,
    @Schema(description = "Отчество")
    String patronymic,
    @Schema(type = "string", description = "Депозит")
    BigDecimal deposit,
    @Schema(description = "Аватар")
    String logo,
    @Schema(description = "Адрес электронной почты")
    String email,
    @Schema(description = "День рождения")
    Instant birthday,
    @Schema(description = "Дата вступления")
    Instant joiningDate,
    @Schema(description = "Должность")
    Position position
) implements HasUUID, HasName {

    @Override
    public String name() {
        return firstName + " " + patronymic + " " + lastName;
    }
}
