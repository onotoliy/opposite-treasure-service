package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Cashbox;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;
import static com.github.onotoliy.opposite.treasure.utils.TimestampUtil.TIMESTAMP;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_CASHBOX;

@Repository
public class CashboxRepository {

    private final DSLContext dsl;

    @Autowired
    public CashboxRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Cashbox find() {
        return dsl.select().from(TREASURE_CASHBOX).fetchAny(this::toDTO);
    }

    public void cost(Configuration configuration, BigDecimal money) {
        setDeposit(configuration, TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).sub(money));
    }

    public void contribution(Configuration configuration, BigDecimal money) {
        setDeposit(configuration, TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).add(money));
    }

    private void setDeposit(Configuration configuration, Field<BigDecimal> deposit) {
        DSL.using(configuration)
           .update(TREASURE_CASHBOX)
           .set(TREASURE_CASHBOX.LAST_UPDATE_DATE, TIMESTAMP.now())
           .set(TREASURE_CASHBOX.DEPOSIT, deposit)
           .execute();
    }

    private Cashbox toDTO(Record record) {
        return new Cashbox(
            MONEY.format(record, TREASURE_CASHBOX.DEPOSIT),
            TIMESTAMP.format(record, TREASURE_CASHBOX.LAST_UPDATE_DATE));
    }
}
