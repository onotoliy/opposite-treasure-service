package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.convectors.DebtNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.DepositNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.EventNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.TransactionNotificationConvector;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.notifications.NotificationExecutor;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(NotificationService.class);

    /**
     * Сервисы описывающие бизнес логику тразакций.
     */
    private final List<NotificationExecutor> executors;

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Сервис чтения депозитов.
     */
    private final DepositService deposit;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Сервис чтения долгов.
     */
    private final DebtService debt;

    /**
     * Конструктор.
     *
     * @param executors Сервисы описывающие бизнес логику тразакций.
     * @param cashbox   Сервис чтения данных о кассе.
     * @param deposit   Сервис чтения депозитов.
     * @param users     Сервис чтения данных о пользвателях из Keycloak.
     * @param debt      Сервис чтения долгов.
     */
    @Autowired
    public NotificationService(final List<NotificationExecutor> executors,
                               final ICashboxService cashbox,
                               final DepositService deposit,
                               final KeycloakRPC users,
                               final DebtService debt) {
        this.executors = executors;
        this.cashbox = cashbox;
        this.deposit = deposit;
        this.users = users;
        this.debt = debt;
    }

    /**
     * Отправка push уведомления события.
     *
     * @param event Событие.
     */
    public void notify(final Event event) {
        Map<String, String> parameters = Map.of(
            "uuid", event.getUuid(),
            "title", event.getName(),
            "contribution", event.getContribution(),
            "deadline", event.getDeadline(),
            "type", "event"
        );

        Function<Boolean, String> toMessage = isHTML ->
            new EventNotificationConvector(isHTML)
                .toNotification(event, cashbox.get());

        notify("Событие", toMessage, parameters);
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    public void notify(final Transaction transaction) {
        LOGGER.info("Transaction notify {}", transaction);

        Map<String, String> parameters = Map.of(
            "uuid", transaction.getUuid(),
            "title", transaction.getName(),
            "cash", transaction.getCash(),
            "event", transaction.getEvent() == null
                ? "" : transaction.getEvent().getName(),
            "person", transaction.getPerson() == null
                ? "" : transaction.getPerson().getName(),
            "transaction", transaction.getType().getLabel(),
            "type", "event"
        );

        Function<Boolean, String> toMessage = isHTML ->
            new TransactionNotificationConvector(isHTML)
                .toNotification(transaction, cashbox.get());

        notify("Транзакция", toMessage, parameters);
    }

    /**
     * Отправка отчета по долгам.
     */
    public void debts() {
        final String title = "Долги на " + Dates.toShortFormat(Dates.now());
        final Cashbox cb = this.cashbox.get();
        final Timestamp now = Dates.now();

        for (User user : users.getAll()) {
            List<Event> debts = getDebts(now, user);

            if (debts.isEmpty()) {
                continue;
            }

            Function<Boolean, String> toMessage = isHTML ->
                new DebtNotificationConvector(isHTML)
                    .toNotification(user, debts, cb);

            notify(title, toMessage, Collections.emptyMap());
        }
    }

    /**
     * Отправка статистики должников.
     */
    public void statistic() {
        final Cashbox cb = this.cashbox.get();
        final Timestamp now = Dates.now();

        notify(
            "Итого долгов на " + Dates.toShortFormat(Dates.now()),
            isHTML -> new DebtNotificationConvector(isHTML)
                .toNotification(
                    users.getAll(),
                    user -> getDebts(now, user),
                    cb
                ),
            Collections.emptyMap()
        );
    }

    /**
     * Отправка отчета депозитов.
     */
    public void deposit() {
        final DepositSearchParameter parameter =
            new DepositSearchParameter(0, Integer.MAX_VALUE);
        final Set<String> members = users.getAll()
                                         .stream()
                                         .map(User::getUuid)
                                         .collect(Collectors.toSet());

        notify(
            "Переплата на " + Dates.toShortFormat(Dates.now()),
            isHTML ->
                new DepositNotificationConvector(members, isHTML)
                    .toNotification(
                        deposit.getAll(parameter).getContext(),
                        cashbox.get()
                    ),
            Collections.emptyMap());
    }

    /**
     * Получение долгов пользователя на текущей момент.
     *
     * @param now Текущей момент.
     * @param user Пользваотель.
     * @return Списко долгов пользователя.
     */
    private List<Event> getDebts(final Timestamp now, final User user) {
        return debt
            .getDebts(GUIDs.parse(user))
            .getContext()
            .stream()
            .filter(dto -> now.compareTo(Dates.parse(dto.getDeadline())) >= 0)
            .collect(Collectors.toList());
    }

    /**
     * Оправка уведомлений.
     *
     * @param title Заголовок.
     * @param message Сообщение.
     * @param parameters Дополнительные параметры
     */
    private void notify(
        final String title,
        final Function<Boolean, String> message,
        final Map<String, String> parameters
    ) {
        executors.forEach(executor -> {
            try {
                executor.notify(
                    title, message.apply(executor.isHTML()), parameters
                );
            } catch (Exception e) {
                LOGGER.error(
                    "Executor {}. Title {}. Message {}. Parameters {}.",
                    executor.getClass().getSimpleName(),
                    title,
                    message,
                    parameters
                );
                LOGGER.error(e.getMessage(), e);
            }
        });
    }
}
