package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.convectors.EventNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.TransactionNotificationConvector;
import com.github.onotoliy.opposite.treasure.services.notifications.NotificationExecutor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationService {

    /**
     * Сервисы описывающие бизнес логику тразакций.
     */
    private final List<NotificationExecutor> executors;

    /**
     * Сервис чтения данных о кассе.
     */
    private final CashboxService cashbox;

    /**
     * Конструктор.
     *
     * @param executors Сервисы описывающие бизнес логику тразакций.
     * @param cashbox Сервис чтения данных о кассе.
     */
    @Autowired
    public NotificationService(final List<NotificationExecutor> executors,
                               final CashboxService cashbox) {
        this.executors = executors;
        this.cashbox = cashbox;
    }

    /**
     * Отправка push уведомления события.
     *
     * @param event Событие.
     */
    public void notify(final Event event) {
        executors.forEach(executor -> {
            String message = new EventNotificationConvector(executor.isHTML())
                .toNotification(event, cashbox.get());

            executor.notify("Событие", message);
        });
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    public void notify(final Transaction transaction) {
        executors.forEach(executor -> {
            String message =
                new TransactionNotificationConvector(executor.isHTML())
                    .toNotification(transaction, cashbox.get());

            executor.notify("Тразакция", message);
        });
    }

}
