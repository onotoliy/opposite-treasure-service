package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Класс описывающий логику преобразования депозитов в текстовое уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public class DepositNotificationConvector
extends AbstractNotificationConvector<List<Deposit>> {

    /**
     * Тысяча.
     */
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    /**
     * Действующие члены клуба.
     */
    private final Set<String> members;

    /**
     * Конструктор.
     *
     * @param members Действующие члены клуба.
     * @param html Использовать HTML верстку.
     */
    public DepositNotificationConvector(final Set<String> members,
                                        final boolean html) {
        super(html);

        this.members = members;
    }

    @Override
    protected void append(final List<Deposit> dto) {
        if (html) {
            message.append("<i>");
        }

        message.append("Переплата есть у следующих людей");

        if (html) {
            message.append("</i>");
        }

        newLine();

        for (Deposit deposit: dto) {
            if (!members.contains(deposit.getUuid())) {
                continue;
            }

            if (Numbers.isEmpty(deposit.getDeposit())) {
                continue;
            }

            BigDecimal money = Numbers.parse(deposit.getDeposit());

            if (Objects.isNull(money)) {
                continue;
            }

            if (THOUSAND.compareTo(money) > 0) {
                append(deposit.getName(), deposit.getDeposit());
            }
        }

        message.append(
            "Эти деньги на взносы не раскидаешь, поэтому просто записаны.");

        newLine();

        message.append(
            "Можете докинуть до 1 000 руб. я месяц взносов закрою.");

        newLine();
    }

}
