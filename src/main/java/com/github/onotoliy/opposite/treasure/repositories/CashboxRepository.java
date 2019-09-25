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

/**
 * Репозиторий управления данными о кассе.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class CashboxRepository {

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
    public CashboxRepository(final DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Получение данных о кассе.
     *
     * @return Данные о кассе.
     */
    public Cashbox find() {
        return dsl.select().from(TREASURE_CASHBOX).fetchAny(this::toDTO);
    }

    /**
     * Уменьшение депозита кассы на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param money Денежные средства.
     */
    public void cost(final Configuration configuration, final BigDecimal money) {
        setDeposit(configuration, TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).sub(money));
    }

    /**
     * Увеличение депозита кассы на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param money Денежные средства.
     */
    public void contribution(final Configuration configuration, final BigDecimal money) {
        setDeposit(configuration, TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).add(money));
    }

    /**
     * Произведение операции с депозитом кассы.
     *
     * @param configuration Настройки транзакции.
     * @param deposit Операция над депозитом.
     */
    private void setDeposit(final Configuration configuration, final Field<BigDecimal> deposit) {
        DSL.using(configuration)
           .update(TREASURE_CASHBOX)
           .set(TREASURE_CASHBOX.LAST_UPDATE_DATE, TIMESTAMP.now())
           .set(TREASURE_CASHBOX.DEPOSIT, deposit)
           .execute();
    }

    /**
     * Преобзазование записи из БД в объект.
     *
     * @param record Запись из БД.
     * @return Объект.
     */
    private Cashbox toDTO(final Record record) {
        return new Cashbox(
            MONEY.format(record, TREASURE_CASHBOX.DEPOSIT),
            TIMESTAMP.format(record, TREASURE_CASHBOX.LAST_UPDATE_DATE));
    }
}
