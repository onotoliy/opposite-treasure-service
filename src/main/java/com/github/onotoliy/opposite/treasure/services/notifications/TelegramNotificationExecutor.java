package com.github.onotoliy.opposite.treasure.services.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.onotoliy.opposite.treasure.dto.Contact;
import com.github.onotoliy.opposite.treasure.exceptions.NotificationException;
import com.github.onotoliy.opposite.treasure.utils.Strings;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Описание бизнес логики уведомления отправляемого через Telegram.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
@Qualifier("telegram")
public class TelegramNotificationExecutor implements NotificationExecutor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(TelegramNotificationExecutor.class);

    /**
     * Duration.
     */
    private static final int DURATION = 10;

    /**
     * Host.
     */
    private final String host;

    /**
     * Bot API key.
     */
    private final String bot;

    /**
     * Chat ID.
     */
    private final String chat;

    /**
     * HTTP client.
     */
    private final HttpClient client;

    /**
     * Конструктор.
     *
     * @param host Host.
     * @param bot  Bot API key.
     * @param chat Chat ID.
     */
    public TelegramNotificationExecutor(
        @Value("${treasure.telegram.host}") final String host,
        @Value("${treasure.telegram.bot-api-key}") final String bot,
        @Value("${treasure.telegram.chat-id}") final String chat
    ) {
        this.host = host;
        this.bot = bot;
        this.chat = chat;
        this.client = HttpClient
            .newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(DURATION))
            .build();
    }

    @Override
    public void notify(final Contact to,
                       final String title,
                       final String body,
                       final Map<String, String> parameters) {
        if (Strings.isEmpty(to.getPhone())) {
            throw new IllegalArgumentException(String.format(
                "У пользователя (%s) не указан чат Telegram", to.getUuid()
            ));
        }

        notify(chat, title, body);
    }

    @Override
    public void notify(final String title,
                       final String body,
                       final Map<String, String> parameters) {
        notify(chat, title, body);
    }

    @Override
    public boolean isHTML() {
        return true;
    }

    /**
     * Отправка текстового уведомления.
     *
     * @param to    Пользователь.
     * @param title Заголовок.
     * @param body  Сообщение.
     */
    private void notify(final String to,
                        final String title,
                        final String body
    ) {
        String url = "https://" + host + "/bot" + bot + "/sendMessage?";
        String message = title + "\n" + body.replaceAll("<br/>", "\n");

        LOGGER.info(
            "Telegram notify. To {}. Title {}. Body {}", to, title, body
        );

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("chat_id", Long.parseLong(to));
        node.put("text", message);
        node.put("parse_mode", "html");

        HttpRequest request = HttpRequest
            .newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(node.toString()))
            .uri(URI.create(url))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .build();

        try {
            HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.info(
                "Telegram notification. Response code: {}, Body: {}. {}",
                response.statusCode(), response.body(), response
            );

            if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new NotificationException(String.format(
                    "Ошибка при отправки уведомления в Telegram. %s",
                    response.body()
                ));
            }

        } catch (Exception e) {
            throw new NotificationException(e);
        }
    }
}
