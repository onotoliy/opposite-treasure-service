package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import java.util.Deque;
import java.util.UUID;

import com.github.onotoliy.opposite.treasure.services.IEventService;
import com.github.onotoliy.opposite.treasure.services.ITransactionService;
import com.github.onotoliy.opposite.treasure.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Сервис отправляющий в очередь на запросы отправку уведомлений.
 */
@Component
public class NotificationPublisher {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(NotificationPublisher.class);

    /**
     * Очередь.
     */
    private final Deque<NotificationObject> deque;

    /**
     * Сервис управления событиями.
     */
    private final IEventService events;

    /**
     * Сервис управления транзакциями.
     */
    private final ITransactionService transactions;

    /**
     * Сервис уведомлений.
     */
    private final NotificationService notification;

    /**
     * Конструктор.
     *
     * @param deque Очередь.
     * @param notification Сервис уведомлений.
     * @param events Сервис управления событиями.
     * @param transactions Сервис управления транзакциями.
     */
    @Autowired
    public NotificationPublisher(
        final Deque<NotificationObject> deque,
        final IEventService events,
        final ITransactionService transactions,
        final NotificationService notification
    ) {
        this.deque = deque;
        this.events = events;
        this.transactions = transactions;
        this.notification = notification;
    }

    /**
     * Отправить запрос отправку уведомлений.
     *
     * @param type Тип.
     */
    public void publish(final NotificationType type) {
        publish(type, null);
    }

    /**
     * Отправить запрос отправку уведомлений.
     *
     * @param type Тип.
     * @param uuid Уникальный идентификатор объекта.
     */
    public void publish(final NotificationType type, final UUID uuid) {
        LOGGER.info("Sending message. Type {}, Object UUID {}", type, uuid);

        deque.addLast(new NotificationObject(type, uuid));
    }

    /**
     * Чтение каждую минуту данных из очереди.
     */
    @Scheduled(cron = "0 */1 * * * *")
    public final void listener() {
        LOGGER.info(
            "Listening queue. Messages awaiting sending {}",
            deque.size()
        );

        if (deque.isEmpty()) {
            LOGGER.info("Deque is empty");

            return;
        }

        try {
            listener(deque.pop());
        } catch (Exception exception) {
            LOGGER.error("Exception to process message {}", deque);
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    /**
     * Получение запроса на отправку уведомления.
     *
     * @param object Запрос на отправку уведомления.
     */
    private void listener(final NotificationObject object) {
        LOGGER.info(
                "Reading message. Type {}, Object UUID {}",
                object.getType(),
                object.getObject()
        );

        switch (object.getType()) {
            case EVENT:
                notification.notify(events.get(object.getObject()));
                break;
            case TRANSACTION:
                notification.notify(transactions.get(object.getObject()));
                break;
            case DEPOSITS:
                notification.deposit();
                break;
            case DEBTS:
                notification.debts();
                break;
            case STATISTIC_DEBTS:
                notification.statistic();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
