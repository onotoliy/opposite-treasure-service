package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.page.Meta;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.data.page.Paging;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.exceptions.NotUniqueException;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEPOSIT;
import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;

@Repository
public class DepositRepository {

    private final DSLContext dsl;

    private final UserRPC user;

    public DepositRepository(DSLContext dsl, UserRPC user) {
        this.dsl = dsl;
        this.user = user;
    }

    public Deposit get(UUID uuid) {
        return dsl.select().from(TREASURE_DEPOSIT)
                  .where(TREASURE_DEPOSIT.USER_UUID.eq(uuid))
                  .fetchOptional(this::toDTO)
                  .orElseThrow(() -> new NotFoundException(TREASURE_DEPOSIT, uuid));
    }

    public Page<Deposit> getAll(DepositSearchParameter parameter) {
        return new Page<>(
            new Meta(
                dsl.selectCount()
                   .from(TREASURE_DEPOSIT)
                   .fetchOptional(0, int.class)
                   .orElse(0),
                new Paging(parameter.offset(), parameter.offset())),
            dsl.select().from(TREASURE_DEPOSIT)
               .limit(parameter.offset(), parameter.numberOfRows())
               .fetch(this::toDTO));
    }

    public void cost(Configuration configuration, UUID guid, BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.sub(money));
    }

    public void contribution(Configuration configuration, UUID guid, BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.add(money));
    }

    private void setDeposit(Configuration configuration, UUID guid, Field<BigDecimal> deposit) {
        int count = DSL.using(configuration)
                       .update(TREASURE_DEPOSIT)
                       .set(TREASURE_DEPOSIT.DEPOSIT, deposit)
                       .where(TREASURE_DEPOSIT.USER_UUID.eq(guid))
                       .execute();

        if (count > 1) {
            throw new NotUniqueException(TREASURE_DEPOSIT, guid);
        }

        if (count == 0) {
            throw new NotFoundException(TREASURE_DEPOSIT, guid);
        }
    }

    private Deposit toDTO(Record record) {
        return new Deposit(
            Optional.of(record.getValue(TREASURE_DEPOSIT.USER_UUID, UUID.class))
                    .flatMap(user::findOption)
                    .orElse(null),
            MONEY.format(record, TREASURE_DEPOSIT.DEPOSIT));
    }
}
