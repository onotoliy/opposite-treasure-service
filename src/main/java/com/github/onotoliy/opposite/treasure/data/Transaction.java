package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(
        UUID uuid,
        String name,
        BigDecimal cash,
        TransactionType type,
        Option person,
        Option event,
        Instant transactionDate,
        Instant creationDate,
        Option author,
        Instant deletionDate
) implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

    @Override
    public String toString() {
        return "{\n" +
                "  'uuid': '" + uuid + "', \n" +
                "  'name': '" + name + "', \n" +
                "  'cash': '" + cash + "', \n" +
                "  'type': '" + type + "', \n" +
                "  'person': " + person + ", \n" +
                "  'event': " + event + ", \n" +
                "  'transactionDate': '" + transactionDate + "', \n" +
                "  'creationDate': '" + creationDate + "', \n" +
                "  'author': " + author + ", \n" +
                "  'deletionDate': '" + deletionDate + "'\n" +
                "}";
    }

}
