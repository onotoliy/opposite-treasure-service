package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
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
     * Конструктор.
     *
     * @param executors Сервисы описывающие бизнес логику тразакций.
     */
    @Autowired
    public NotificationService(final List<NotificationExecutor> executors) {
        this.executors = executors;
    }

    /**
     * Отправка push уведомления события.
     *
     * @param event Событие.
     */
    public void notify(final Event event) {
        executors.forEach(executor -> executor.notify(event));
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    public void notify(final Transaction transaction) {
        executors.forEach(executor -> executor.notify(transaction));
    }
}
