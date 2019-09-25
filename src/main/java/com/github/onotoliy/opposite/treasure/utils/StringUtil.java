package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

public class StringUtil {

    public static final StringUtil STRING = new StringUtil();

    private static final String EMPTY = "—";

    public boolean nonEmpty(final String value) {
        return !isEmpty(value);
    }

    public boolean isEmpty(final String value) {
        return value == null || value.trim().isEmpty() || value.equals(EMPTY);
    }

    public String parse(final String value) {
        return nonEmpty(value) ? value : null;
    }

    @NotNull
    public String format(final String value) {
        return nonEmpty(value) ? value : EMPTY;
    }

    public String format(final Record record, final Field<String> field) {
        return format(record.getValue(field, String.class));
    }
}
