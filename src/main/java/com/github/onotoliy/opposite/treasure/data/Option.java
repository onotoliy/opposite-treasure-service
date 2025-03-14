package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;

import java.util.UUID;

public record Option(
        UUID uuid,
        String name
) implements HasUUID, HasName {

    // Переопределение toString метода
    @Override
    public String toString() {
        return "{\"uuid\": \"" + uuid + "\", \"name\": \"" + name + "\"}";
    }
}