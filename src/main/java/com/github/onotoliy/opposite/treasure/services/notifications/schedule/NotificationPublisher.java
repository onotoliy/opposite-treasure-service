package com.github.onotoliy.opposite.treasure.services.notifications.schedule;

import javax.jms.Queue;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Сервис отправляющий в очередь на запросы отправку уведомлений.
 */
@Component
public class NotificationPublisher {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(NotificationPublisher.class);

    /**
     * JmsTemplate.
     */
    private final JmsTemplate template;

    /**
     * Очередь.
     */
    private final Queue queue;

    /**
     * Конструктор.
     *
     * @param template JmsTemplate.
     * @param queue Очередь.
     */
    @Autowired
    public NotificationPublisher(
        final JmsTemplate template,
        final Queue queue
    ) {
        this.template = template;
        this.queue = queue;
    }

    /**
     * Отправить запрос отправку уведомлений.
     *
     * @param type Тип.
     */
    public void publish(final NotificationType type) {
        publish(type, null);
    }

    /**
     * Отправить запрос отправку уведомлений.
     *
     * @param type Тип.
     * @param uuid Уникальный идентификатор объекта.
     */
    public void publish(final NotificationType type, final UUID uuid) {
        LOGGER.info("Sending message. Type {}, Object UUID {}", type, uuid);

        template.convertAndSend(queue, new NotificationObject(type, uuid));
    }

}
