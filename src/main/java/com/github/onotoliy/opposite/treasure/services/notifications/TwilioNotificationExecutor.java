package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.CashboxService;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики уведомления отправляемого SMS.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class TwilioNotificationExecutor implements NotificationExecutor {

    /**
     * От кого отправляется сообщение.
     */
    private final PhoneNumber from;

    /**
     * Сервис чтения данных о кассе.
     */
    private final CashboxService cashbox;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC keycloak;

    /**
     * Конструктор.
     *
     * @param username Имя пользователя.
     * @param password Пароль
     * @param from От кого отправляется сообщение.
     * @param cashbox Сервис чтения данных о кассе.
     * @param keycloak Сервис чтения данных о пользвателях из Keycloak.
     */
    @Autowired
    public TwilioNotificationExecutor(
        @Value("treasure.twilio.username") final String username,
        @Value("treasure.twilio.password") final String password,
        @Value("treasure.twilio.from") final String from,
        final CashboxService cashbox,
        final KeycloakRPC keycloak
    ) {
        Twilio.init(username, password);

        this.from = new PhoneNumber(from);
        this.cashbox = cashbox;
        this.keycloak = keycloak;
    }

    /**
     * Отправка sms уведомления.
     *
     * @param body Текст уведомления.
     */
    private void notify(final String body) {
        keycloak.getAll()
                .stream()
                .filter(User::getNotifyByPhone)
                .map(User::getPhone)
                .filter(Strings::nonEmpty)
                .forEach(phone -> Message
                    .creator(new PhoneNumber(phone), from, body)
                    .create()
                );
    }

    @Override
    public void notify(final Event event) {
        notify(String.format(
            "%s. \nСумма взноса: %s. Сдать до: %s",
            event.getName(), event.getContribution(), event.getDeadline()
        ));
    }

    @Override
    public void notify(final Transaction transaction) {
        StringBuilder builder = new StringBuilder()
            .append(transaction.getName())
            .append("Тип: ")
            .append(transaction.getType().getLabel())
            .append(". ")
            .append("Сумма: ")
            .append(transaction.getCash())
            .append(". ");

        if (Objects.nonEmpty(transaction.getEvent())) {
            builder.append("Событие: ")
                   .append(transaction.getEvent().getName())
                   .append(". ");
        }

        if (Objects.nonEmpty(transaction.getPerson())) {
            builder.append("Пользователь: ")
                   .append(transaction.getPerson().getName())
                   .append(". ");
        }

        builder.append("В кассе: ")
               .append(cashbox.get().getDeposit())
               .append("руб.");

        notify(builder.toString());
    }
}
