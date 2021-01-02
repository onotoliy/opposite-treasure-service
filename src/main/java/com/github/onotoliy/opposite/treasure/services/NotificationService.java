package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.convectors.EventNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.TransactionNotificationConvector;
import com.github.onotoliy.opposite.treasure.services.notifications.NotificationExecutor;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Сервис уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(NotificationService.class);

    /**
     * Сервисы описывающие бизнес логику тразакций.
     */
    private final List<NotificationExecutor> executors;

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Конструктор.
     *
     * @param executors Сервисы описывающие бизнес логику тразакций.
     * @param cashbox Сервис чтения данных о кассе.
     */
    @Autowired
    public NotificationService(final List<NotificationExecutor> executors,
                               final ICashboxService cashbox) {
        this.executors = executors;
        this.cashbox = cashbox;
    }

    /**
     * Отправка push уведомления события.
     *
     * @param event Событие.
     */
    @Async
    public void notify(final Event event) {
        Map<String, String> parameters = Map.of(
            "uuid", event.getUuid(),
            "title", event.getName(),
            "contribution", event.getContribution(),
            "deadline", event.getDeadline(),
            "type", "event"
        );

        executors.forEach(executor -> {
            String message = new EventNotificationConvector(executor.isHTML())
                .toNotification(event, cashbox.get());

            try {
                executor.notify("Событие", message, parameters);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    @Async
    public void notify(final Transaction transaction) {
        LOGGER.info("Transaction notify {}", transaction);

        Map<String, String> parameters = Map.of(
            "uuid", transaction.getUuid(),
            "title", transaction.getName(),
            "cash", transaction.getCash(),
            "event", transaction.getEvent() == null
                ? "" : transaction.getEvent().getName(),
            "person", transaction.getPerson() == null
                ? "" : transaction.getPerson().getName(),
            "transaction", transaction.getType().getLabel(),
            "type", "event"
        );

        executors.forEach(executor -> {
            String message =
                new TransactionNotificationConvector(executor.isHTML())
                    .toNotification(transaction, cashbox.get());

            try {
                executor.notify("Транзакция", message, parameters);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

}
