package com.github.onotoliy.opposite.treasure.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;

public class BigDecimalUtil {

    public static final BigDecimalUtil MONEY = new BigDecimalUtil();

    public boolean nonEmpty(BigDecimal value) {
        return !isEmpty(value);
    }

    public boolean isEmpty(BigDecimal value) {
        return value == null;
    }

    public BigDecimal parse(String value) {
        return STRING.isEmpty(value) ? null : new BigDecimal(value);
    }

    @NotNull
    public String format(BigDecimal value) {
        if (isEmpty(value)) {
            return "0.0";
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return df.format(value.setScale(2, RoundingMode.HALF_DOWN));
    }

    public String format(Record record, Field<BigDecimal> field) {
        return format(record.getValue(field, BigDecimal.class));
    }

    public boolean nonEqually(String x, String y) {
        return ObjectUtil.OBJECT.nonEqually(x, y,
            (o1, o2) -> parse(o1).equals(parse(o2)));
    }
}
