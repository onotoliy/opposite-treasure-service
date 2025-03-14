package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Event(
        UUID uuid,
        String name,
        BigDecimal contribution,
        BigDecimal total,
        Instant deadline,
        Instant creationDate,
        Option author,
        Instant deletionDate
) implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

    // Переопределение метода toString()
    @Override
    public String toString() {
        return "{\n" +
                "  'uuid': '" + uuid + "', \n" +
                "  'name': '" + name + "', \n";

    }}