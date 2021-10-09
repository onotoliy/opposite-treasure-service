/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables.records;


import com.github.onotoliy.opposite.treasure.jooq.tables.TreasureException;

import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * Описание ошибки устройства
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TreasureExceptionRecord extends UpdatableRecordImpl<TreasureExceptionRecord> implements Record6<UUID, String, String, String, String, String> {

    private static final long serialVersionUID = -1466372068;

    /**
     * Setter for <code>public.treasure_exception.guid</code>. Уникальный идентификатор
     */
    public void setGuid(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.treasure_exception.guid</code>. Уникальный идентификатор
     */
    public UUID getGuid() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.treasure_exception.device</code>. Устройство
     */
    public void setDevice(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.treasure_exception.device</code>. Устройство
     */
    public String getDevice() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.treasure_exception.agent</code>. Агент
     */
    public void setAgent(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.treasure_exception.agent</code>. Агент
     */
    public String getAgent() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.treasure_exception.message</code>. Сообщение об ошибки
     */
    public void setMessage(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.treasure_exception.message</code>. Сообщение об ошибки
     */
    public String getMessage() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.treasure_exception.localized_message</code>. Локализированое сообщение об ошибки
     */
    public void setLocalizedMessage(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.treasure_exception.localized_message</code>. Локализированое сообщение об ошибки
     */
    public String getLocalizedMessage() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.treasure_exception.stack_trace</code>. StackTrace
     */
    public void setStackTrace(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.treasure_exception.stack_trace</code>. StackTrace
     */
    public String getStackTrace() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<UUID, String, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<UUID, String, String, String, String, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return TreasureException.TREASURE_EXCEPTION.GUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return TreasureException.TREASURE_EXCEPTION.DEVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return TreasureException.TREASURE_EXCEPTION.AGENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return TreasureException.TREASURE_EXCEPTION.MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return TreasureException.TREASURE_EXCEPTION.LOCALIZED_MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return TreasureException.TREASURE_EXCEPTION.STACK_TRACE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID component1() {
        return getGuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getDevice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getAgent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getLocalizedMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getGuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getDevice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getAgent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getLocalizedMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value1(UUID value) {
        setGuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value2(String value) {
        setDevice(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value3(String value) {
        setAgent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value4(String value) {
        setMessage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value5(String value) {
        setLocalizedMessage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord value6(String value) {
        setStackTrace(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureExceptionRecord values(UUID value1, String value2, String value3, String value4, String value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TreasureExceptionRecord
     */
    public TreasureExceptionRecord() {
        super(TreasureException.TREASURE_EXCEPTION);
    }

    /**
     * Create a detached, initialised TreasureExceptionRecord
     */
    public TreasureExceptionRecord(UUID guid, String device, String agent, String message, String localizedMessage, String stackTrace) {
        super(TreasureException.TREASURE_EXCEPTION);

        set(0, guid);
        set(1, device);
        set(2, agent);
        set(3, message);
        set(4, localizedMessage);
        set(5, stackTrace);
    }
}