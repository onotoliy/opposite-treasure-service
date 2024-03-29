/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq;


import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureCashbox;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDebt;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDeposit;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureException;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureLog;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureNotification;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureVersion;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * Касса
     */
    public static final TreasureCashbox TREASURE_CASHBOX = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureCashbox.TREASURE_CASHBOX;

    /**
     * Долги
     */
    public static final TreasureDebt TREASURE_DEBT = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDebt.TREASURE_DEBT;

    /**
     * Депозиты
     */
    public static final TreasureDeposit TREASURE_DEPOSIT = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDeposit.TREASURE_DEPOSIT;

    /**
     * События
     */
    public static final TreasureEvent TREASURE_EVENT = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent.TREASURE_EVENT;

    /**
     * Описание ошибки устройства
     */
    public static final TreasureException TREASURE_EXCEPTION = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureException.TREASURE_EXCEPTION;

    /**
     * Логи
     */
    public static final TreasureLog TREASURE_LOG = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureLog.TREASURE_LOG;

    /**
     * Транзакции
     */
    public static final TreasureNotification TREASURE_NOTIFICATION = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureNotification.TREASURE_NOTIFICATION;

    /**
     * Транзакции
     */
    public static final TreasureTransaction TREASURE_TRANSACTION = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction.TREASURE_TRANSACTION;

    /**
     * Версия таблицы
     */
    public static final TreasureVersion TREASURE_VERSION = com.github.onotoliy.opposite.treasure.jooq.tables.TreasureVersion.TREASURE_VERSION;
}
