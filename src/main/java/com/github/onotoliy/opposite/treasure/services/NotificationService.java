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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Async
    public void notify(final Event event) {
        Map<String, String> parameters = Map.of(
            "uuid", event.getUuid(),
            "title", event.getName(),
            "contribution", event.getContribution(),
            "deadline", event.getDeadline(),
            "type", "event"
        );

        executors.forEach(executor -> {
            String message = new EventNotificationConvector(executor.isHTML())
                .toNotification(event, cashbox.get());

            executor.notify("Событие", message, parameters);
        });
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    @Async
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

        executors.forEach(executor -> {
            String message =
                new TransactionNotificationConvector(executor.isHTML())
                    .toNotification(transaction, cashbox.get());

            executor.notify("Транзакция", message, parameters);
        });
    }

    /**
     * Отправка push уведомления по долгам.
     */
    @Async
    public void debts() {
        String title = "Долги на " + Dates.format(Dates.format(Dates.now()));
        Map<String, String> parameters = new HashMap<>();
        Cashbox cb = this.cashbox.get();
        long now = Dates.now().getTime();

        executors.forEach(
            executor -> {
                DebtNotificationConvector convector =
                    new DebtNotificationConvector(executor.isHTML());

                for (User user : users.getAll()) {
                    List<Event> debts = debt
                        .getDebts(GUIDs.parse(user))
                        .getContext()
                        .stream()
                        .filter(event ->
                            now >= Dates.parse(event.getDeadline())
                                        .getTime())
                        .collect(Collectors.toList());

                    if (debts.isEmpty()) {
                        continue;
                    }

                    try {
                        String message = convector
                            .toNotification(user, debts, cb);

                        executor.notify(title, message, parameters);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        );
    }

    /**
     * Отправка push уведомления по долгам.
     */
    @Async
    public void deposit() {
        DepositSearchParameter parameter =
            new DepositSearchParameter(0, Integer.MAX_VALUE);
        String title =
            "Переплата на " + Dates.format(Dates.format(Dates.now()));
        Set<String> members = users.getAll()
                                   .stream()
                                   .map(User::getUuid)
                                   .collect(Collectors.toSet());

        executors.forEach(
            executor -> {
                try {
                    DepositNotificationConvector convector =
                        new DepositNotificationConvector(members,
                                                         executor.isHTML());

                    String message = convector.toNotification(
                        deposit.getAll(parameter).getContext(),
                        cashbox.get()
                    );

                    executor.notify(title, message, new HashMap<>());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        );
    }
}
