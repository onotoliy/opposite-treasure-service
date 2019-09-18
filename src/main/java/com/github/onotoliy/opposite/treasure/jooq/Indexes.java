/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq;


import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDebt;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDeposit;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureEvent;
import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureTransaction;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index TREASURE_DEBT_PKEY = Indexes0.TREASURE_DEBT_PKEY;
    public static final Index TREASURE_DEPOSIT_PKEY = Indexes0.TREASURE_DEPOSIT_PKEY;
    public static final Index TREASURE_EVENT_PKEY = Indexes0.TREASURE_EVENT_PKEY;
    public static final Index TREASURE_TRANSACTION_PKEY = Indexes0.TREASURE_TRANSACTION_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index TREASURE_DEBT_PKEY = Internal.createIndex("treasure_debt_pkey", TreasureDebt.TREASURE_DEBT, new OrderField[] { TreasureDebt.TREASURE_DEBT.USER_UUID }, true);
        public static Index TREASURE_DEPOSIT_PKEY = Internal.createIndex("treasure_deposit_pkey", TreasureDeposit.TREASURE_DEPOSIT, new OrderField[] { TreasureDeposit.TREASURE_DEPOSIT.USER_UUID }, true);
        public static Index TREASURE_EVENT_PKEY = Internal.createIndex("treasure_event_pkey", TreasureEvent.TREASURE_EVENT, new OrderField[] { TreasureEvent.TREASURE_EVENT.GUID }, true);
        public static Index TREASURE_TRANSACTION_PKEY = Internal.createIndex("treasure_transaction_pkey", TreasureTransaction.TREASURE_TRANSACTION, new OrderField[] { TreasureTransaction.TREASURE_TRANSACTION.GUID }, true);
    }
}
