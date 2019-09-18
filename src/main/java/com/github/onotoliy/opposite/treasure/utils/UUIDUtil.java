package com.github.onotoliy.opposite.treasure.utils;

import com.github.onotoliy.opposite.data.core.HasUUID;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.ObjectUtil.OBJECT;
import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;

public class UUIDUtil {

    public static final UUIDUtil GUID = new UUIDUtil();

    public String format(Record record, Field<UUID> field) {
        return format(record.getValue(field, UUID.class));
    }

    public String format(UUID uuid) {
        return uuid.toString();
    }

    public UUID parse(HasUUID value) {
        return value == null ? null : parse(value.getUuid());
    }

    @NotNull
    public UUID parse(@NotNull String value) {
        return UUID.fromString(value);
    }

    public boolean isEmpty(UUID x) {
        return x == null;
    }

    public boolean nonEmpty(UUID x) {
        return !isEmpty(x);
    }

    public boolean isEmpty(HasUUID x) {
        return x == null || STRING.isEmpty(x.getUuid());
    }

    public boolean nonEmpty(HasUUID x) {
        return !isEmpty(x);
    }

    public boolean notEqually(HasUUID x, HasUUID y) {
        return !isEqually(x, y);
    }

    public boolean isEqually(HasUUID x, HasUUID y) {
        return OBJECT.isEqually(x, y, (o1, o2) -> o1.getUuid().equals(o2.getUuid()));
    }


}
