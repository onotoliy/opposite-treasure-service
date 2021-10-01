/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables;


import com.github.onotoliy.opposite.treasure.jooq.Indexes;
import com.github.onotoliy.opposite.treasure.jooq.Keys;
import com.github.onotoliy.opposite.treasure.jooq.Public;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureExceptionRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class TreasureException extends TableImpl<TreasureExceptionRecord> {

    private static final long serialVersionUID = -533498748;

    /**
     * The reference instance of <code>public.treasure_exception</code>
     */
    public static final TreasureException TREASURE_EXCEPTION = new TreasureException();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TreasureExceptionRecord> getRecordType() {
        return TreasureExceptionRecord.class;
    }

    /**
     * The column <code>public.treasure_exception.guid</code>. Уникальный идентификатор
     */
    public final TableField<TreasureExceptionRecord, UUID> GUID = createField("guid", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "Уникальный идентификатор");

    /**
     * The column <code>public.treasure_exception.device</code>. Устройство
     */
    public final TableField<TreasureExceptionRecord, String> DEVICE = createField("device", org.jooq.impl.SQLDataType.VARCHAR, this, "Устройство");

    /**
     * The column <code>public.treasure_exception.agent</code>. Агент
     */
    public final TableField<TreasureExceptionRecord, String> AGENT = createField("agent", org.jooq.impl.SQLDataType.VARCHAR, this, "Агент");

    /**
     * The column <code>public.treasure_exception.message</code>. Сообщение об ошибки
     */
    public final TableField<TreasureExceptionRecord, String> MESSAGE = createField("message", org.jooq.impl.SQLDataType.VARCHAR, this, "Сообщение об ошибки");

    /**
     * The column <code>public.treasure_exception.localized_message</code>. Локализированое сообщение об ошибки
     */
    public final TableField<TreasureExceptionRecord, String> LOCALIZED_MESSAGE = createField("localized_message", org.jooq.impl.SQLDataType.VARCHAR, this, "Локализированое сообщение об ошибки");

    /**
     * The column <code>public.treasure_exception.stack_trace</code>. StackTrace
     */
    public final TableField<TreasureExceptionRecord, String> STACK_TRACE = createField("stack_trace", org.jooq.impl.SQLDataType.VARCHAR, this, "StackTrace");

    /**
     * Create a <code>public.treasure_exception</code> table reference
     */
    public TreasureException() {
        this(DSL.name("treasure_exception"), null);
    }

    /**
     * Create an aliased <code>public.treasure_exception</code> table reference
     */
    public TreasureException(String alias) {
        this(DSL.name(alias), TREASURE_EXCEPTION);
    }

    /**
     * Create an aliased <code>public.treasure_exception</code> table reference
     */
    public TreasureException(Name alias) {
        this(alias, TREASURE_EXCEPTION);
    }

    private TreasureException(Name alias, Table<TreasureExceptionRecord> aliased) {
        this(alias, aliased, null);
    }

    private TreasureException(Name alias, Table<TreasureExceptionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Описание ошибки устройства"));
    }

    public <O extends Record> TreasureException(Table<O> child, ForeignKey<O, TreasureExceptionRecord> key) {
        super(child, key, TREASURE_EXCEPTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TREASURE_EXCEPTION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TreasureExceptionRecord> getPrimaryKey() {
        return Keys.TREASURE_EXCEPTION_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TreasureExceptionRecord>> getKeys() {
        return Arrays.<UniqueKey<TreasureExceptionRecord>>asList(Keys.TREASURE_EXCEPTION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureException as(String alias) {
        return new TreasureException(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureException as(Name alias) {
        return new TreasureException(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureException rename(String name) {
        return new TreasureException(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureException rename(Name name) {
        return new TreasureException(name, null);
    }
}
