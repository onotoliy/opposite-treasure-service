package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.convectors.DebtNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.DepositNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.EventNotificationConvector;
import com.github.onotoliy.opposite.treasure.convectors.TransactionNotificationConvector;
import com.github.onotoliy.opposite.treasure.dto.Delivery;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.dto.Notification;
import com.github.onotoliy.opposite.treasure.dto.NotificationSearchParameter;
import com.github.onotoliy.opposite.treasure.dto.NotificationType;
import com.github.onotoliy.opposite.treasure.repositories.NotificationRepository;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.services.notifications.NotificationExecutor;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import org.jooq.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationService
extends AbstractModifierService<
    Notification,
    NotificationSearchParameter,
    NotificationRepository>
implements INotificationService {

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
     * @param repository   Репозиторий управления уведомлениями.
     * @param executors    Сервисы описывающие бизнес логику тразакций.
     * @param cashbox      Сервис чтения данных о кассе.
     * @param deposit      Сервис чтения депозитов.
     * @param users        Сервис чтения данных о пользвателях из Keycloak.
     * @param debt         Сервис чтения долгов.
     */
    @Autowired
    public NotificationService(final NotificationRepository repository,
                               final List<NotificationExecutor> executors,
                               final ICashboxService cashbox,
                               final DepositService deposit,
                               final KeycloakRPC users,
                               final DebtService debt) {
        super(repository);

        this.executors = executors;
        this.cashbox = cashbox;
        this.deposit = deposit;
        this.users = users;
        this.debt = debt;
    }


    @Override
    public void publish() {
        final NotificationSearchParameter parameter =
            new NotificationSearchParameter(
                null, Delivery.ONLY_NOT_DELIVERED, 0, 10
            );

        while (true) {
            final List<Notification> context = repository
                .getAll(parameter)
                .getContext();

            if (context.isEmpty()) {
                return;
            }

            context.forEach(this::notify);
        }
    }

    @Override
    public void notify(final Configuration configuration, final Event event) {
        notify(
            configuration,
            "Событие",
            isHTML -> new EventNotificationConvector(isHTML)
                .toNotification(event, cashbox.get()),
            NotificationType.EVENT
        );
    }

    @Override
    public void notify(final Configuration configuration,
                       final Transaction transaction) {
        notify(
            configuration,
            "Транзакция",
            isHTML -> new TransactionNotificationConvector(isHTML)
                .toNotification(transaction, cashbox.get()),
            NotificationType.TRANSACTION
        );
    }

    @Override
    public void debts() {
        repository.discharge(NotificationType.DEBTS);

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

            notify(title, toMessage, NotificationType.DEBTS);
        }
    }

    @Override
    public void statistic() {
        repository.discharge(NotificationType.STATISTIC);

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
            NotificationType.STATISTIC
        );
    }

    @Override
    public void deposit() {
        repository.discharge(NotificationType.DEPOSITS);

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
            NotificationType.DEPOSITS);
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
     * @param notification Уведомление.
     */
    private void notify(final Notification notification) {
        executors
            .stream()
            .filter(executor ->
                executor.getExecutor().equals(notification.getExecutor())
            )
            .findFirst()
            .ifPresentOrElse(
                executor -> notify(executor, notification),
                () -> LOGGER.info(
                    "Executor not found. Delivery type {}",
                    notification.getExecutor()
                )
            );
    }

    /**
     * Оправка уведомлений.
     *
     * @param executor Описание бизнес логики отправки уведомлений.
     * @param notification Уведомление.
     */
    private void notify(final NotificationExecutor executor,
                        final Notification notification) {
        try {
            LOGGER.info(
                "Executor {}. Send message.", executor.getExecutor()
            );

            executor.notify(
                notification.getName(), notification.getMessage(), Map.of()
            );

            repository.delivered(GUIDs.parse(notification));
        } catch (Exception e) {
            LOGGER.error(
                "Executor {}. Title {}. Message {}.",
                executor.getExecutor(),
                notification.getName(),
                notification.getMessage()
            );
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Оправка уведомлений.
     *
     * @param title Заголовок.
     * @param message Сообщение.
     * @param type Тип уведомления.
     */
    private void notify(final String title,
                        final Function<Boolean, String> message,
                        final NotificationType type) {
        repository.transaction(configuration ->
            notify(configuration, title, message, type)
        );
    }

    /**
     * Оправка уведомлений.
     *
     * @param configuration Настройки транзакции.
     * @param title Заголовок.
     * @param message Сообщение.
     * @param notificationType Тип уведомления.
     */
    private void notify(final Configuration configuration,
                        final String title,
                        final Function<Boolean, String> message,
                        final NotificationType notificationType) {
        for (NotificationExecutor executor : executors) {
            Notification notification = new Notification(
                GUIDs.random().toString(),
                title,
                message.apply(executor.isHTML()),
                notificationType,
                executor.getExecutor(),
                null,
                Dates.format(Dates.now()),
                null,
                null
            );

            repository.create(configuration, notification);
        }
    }
}
