package com.github.onotoliy.opposite.treasure.services.notifications;

import javax.mail.internet.InternetAddress;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики уведомления отправляемого по электронному адресу.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class MailNotificationExecutor implements NotificationExecutor {

    /**
     * Отправитель сообщений.
     */
    private final JavaMailSender sender;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC keycloak;

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Mail от имени которого отправляются сообщения.
     */
    private final InternetAddress from;

    /**
     * Конструктор.
     *
     * @param sender Отправитель сообщений.
     * @param cashbox Сервис чтения данных о кассе.
     * @param keycloak Сервис чтения данных о пользвателях из Keycloak.
     * @param from Mail от имени которого отправляются сообщения.
     * @throws UnsupportedEncodingException Ошибка чтения электронного адреса.
     */
    @Autowired
    public MailNotificationExecutor(
        final JavaMailSender sender,
        final ICashboxService cashbox,
        final KeycloakRPC keycloak,
        @Value("${spring.mail.username}") final String from)
    throws
        UnsupportedEncodingException {
        this.sender = sender;
        this.cashbox = cashbox;
        this.keycloak = keycloak;
        this.from = new InternetAddress(from, "Оппозит МК");
    }

    /**
     * Отправка уведомления по электронной почте.
     *
     * @param to Электронный адрес получателя.
     * @param title Заголовок уведомления.
     * @param body Текст уведомления.
     */
    private void notify(final String to,
                        final String title,
                        final String body) {
        sender.send(message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(body, true);
        });
    }

    /**
     * Отправка уведомлений по электронной почте.
     *
     * @param title Заголовок уведомления.
     * @param body Текст уведомления.
     */
    private void notify(final String title, final String body) {
        keycloak.getAll()
                .stream()
                .filter(User::getNotifyByEmail)
                .map(User::getEmail)
                .filter(Strings::nonEmpty)
                .forEach(email -> notify(email, title, body));
    }

    @Override
    public void notify(final Event event) {
        notify(event.getName(), String.format(
            "<b>%s.</b><br/><b>Сумма взноса</b>: %s.<br/><b>Сдать до:</b> %s",
            event.getName(), event.getContribution(), event.getDeadline()
        ));
    }

    @Override
    public void notify(final Transaction transaction) {
        StringBuilder builder = new StringBuilder()
            .append("<b>Тип</b>: ")
            .append(transaction.getType().getLabel())
            .append(".<br/>")
            .append("<b>Сумма</b>: ")
            .append(transaction.getCash())
            .append(".<br/>");

        if (Objects.nonEmpty(transaction.getEvent())) {
            builder.append("<b>Событие</b>: ")
                   .append(transaction.getEvent().getName())
                   .append(". ")
                   .append("<br/>");
        }

        if (Objects.nonEmpty(transaction.getPerson())) {
            builder.append("<b>Пользователь</b>: ")
                   .append(transaction.getPerson().getName())
                   .append(". ")
                   .append("<br/>");
        }

        builder.append("<b>В кассе</b>: ")
               .append(cashbox.get().getDeposit())
               .append("руб.");

        notify(transaction.getName(), builder.toString());
    }
}
