package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.treasure.dto.Contact;
import com.github.onotoliy.opposite.treasure.exceptions.NotificationException;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики push уведомления.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
@Qualifier("firebase")
public class FirebaseNotificationExecutor implements NotificationExecutor {

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
     * @throws IOException Ошибка чтения файла подключения Firebase.
     */
    @Autowired
    public FirebaseNotificationExecutor(
        @Value("${treasure.firebase.config.path}") final String path,
        @Value("${treasure.firebase.topic}") final String topic,
        @Value("${treasure.firebase.icon}") final String icon
    ) throws IOException {

        this.topic = topic;
        this.icon = icon;

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
    public void notify(final Contact to,
                       final String title,
                       final String body,
                       final Map<String, String> parameters) {

    }

    @Override
    public void notify(final String title,
                       final String body,
                       final Map<String, String> parameters) {
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
                .putAllData(parameters)
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

    @Override
    public String getExecutor() {
        return FirebaseNotificationExecutor.class.getSimpleName();
    }
}
