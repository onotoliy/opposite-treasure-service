package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.treasure.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Action Telegram bot-а на запрос уведомлений в Telegram канал.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class NotificationTelegramAction extends AbstractTelegramAction {

    /**
     * Сервис уведомлений.
     */
    private final INotificationService publisher;

    /**
     * Конструктор.
     *
     * @param publisher Сервис уведомлений.
     */
    @Autowired
    public NotificationTelegramAction(
        final INotificationService publisher
    ) {
        super("notify");

        this.publisher = publisher;
    }

    @Override
    protected String getText(final Update update) {
        publisher.debts();
        publisher.statistic();
        publisher.deposit();

        publisher.publish();

        return "Сообщения отправленны!";
    }
}
