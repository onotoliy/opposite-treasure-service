package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.treasure.data.Cashbox;
import com.github.onotoliy.opposite.treasure.data.Debt;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.User;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс описывающий логику преобразования долгов в текстовое уведомление.
 *
 * @author Anatoliy Pokhresnyi
 */
public class DebtNotificationConvector
extends AbstractNotificationConvector<Debt> {

    /**
     * Формат даты.
     */
    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("MM.yy");

    /**
     * Конструктор.
     *
     * @param html Использовать HTML верстку.
     */
    public DebtNotificationConvector(final boolean html) {
        super(html);
    }

    @Override
    public String toNotification(final Debt dto, final Cashbox cashbox) {
        throw new UnsupportedOperationException();
    }

    /**
     * Преобразование объекта в текстовое уведомление.
     *
     * @param user Пользователь.
     * @param events Долги.
     * @param cashbox Данные о кассе.
     * @return Текстовое уведомление.
     */
    public String toNotification(final User user,
                                 final List<Event> events,
                                 final Cashbox cashbox) {
        message.setLength(0);

        append("Член клуба", user.name());
        append("Долги", events
            .stream()
            .map(this::toNotification)
            .collect(Collectors.joining(", ")));
        append("Итого", Numbers.format(toTotal(events)));
        append("В кассе", cashbox.deposit());

        return message.toString();
    }


    @Override
    protected void append(final Debt dto) {
        throw new UnsupportedOperationException();
    }

    /**
     * Преобразование объекта в текстовое уведомление.
     *
     * @param event Событие.
     * @return Текстовое уведомление.
     */
    private String toNotification(final Event event) {
        if (event.name().toLowerCase().startsWith("взносы")) {
            return String.format(
                "%s (%s)",
                event.contribution(),
                DATE_FORMAT.format(event.deadline())
            );
        } else {
            return String
                .format("%s (%s)", event.contribution(), event.name());
        }
    }

    /**
     * Преобразование объекта в текстовое уведомление.
     *
     * @param users Пользователи.
     * @param toEvents Функция получения событий.
     * @param cashbox Касса.
     * @return Текстовое уведомление.
     */
    public String toNotification(final List<User> users,
                                 final Function<User, List<Event>> toEvents,
                                 final Cashbox cashbox) {
        message.setLength(0);

        newLine();

        int numberOfDebtors = 0;
        BigDecimal totalDebts = BigDecimal.ZERO;

        for (User user : users) {
            BigDecimal debts = toTotal(toEvents.apply(user));

            if (debts.equals(BigDecimal.ZERO)) {
                continue;
            }

            numberOfDebtors++;
            totalDebts = totalDebts.add(debts);

            append(user.name(), Numbers.format(debts));
        }

        newLine();

        append("Всего должников", String.valueOf(numberOfDebtors));
        append("Всего долгов на сумму", Numbers.format(totalDebts));

        newLine();

        append("В кассе", cashbox.deposit());

        return message.toString();
    }

    /**
     * Сумма долгов на данный момент.
     *
     * @param events События.
     * @return Сумма долгов.
     */
    private BigDecimal toTotal(final List<Event> events) {
        return events
            .stream()
            .map(Event::contribution)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
    }
}
