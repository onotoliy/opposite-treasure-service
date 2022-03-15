package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.treasure.dto.NotificationType;
import com.github.onotoliy.opposite.treasure.services.INotificationService;
import com.github.onotoliy.opposite.treasure.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    protected List<List<InlineKeyboardButton>> getKeyboard(
        final Update update
    ) {
        final String action = update.getCallbackQuery().getData();

        if (action.equalsIgnoreCase(this.action)) {
            return List
                .of(
                    InlineKeyboardButton
                        .builder()
                        .text("Все уведомления")
                        .callbackData("notify-all")
                        .build(),
                    InlineKeyboardButton
                        .builder()
                        .text("Только существующие")
                        .callbackData("notify-without-statistic")
                        .build(),
                    InlineKeyboardButton
                        .builder()
                        .text("Назад")
                        .callbackData("notify-back")
                        .build()
                )
                .stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());
        } else {
            return TelegramUtils.TOP_MENU;
        }
    }

    @Override
    protected String getText(final Update update) {
        final String action = update.getCallbackQuery().getData();

        if (action.equalsIgnoreCase(this.action)) {
            return "Выберите действие!";
        } else {
            switch (action) {
                case "notify-all":
                    publisher.debts();
                    publisher.statistic();
                    publisher.deposit();
                    break;
                case "notify-without-statistic":
                    publisher.discharge(NotificationType.DEBTS);
                    publisher.discharge(NotificationType.STATISTIC);
                    publisher.discharge(NotificationType.DEPOSITS);
                    break;
                case "notify-back":
                default:
                    break;
            }

            publisher.publish();

            return "Сообщения отправленны!";
        }
    }
}
