/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables;


import com.github.onotoliy.opposite.treasure.jooq.Indexes;
import com.github.onotoliy.opposite.treasure.jooq.Keys;
import com.github.onotoliy.opposite.treasure.jooq.Public;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureDebtRecord;

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
public class TreasureDebt extends TableImpl<TreasureDebtRecord> {

    private static final long serialVersionUID = 875309345;

    /**
     * The reference instance of <code>public.treasure_debt</code>
     */
    public static final TreasureDebt TREASURE_DEBT = new TreasureDebt();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TreasureDebtRecord> getRecordType() {
        return TreasureDebtRecord.class;
    }

    /**
     * The column <code>public.treasure_debt.user_uuid</code>. Пользователь
     */
    public final TableField<TreasureDebtRecord, UUID> USER_UUID = createField("user_uuid", org.jooq.impl.SQLDataType.UUID, this, "Пользователь");

    /**
     * The column <code>public.treasure_debt.event_guid</code>. Событие
     */
    public final TableField<TreasureDebtRecord, UUID> EVENT_GUID = createField("event_guid", org.jooq.impl.SQLDataType.UUID, this, "Событие");

    /**
     * Create a <code>public.treasure_debt</code> table reference
     */
    public TreasureDebt() {
        this(DSL.name("treasure_debt"), null);
    }

    /**
     * Create an aliased <code>public.treasure_debt</code> table reference
     */
    public TreasureDebt(String alias) {
        this(DSL.name(alias), TREASURE_DEBT);
    }

    /**
     * Create an aliased <code>public.treasure_debt</code> table reference
     */
    public TreasureDebt(Name alias) {
        this(alias, TREASURE_DEBT);
    }

    private TreasureDebt(Name alias, Table<TreasureDebtRecord> aliased) {
        this(alias, aliased, null);
    }

    private TreasureDebt(Name alias, Table<TreasureDebtRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Долги"));
    }

    public <O extends Record> TreasureDebt(Table<O> child, ForeignKey<O, TreasureDebtRecord> key) {
        super(child, key, TREASURE_DEBT);
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
        return Arrays.<Index>asList(Indexes.TREASURE_DEBT_USER_UUID_EVENT_GUID_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TreasureDebtRecord>> getKeys() {
        return Arrays.<UniqueKey<TreasureDebtRecord>>asList(Keys.TREASURE_DEBT_USER_UUID_EVENT_GUID_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<TreasureDebtRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TreasureDebtRecord, ?>>asList(Keys.TREASURE_DEBT__FK_TT_EVENT);
    }

    public TreasureEvent treasureEvent() {
        return new TreasureEvent(this, Keys.TREASURE_DEBT__FK_TT_EVENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureDebt as(String alias) {
        return new TreasureDebt(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureDebt as(Name alias) {
        return new TreasureDebt(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureDebt rename(String name) {
        return new TreasureDebt(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureDebt rename(Name name) {
        return new TreasureDebt(name, null);
    }
}
