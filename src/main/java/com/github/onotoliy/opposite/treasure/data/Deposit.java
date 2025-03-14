package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;

import java.util.UUID;

public record Deposit(
        UUID uuid,
        String name,
        String deposit
) implements HasUUID, HasName {

    // Переопределение метода toString()
    @Override
    public String toString() {
        return "{\"uuid\": \"" + uuid + "\", \"name\": \"" + name + "\", \"deposit\": \"" + deposit + "\"}";
    }
}