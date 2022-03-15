package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.dto.Notification;
import com.github.onotoliy.opposite.treasure.dto.NotificationSearchParameter;
import com.github.onotoliy.opposite.treasure.dto.NotificationType;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;
import org.jooq.Configuration;

/**
 * Сервис управления уведомлениями.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface INotificationService
extends ModifierService<Notification, NotificationSearchParameter> {

    /**
     * Отправка всех неотправленных уведомлений.
     */
    void publish();

    /**
     * Отправка push уведомления события.
     *
     * @param configuration Настройки транзакции.
     * @param event Событие.
     */
    void notify(Configuration configuration,
                Event event);

    /**
     * Отправка push уведомления транзакции.
     *
     * @param configuration Настройки транзакции.
     * @param transaction Транзакция.
     */
    void notify(Configuration configuration,
                Transaction transaction);

    /**
     * Отправка отчета по долгам.
     */
    void debts();

    /**
     * Отправка статистики должников.
     */
    void statistic();

    /**
     * Отправка отчета депозитов.
     */
    void deposit();

    /**
     * Сброс уведомлений по указанному типу которые еще не доставленны.
     *
     * @param type Тип уведомления.
     */
    void discharge(NotificationType type);
}
