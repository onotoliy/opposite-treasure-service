package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.treasure.exceptions.NotificationException;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import com.github.onotoliy.opposite.treasure.utils.Objects;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики push уведомления.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class FirebaseNotificationExecutor implements NotificationExecutor {

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Название топика.
     */
    private final String topic;

    /**
     * URL к иконке.
     */
    private final String icon;

    /**
     * Конструктор.
     *
     * @param path Путь к файлу подключения Firebase.
     * @param topic Название топика.
     * @param icon URL к иконке.
     * @param cashbox Сервис чтения данных о кассе.
     * @throws IOException Ошибка чтения файла подключения Firebase.
     */
    @Autowired
    public FirebaseNotificationExecutor(
        @Value("${treasure.firebase.config.path}") final String path,
        @Value("${treasure.firebase.topic}") final String topic,
        @Value("${treasure.firebase.icon}") final String icon,
        final ICashboxService cashbox
    ) throws
        IOException {
        this.topic = topic;
        this.icon = icon;
        this.cashbox = cashbox;

        GoogleCredentials credentials =
            GoogleCredentials.fromStream(new FileInputStream(path));
        FirebaseOptions options = FirebaseOptions
            .builder()
            .setCredentials(credentials)
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Override
    public void notify(final Event event) {
        notify(event.getName(), String.format(
            "Сумма взноса %s. Сдать до: %s",
            event.getContribution(), event.getDeadline()
        ));
    }

    @Override
    public void notify(final Transaction transaction) {
        StringBuilder builder = new StringBuilder()
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

        notify(transaction.getName(), builder.toString());
    }

    /**
     * Отправка push уведомления.
     *
     * @param title Заголовок.
     * @param body Сообщение.
     */
    private void notify(final String title, final String body) {
        AndroidConfig android = AndroidConfig
            .builder()
            .setTtl(Duration.ofMinutes(2).toMillis())
            .setCollapseKey(topic)
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(AndroidNotification
                                 .builder()
                                 .setSound("default")
                                 .setColor("#FFFF00")
                                 .setTag(topic)
                                 .build())
            .build();

        Message message = Message
            .builder()
            .setAndroidConfig(android)
            .setNotification(Notification
                                 .builder()
                                 .setImage(icon)
                                 .setTitle(title)
                                 .setBody(body)
                                 .build())
            .setTopic(topic)
            .build();

        try {
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new NotificationException(e);
        }
    }
}
