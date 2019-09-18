package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

public class StringUtil {

    public static final StringUtil STRING = new StringUtil();

    public static final String EMPTY = "—";

    public boolean nonEmpty(String value) {
        return !isEmpty(value);
    }

    public boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.equals(EMPTY);
    }

    public String parse(String value) {
        return nonEmpty(value) ? value : null;
    }

    @NotNull
    public String format(String value) {
        return nonEmpty(value) ? value : EMPTY;
    }

    public String format(Record record, Field<String> field) {
        return format(record.getValue(field, String.class));
    }
}
