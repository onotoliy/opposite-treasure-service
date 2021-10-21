package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.treasure.dto.Contact;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики уведомления отправляемого SMS.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
@Qualifier("twilio")
public class TwilioNotificationExecutor implements NotificationExecutor {

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * От кого отправляется сообщение.
     */
    private final PhoneNumber from;

    /**
     * Отправка СМС сообщений отключена.
     */
    private final boolean off;

    /**
     * Конструктор.
     *
     * @param username Имя пользователя.
     * @param password Пароль
     * @param from От кого отправляется сообщение.
     * @param off Отправка СМС сообщений отключена.
     * @param users Сервис чтения данных о пользвателях из Keycloak.
     */
    @Autowired
    public TwilioNotificationExecutor(
        @Value("${treasure.twilio.username}") final String username,
        @Value("${treasure.twilio.password}") final String password,
        @Value("${treasure.twilio.from}") final String from,
        @Value("${treasure.twilio.off}") final boolean off,
        final KeycloakRPC users
    ) {
        this.off = off;
        this.users = users;
        this.from = new PhoneNumber(from);

        Twilio.init(username, password);
    }

    @Override
    public void notify(final Contact to,
                       final String title,
                       final String body,
                       final Map<String, String> parameters) {
        if (Strings.isEmpty(to.getPhone())) {
            throw new IllegalArgumentException(String.format(
                "У пользователя (%s) не указан номер телефона", to.getUuid()
            ));
        }

        notify(to.getPhone(), title, body);
    }

    @Override
    public void notify(final String title,
                       final String body,
                       final Map<String, String> parameters) {
        users.getAll()
             .stream()
             .map(user -> users.getContact(user.getUuid()))
             .filter(Objects::nonEmpty)
             .filter(Contact::isNotifyByPhone)
             .map(Contact::getPhone)
             .filter(Strings::nonEmpty)
             .forEach(to -> notify(to, title, body));
    }

    @Override
    public String getExecutor() {
        return TwilioNotificationExecutor.class.getSimpleName();
    }

    /**
     * Отправка текстового уведомления.
     *
     * @param to Пользователь.
     * @param title Заголовок.
     * @param body Сообщение.
     */
    private void notify(final String to,
                        final String title,
                        final String body) {
        if (off) {
            return;
        }

        Message.creator(new PhoneNumber(to), from, title + "\n" + body)
               .create();
    }
}
