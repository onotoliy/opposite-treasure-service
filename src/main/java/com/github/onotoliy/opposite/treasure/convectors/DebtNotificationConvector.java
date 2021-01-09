package com.github.onotoliy.opposite.treasure.convectors;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.data.Debt;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.Numbers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
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
        append("Пользователь", user.getName());
        append("Долги", events
            .stream()
            .map(this::toNotification)
            .collect(Collectors.joining(", ")));
        append("Итого", events
            .stream()
            .map(Event::getContribution)
            .map(Numbers::parse)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO)
            .toString());
        append("В кассе", cashbox.getDeposit());

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
        if (event.getName().toLowerCase().startsWith("взносы")) {
            return String.format(
                "%s (%s)",
                event.getContribution(),
                DATE_FORMAT.format(Dates.parse(event.getDeadline()))
            );
        } else {
            return String
                .format("%s (%s)", event.getContribution(), event.getName());
        }
    }
}
