package com.github.onotoliy.opposite.treasure.dto;

/**
 * Типы уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
public enum NotificationType {
    /**
     * Событие.
     */
    EVENT,

    /**
     * Транзакция.
     */
    TRANSACTION,

    /**
     * Отчет о статистики долгов.
     */
    STATISTIC,

    /**
     * Отчет о долгах.
     */
    DEBTS,

    /**
     * Отчет о депозитах.
     */
    DEPOSITS
}
