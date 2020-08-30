package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;

/**
 * Описание бизнес логики отправки уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface NotificationExecutor {

    /**
     * Отправка уведомлений по событию.
     *
     * @param event Событие.
     */
    void notify(Event event);

    /**
     * Отправка уведомлений по транзакции.
     *
     * @param transaction Транзакция.
     */
    void notify(Transaction transaction);

}
