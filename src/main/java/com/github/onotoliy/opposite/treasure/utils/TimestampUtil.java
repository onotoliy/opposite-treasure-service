package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;

public class TimestampUtil {

    public static final TimestampUtil TIMESTAMP = new TimestampUtil();

    public Timestamp now() {
        return toTimestamp(Instant.now());
    }

    public Timestamp parse(final String value) {
        if (STRING.isEmpty(value)) {
            return null;
        }

        String v = value.endsWith("Z")
            ? value.substring(0, value.length() - 1)
            : value;

        try {
            if (value.endsWith("Z")) {
                return toTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(v));
            } else {
                return toTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(v));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @NotNull
    public String format(final Timestamp value) {
        return value == null
            ? "—"
            : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(value);
    }

    private Timestamp toTimestamp(final Instant value) {
        return value == null ? null : new Timestamp(value.toEpochMilli());
    }

    private Timestamp toTimestamp(final Date value) {
        return value == null ? null : toTimestamp(value.toInstant());
    }

    public String format(final Record record, final Field<Timestamp> field) {
        return format(record.getValue(field, Timestamp.class));
    }
}
