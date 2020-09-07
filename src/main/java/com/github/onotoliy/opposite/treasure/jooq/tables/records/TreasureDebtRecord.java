/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables.records;


import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureDebt;

import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


/**
 * Долги
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TreasureDebtRecord extends TableRecordImpl<TreasureDebtRecord> implements Record2<UUID, UUID> {

    private static final long serialVersionUID = 640269961;

    /**
     * Setter for <code>public.treasure_debt.user_uuid</code>. Пользователь
     */
    public void setUserUuid(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.treasure_debt.user_uuid</code>. Пользователь
     */
    public UUID getUserUuid() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.treasure_debt.event_guid</code>. Событие
     */
    public void setEventGuid(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.treasure_debt.event_guid</code>. Событие
     */
    public UUID getEventGuid() {
        return (UUID) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<UUID, UUID> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<UUID, UUID> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return TreasureDebt.TREASURE_DEBT.USER_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return TreasureDebt.TREASURE_DEBT.EVENT_GUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component1() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component2() {
        return getEventGuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value2() {
        return getEventGuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureDebtRecord value1(UUID value) {
        setUserUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureDebtRecord value2(UUID value) {
        setEventGuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureDebtRecord values(UUID value1, UUID value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TreasureDebtRecord
     */
    public TreasureDebtRecord() {
        super(TreasureDebt.TREASURE_DEBT);
    }

    /**
     * Create a detached, initialised TreasureDebtRecord
     */
    public TreasureDebtRecord(UUID userUuid, UUID eventGuid) {
        super(TreasureDebt.TREASURE_DEBT);

        set(0, userUuid);
        set(1, eventGuid);
    }
}
