package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
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
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationService {

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
    public NotificationService(
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

    /**
     * Отправка push уведомления события.
     *
     * @param event Событие.
     */
    public void push(final Event event) {
        String body = String.format(
            "Сумма взноса %s. Сдать до: %s",
            event.getContribution(), event.getDeadline()
        );

        push(event.getName(), body);
    }

    /**
     * Отправка push уведомления транзакции.
     *
     * @param transaction Транзакция.
     */
    public void push(final Transaction transaction) {
        String person = transaction.getPerson() == null
            ? "" : " Пользователь " + transaction.getPerson().getName();

        String body = String.format(
            "Тип %s. Сумма: %s. %s",
            transaction.getType().getLabel(), transaction.getCash(), person
        );

        push(transaction.getName(), body);
    }

    /**
     * Отправка push уведомления.
     *
     * @param title Заголовок.
     * @param body Сообщение.
     */
    private void push(final String title, final String body) {
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
