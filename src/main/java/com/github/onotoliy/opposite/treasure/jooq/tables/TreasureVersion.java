/*
 * This file is generated by jOOQ.
 */
package com.github.onotoliy.opposite.treasure.jooq.tables;


import com.github.onotoliy.opposite.treasure.jooq.Indexes;
import com.github.onotoliy.opposite.treasure.jooq.Keys;
import com.github.onotoliy.opposite.treasure.jooq.Public;
import com.github.onotoliy.opposite.treasure.jooq.tables.records.TreasureVersionRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
 * Версия таблицы
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TreasureVersion extends TableImpl<TreasureVersionRecord> {

    private static final long serialVersionUID = -849776302;

    /**
     * The reference instance of <code>public.treasure_version</code>
     */
    public static final TreasureVersion TREASURE_VERSION = new TreasureVersion();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TreasureVersionRecord> getRecordType() {
        return TreasureVersionRecord.class;
    }

    /**
     * The column <code>public.treasure_version.name</code>. Название таблицы
     */
    public final TableField<TreasureVersionRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "Название таблицы");

    /**
     * The column <code>public.treasure_version.version</code>. Версия
     */
    public final TableField<TreasureVersionRecord, BigDecimal> VERSION = createField("version", org.jooq.impl.SQLDataType.NUMERIC.defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.NUMERIC)), this, "Версия");

    /**
     * Create a <code>public.treasure_version</code> table reference
     */
    public TreasureVersion() {
        this(DSL.name("treasure_version"), null);
    }

    /**
     * Create an aliased <code>public.treasure_version</code> table reference
     */
    public TreasureVersion(String alias) {
        this(DSL.name(alias), TREASURE_VERSION);
    }

    /**
     * Create an aliased <code>public.treasure_version</code> table reference
     */
    public TreasureVersion(Name alias) {
        this(alias, TREASURE_VERSION);
    }

    private TreasureVersion(Name alias, Table<TreasureVersionRecord> aliased) {
        this(alias, aliased, null);
    }

    private TreasureVersion(Name alias, Table<TreasureVersionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Версия таблицы"));
    }

    public <O extends Record> TreasureVersion(Table<O> child, ForeignKey<O, TreasureVersionRecord> key) {
        super(child, key, TREASURE_VERSION);
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
        return Arrays.<Index>asList(Indexes.TREASURE_VERSION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TreasureVersionRecord> getPrimaryKey() {
        return Keys.TREASURE_VERSION_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TreasureVersionRecord>> getKeys() {
        return Arrays.<UniqueKey<TreasureVersionRecord>>asList(Keys.TREASURE_VERSION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureVersion as(String alias) {
        return new TreasureVersion(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreasureVersion as(Name alias) {
        return new TreasureVersion(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureVersion rename(String name) {
        return new TreasureVersion(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TreasureVersion rename(Name name) {
        return new TreasureVersion(name, null);
    }
}