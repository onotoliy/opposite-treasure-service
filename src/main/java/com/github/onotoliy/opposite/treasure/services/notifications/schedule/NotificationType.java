package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

/**
 * Тип уведомления.
 */
public enum NotificationType {

    /**
     * По событию.
     */
    EVENT,

    /**
     * По транзакции.
     */
    TRANSACTION,

    /**
     * По переплате.
     */
    DEPOSITS,

    /**
     * По долгам.
     */
    DEBTS,

    /**
     * Уведомление по статистики долгов.
     */
    STATISTIC_DEBTS

}
