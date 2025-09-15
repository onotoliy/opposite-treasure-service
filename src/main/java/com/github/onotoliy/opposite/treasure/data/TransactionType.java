package com.github.onotoliy.opposite.treasure.data;

/**
 * Тип транзакции.
 *
 * @author Anatoliy Pokhresnyi
 */
public enum TransactionType {

    /**
     * Не выбрано.
     */
    NONE,
    /**
     * Расход.
     */
    COST,
    /**
     * Взнос.
     */
    CONTRIBUTION,
    /**
     * Списание с депозита.
     */
    WRITE_OFF,
    /**
     * Платеж.
     */
    PAID,
    /**
     * Заработано.
     */
    EARNED

}
