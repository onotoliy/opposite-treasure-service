package com.github.onotoliy.opposite.treasure.dto;


import com.github.onotoliy.opposite.treasure.data.Option;
import com.github.onotoliy.opposite.treasure.data.core.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public record Notification(
        UUID uuid,
        String name,
        String message,
        NotificationType notificationType,
        String executor,
        Instant deliveryDate,
        Instant creationDate,
        Option author,
        Instant deletionDate
) implements HasUUID, HasName, HasCreationDate, HasAuthor, HasDeletionDate {

}

