package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.bpp.log.Log;

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
    @Log(db = true)
    void notify(Event event);

    /**
     * Отправка уведомлений по транзакции.
     *
     * @param transaction Транзакция.
     */
    @Log(db = true)
    void notify(Transaction transaction);

}
