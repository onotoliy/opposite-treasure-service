/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables;


import com.github.onotoliy.opposite.treasure.jooq.Public;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureCashboxRecord;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * Касса
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TreasureCashbox extends TableImpl<TreasureCashboxRecord> {

    private static final long serialVersionUID = -1461158132;

    /**
     * The reference instance of <code>public.treasure_cashbox</code>
     */
    public static final TreasureCashbox TREASURE_CASHBOX = new TreasureCashbox();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TreasureCashboxRecord> getRecordType() {
        return TreasureCashboxRecord.class;
    }

    /**
     * The column <code>public.treasure_cashbox.last_update_date</code>. Дата последненго изменения
     */
    public final TableField<TreasureCashboxRecord, Timestamp> LAST_UPDATE_DATE = createField("last_update_date", org.jooq.impl.SQLDataType.TIMESTAMP, this, "Дата последненго изменения");

    /**
     * The column <code>public.treasure_cashbox.deposit</code>. Сумма в кассе
     */
    public final TableField<TreasureCashboxRecord, BigDecimal> DEPOSIT = createField("deposit", org.jooq.impl.SQLDataType.NUMERIC, this, "Сумма в кассе");

    /**
     * Create a <code>public.treasure_cashbox</code> table reference
     */
    public TreasureCashbox() {
        this(DSL.name("treasure_cashbox"), null);
    }

    /**
     * Create an aliased <code>public.treasure_cashbox</code> table reference
     */
    public TreasureCashbox(String alias) {
        this(DSL.name(alias), TREASURE_CASHBOX);
    }

    /**
     * Create an aliased <code>public.treasure_cashbox</code> table reference
     */
    public TreasureCashbox(Name alias) {
        this(alias, TREASURE_CASHBOX);
    }

    private TreasureCashbox(Name alias, Table<TreasureCashboxRecord> aliased) {
        this(alias, aliased, null);
    }

    private TreasureCashbox(Name alias, Table<TreasureCashboxRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Касса"));
    }

    public <O extends Record> TreasureCashbox(Table<O> child, ForeignKey<O, TreasureCashboxRecord> key) {
        super(child, key, TREASURE_CASHBOX);
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
    public TreasureCashbox as(String alias) {
        return new TreasureCashbox(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureCashbox as(Name alias) {
        return new TreasureCashbox(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureCashbox rename(String name) {
        return new TreasureCashbox(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureCashbox rename(Name name) {
        return new TreasureCashbox(name, null);
    }
}
