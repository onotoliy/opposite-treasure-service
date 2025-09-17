package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import com.github.onotoliy.opposite.treasure.exceptions.NotUniqueException;
import java.math.BigDecimal;
import java.util.UUID;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEPOSIT;

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
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     */
    @Autowired
    public DepositRepository(final DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Получение суммы денежных средств на депозите.
     *
     * @param uuid Уникальный идентификатор.
     * @return Сумма денежных средств на депозите.
     */
    public BigDecimal money(final UUID uuid) {
        return dsl.select()
                  .from(TREASURE_DEPOSIT)
                  .where(TREASURE_DEPOSIT.USER_UUID.eq(uuid))
                  .fetchOptional(record ->
                      record.getValue(TREASURE_DEPOSIT.DEPOSIT,
                                      BigDecimal.class))
                  .orElseThrow(() ->
                      new NotFoundException(TREASURE_DEPOSIT, uuid));
    }

    /**
     * Уменьшение депозита пользователя на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param guid Уникальный идентификатор.
     * @param money Денежные средства.
     */
    public void cost(final Configuration configuration,
                     final UUID guid,
                     final BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.sub(money));
    }

    /**
     * Увеличение депозита пользователя на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param guid Уникальный идентификатор.
     * @param money Денежные средства.
     */
    public void contribution(final Configuration configuration,
                             final UUID guid,
                             final BigDecimal money) {
        setDeposit(configuration, guid, TREASURE_DEPOSIT.DEPOSIT.add(money));
    }

    /**
     * Создание нового депозита.
     *
     * @param guid Уникальный идентификатор.
     */
    public void newDeposit(final UUID guid) {
        dsl.insertInto(TREASURE_DEPOSIT)
           .set(TREASURE_DEPOSIT.USER_UUID, guid)
           .set(TREASURE_DEPOSIT.DEPOSIT, BigDecimal.ZERO)
           .execute();
    }

    /**
     * Произведение операции с депозитом кассы.
     *
     * @param configuration Настройки транзакции.
     * @param guid Пользователь.
     * @param deposit Операция над депозитом.
     */
    private void setDeposit(final Configuration configuration,
                            final UUID guid,
                            final Field<BigDecimal> deposit) {
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
}
