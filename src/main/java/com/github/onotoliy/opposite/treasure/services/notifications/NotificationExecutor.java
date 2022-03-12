package com.github.onotoliy.opposite.treasure.services.notifications;

import com.github.onotoliy.opposite.treasure.dto.Contact;

import java.util.Map;

/**
 * Описание бизнес логики отправки уведомлений.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface NotificationExecutor {

    /**
     * Отправка текстового уведомления.
     *
     * @param to Пользователь.
     * @param title Заголовок.
     * @param body Сообщение.
     * @param parameters Дополнительный параметры.
     */
    void notify(Contact to,
                String title,
                String body,
                Map<String, String> parameters);

    /**
     * Отправка текстового уведомления.
     *
     * @param title Заголовок.
     * @param body Сообщение.
     * @param parameters Дополнительный параметры.
     */
    void notify(String title, String body, Map<String, String> parameters);

    /**
     * Нужна HTML разметка.
     *
     * @return Нужна HTML разметка.
     */
    default boolean isHTML() {
        return false;
    }

    /**
     * Получение типа класса.
     *
     * @return Тип класса.
     */
    String getExecutor();
}
