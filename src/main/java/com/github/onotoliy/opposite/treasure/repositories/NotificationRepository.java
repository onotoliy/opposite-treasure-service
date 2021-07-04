package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.dto.Delivery;
import com.github.onotoliy.opposite.treasure.dto.Notification;
import com.github.onotoliy.opposite.treasure.dto.NotificationSearchParameter;
import com.github.onotoliy.opposite.treasure.dto.NotificationType;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureNotification;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureNotificationRecord;
import com.github.onotoliy.opposite.treasure.repositories.core.AbstractModifierRepository;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_NOTIFICATION;

/**
 * Репозиторий управления уведомлениями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class NotificationRepository
extends AbstractModifierRepository<
    Notification,
    NotificationSearchParameter,
    TreasureNotificationRecord,
    TreasureNotification> {

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    @Autowired
    public NotificationRepository(final DSLContext dsl,
                                  final KeycloakRPC user) {
        super(
            TREASURE_NOTIFICATION,
            TREASURE_NOTIFICATION.GUID,
            TREASURE_NOTIFICATION.NAME,
            TREASURE_NOTIFICATION.AUTHOR,
            TREASURE_NOTIFICATION.CREATION_DATE,
            TREASURE_NOTIFICATION.DELETION_DATE,
            dsl,
            user
        );
    }

    /**
     * Уведомление доставленно.
     *
     * @param uuid Уникальный идентификатор.
     */
    public void delivered(final UUID uuid) {
        dsl.update(TREASURE_NOTIFICATION)
           .set(TREASURE_NOTIFICATION.DELIVERY_DATE, Dates.now())
           .where(TREASURE_NOTIFICATION.GUID.eq(uuid))
           .execute();
    }

    @Override
    public List<Condition> where(final NotificationSearchParameter parameter) {
        List<Condition> conditions = super.where(parameter);

        if (parameter.hasDelivery()) {
            if (parameter.getDelivery() == Delivery.ONLY_DELIVERED) {
                conditions.add(
                    TREASURE_NOTIFICATION.DELIVERY_DATE.isNotNull()
                );
            }

            if (parameter.getDelivery() == Delivery.ONLY_NOT_DELIVERED) {
                conditions.add(
                    TREASURE_NOTIFICATION.DELIVERY_DATE.isNull()
                );
            }
        }

        if (parameter.hasType()) {
            conditions.add(
                TREASURE_NOTIFICATION.NOTIFICATION_TYPE
                    .eq(parameter.getType().name())
            );
        }

        return conditions;
    }

    @Override
    public InsertSetMoreStep<TreasureNotificationRecord> insertQuery(
        final Configuration configuration,
        final Notification dto) {
        return super.insertQuery(configuration, dto)
                    .set(table.MESSAGE, Strings.parse(dto.getMessage()))
                    .set(table.EXECUTOR, Strings.parse(dto.getExecutor()))
                    .set(
                        table.NOTIFICATION_TYPE,
                        Strings.parse(dto.getNotificationType().name())
                    )
                    .set(
                        table.DELIVERY_DATE,
                        Strings.isEmpty(dto.getDeliveryDate())
                            ? null : Dates.parse(dto.getDeliveryDate())
                    );
    }

    @Override
    public UpdateSetMoreStep<TreasureNotificationRecord> updateQuery(
        final Configuration configuration,
        final Notification dto) {
        return super.updateQuery(configuration, dto)
                    .set(table.MESSAGE, Strings.parse(dto.getMessage()))
                    .set(table.EXECUTOR, Strings.parse(dto.getExecutor()))
                    .set(
                        table.NOTIFICATION_TYPE,
                        Strings.parse(dto.getNotificationType().name())
                    )
                    .set(
                        table.DELIVERY_DATE,
                        Strings.isEmpty(dto.getDeliveryDate())
                            ? null : Dates.parse(dto.getDeliveryDate())
                    );
    }

    @Override
    protected List<? extends OrderField<?>> orderBy() {
        return new LinkedList<>(Collections.singleton(creationDate.asc()));
    }

    @Override
    protected Notification toDTO(final Record record) {
        return toDTO(record, formatUser(record, author));
    }

    /**
     * Преобзазование записи из БД в объект.
     *
     * @param record Запись из БД.
     * @param author Пользователь.
     * @return Объект.
     */
    public static Notification toDTO(final Record record, final Option author) {
        String deletionDate =
            Dates.format(record, TREASURE_NOTIFICATION.DELETION_DATE);

        return new Notification(
            GUIDs.format(record, TREASURE_NOTIFICATION.GUID),
            Strings.format(record, TREASURE_NOTIFICATION.NAME),
            Strings.format(record, TREASURE_NOTIFICATION.MESSAGE),
            NotificationType.valueOf(
                Strings.format(record, TREASURE_NOTIFICATION.NOTIFICATION_TYPE)
            ),
            Strings.format(record, TREASURE_NOTIFICATION.EXECUTOR),
            Dates.format(record, TREASURE_NOTIFICATION.DELIVERY_DATE),
            Dates.format(record, TREASURE_NOTIFICATION.CREATION_DATE),
            author,
            deletionDate.equals("—") ? null : deletionDate
        );
    }
}
