package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import com.github.onotoliy.opposite.treasure.services.IEventService;
import com.github.onotoliy.opposite.treasure.services.ITransactionService;
import com.github.onotoliy.opposite.treasure.services.NotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Сервис слушающий очередь запросов на отправку уведомлений.
 */
@Component
public class NotificationListener {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(NotificationListener.class);

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
     * @param events Сервис управления событиями.
     * @param transactions Сервис управления транзакциями.
     * @param notification Сервис уведомлений.
     */
    @Autowired
    public NotificationListener(
        final IEventService events,
        final ITransactionService transactions,
        final NotificationService notification
    ) {
        this.events = events;
        this.transactions = transactions;
        this.notification = notification;
    }

    /**
     * Получение запроса на отправку уведомления.
     *
     * @param object Запрос на отправку уведомления.
     */
    @JmsListener(destination = "inmemory.queue")
    public void listener(final NotificationObject object) {
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
