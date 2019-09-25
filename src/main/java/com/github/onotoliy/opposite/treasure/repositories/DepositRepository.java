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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEPOSIT;
import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;

/**
 * Репозиторий управления депозитами.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class DepositRepository {

    /**
     * Контекст подключения к БД.
     */
    private final DSLContext dsl;

    /**
     * Сервис чтения пользователей.
     */
    private final UserRPC user;

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     * @param user Сервис чтения пользователей.
     */
    @Autowired
    public DepositRepository(final DSLContext dsl, final UserRPC user) {
        this.dsl = dsl;
        this.user = user;
    }

    /**
     * Получение депозита.
     *
     * @param uuid Уникальный идентификатор.
     * @return Депозит.
     */
    public Deposit get(final UUID uuid) {
        return dsl.select().from(TREASURE_DEPOSIT)
                  .where(TREASURE_DEPOSIT.USER_UUID.eq(uuid))
                  .fetchOptional(this::toDTO)
                  .orElseThrow(() -> new NotFoundException(TREASURE_DEPOSIT, uuid));
    }

    /**
     * Поиск депозитов.
     *
     * @param parameter Поисковые параметры.
     * @return Депозиты.
     */
    public Page<Deposit> getAll(final DepositSearchParameter parameter) {
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

    public void cost(final Configuration configuration, final UUID guid, final BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.sub(money));
    }

    public void contribution(final Configuration configuration, final UUID guid, final BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.add(money));
    }

    private void setDeposit(final Configuration configuration, final UUID guid, final Field<BigDecimal> deposit) {
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

    private Deposit toDTO(final Record record) {
        return new Deposit(
            Optional.of(record.getValue(TREASURE_DEPOSIT.USER_UUID, UUID.class))
                    .flatMap(user::findOption)
                    .orElse(null),
            MONEY.format(record, TREASURE_DEPOSIT.DEPOSIT));
    }
}
