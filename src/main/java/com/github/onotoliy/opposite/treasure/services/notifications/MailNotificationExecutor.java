package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.treasure.dto.Contact;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Описание бизнес логики уведомления отправляемого по электронному адресу.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
@Qualifier("mail")
public class MailNotificationExecutor implements NotificationExecutor {

    /**
     * Отправитель сообщений.
     */
    private final JavaMailSender sender;

    /**
     * Mail от имени которого отправляются сообщения.
     */
    private final InternetAddress from;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Отправка на электронную почту отключена.
     */
    private final boolean off;

    /**
     * Конструктор.
     *
     * @param sender Отправитель сообщений.
     * @param users  Сервис чтения данных о пользвателях из Keycloak.
     * @param from   Mail от имени которого отправляются сообщения.
     * @param off    Отправка на электронную почту отключена.
     * @throws UnsupportedEncodingException Ошибка чтения электронного адреса.
     */
    @Autowired
    public MailNotificationExecutor(
        final JavaMailSender sender,
        final KeycloakRPC users,
        @Value("${spring.mail.username}") final String from,
        @Value("${treasure.mail.off}") final boolean off
    ) throws UnsupportedEncodingException {

        this.off = off;
        this.users = users;
        this.sender = sender;
        this.from = new InternetAddress(from, "Оппозит МК");
    }

    @Override
    public boolean isHTML() {
        return true;
    }

    @Override
    public String getExecutor() {
        return MailNotificationExecutor.class.getSimpleName();
    }

    @Override
    public void notify(final String title,
                       final String body,
                       final Map<String, String> parameters) {
        users.getAll()
             .stream()
             .map(user -> users.getContact(user.getUuid()))
             .filter(Objects::nonEmpty)
             .filter(Contact::isNotifyByEmail)
             .map(Contact::getEmail)
             .filter(Strings::nonEmpty)
             .forEach(to -> notify(to, title, body));
    }

    @Override
    public void notify(final Contact to,
                       final String title,
                       final String body,
                       final Map<String, String> parameters) {
        if (Strings.isEmpty(to.getEmail())) {
            throw new IllegalArgumentException(String.format(
                "У пользователя (%s) не указана электронная почта",
                to.getUuid()
            ));
        }

        notify(to.getEmail(), title, body);
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

        sender.send(message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(body, true);
        });
    }
}
