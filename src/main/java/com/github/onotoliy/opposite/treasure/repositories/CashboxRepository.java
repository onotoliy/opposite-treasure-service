package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_CASHBOX;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_DEPOSIT;
import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_VERSION;

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
     * Получение суммы денежных средств в кассе.
     *
     * @return Сумма денежных средств в кассе.
     */
    public BigDecimal money() {
        return dsl.select()
                  .from(TREASURE_CASHBOX)
                  .fetchAny(record -> record.getValue(TREASURE_CASHBOX.DEPOSIT,
                                                      BigDecimal.class));
    }

    /**
     * Уменьшение депозита кассы на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param money Денежные средства.
     */
    public void cost(final Configuration configuration,
                     final BigDecimal money) {
        setDeposit(configuration,
                   TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).sub(money));
    }

    /**
     * Увеличение депозита кассы на указанную сумму.
     *
     * @param configuration Настройки транзакции.
     * @param money Денежные средства.
     */
    public void contribution(final Configuration configuration,
                             final BigDecimal money) {
        setDeposit(configuration,
                   TREASURE_CASHBOX.DEPOSIT.cast(BigDecimal.class).add(money));
    }

    /**
     * Произведение операции с депозитом кассы.
     *
     * @param configuration Настройки транзакции.
     * @param deposit Операция над депозитом.
     */
    private void setDeposit(final Configuration configuration,
                            final Field<BigDecimal> deposit) {
        setVersion(configuration);

        DSL.using(configuration)
           .update(TREASURE_CASHBOX)
           .set(TREASURE_CASHBOX.LAST_UPDATE_DATE, Dates.now())
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
            Numbers.format(record, TREASURE_CASHBOX.DEPOSIT),
            Dates.format(record, TREASURE_CASHBOX.LAST_UPDATE_DATE));
    }

    /**
     * Изменение версии справочника.
     *
     * @param configuration Настройка транзакции.
     */
    private void setVersion(
        final Configuration configuration
    ) {
        BigDecimal version = BigDecimal.valueOf(Dates.now().getTime());

        DSL.using(configuration)
           .update(TREASURE_VERSION)
           .set(TREASURE_VERSION.VERSION, version)
           .where(TREASURE_VERSION.NAME.eq(TREASURE_DEPOSIT.getName()));
    }
}
