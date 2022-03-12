package com.github.onotoliy.opposite.treasure.telegram;

import com.github.onotoliy.opposite.treasure.utils.Strings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Утилитарный класс Telegram бота.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class TelegramUtils {

    /**
     * Конструктор.
     */
    private TelegramUtils() {

    }

    /**
     * Список кнопок, главного меню.
     */
    public static final List<List<InlineKeyboardButton>> TOP_MENU =
        List.of(
            List.of(
                InlineKeyboardButton
                    .builder()
                    .text("Касса")
                    .callbackData("cashbox")
                    .build(),
                InlineKeyboardButton
                    .builder()
                    .text("Депозиты")
                    .callbackData("deposits")
                    .build()
            ),
            List.of(
                InlineKeyboardButton
                    .builder()
                    .text("Долги")
                    .callbackData("debts")
                    .build(),
                InlineKeyboardButton
                    .builder()
                    .text("Уведомить")
                    .callbackData("notify")
                    .build()
            ));

    /**
     * Получение имени пользователя в Telegram-е.
     *
     * @param user Пользователь.
     * @return Имя пользователя.
     */
    public static String getUsername(final User user) {
        if (Strings.nonEmpty(user.getUserName())) {
            return user.getUserName();
        }

        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();

        if (Strings.nonEmpty(firstName) && Strings.nonEmpty(lastName)) {
            return firstName + " " + lastName;
        }

        return Strings.nonEmpty(firstName)
            ? firstName
            : lastName;
    }

    /**
     * Получение сообщение в формате Telegram.
     *
     * @param update Команда.
     * @param text Текстовое сообщение.
     * @param keyboard Меню.
     * @return Сообщение в формате Telegram
     */
    public static SendMessage getMessage(
        final Update update,
        final String text,
        final List<List<InlineKeyboardButton>> keyboard
    ) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        markup.setKeyboard(keyboard);

        final SendMessage message = new SendMessage();

        message.setText(text);
        message.enableHtml(true);
        message.setReplyMarkup(markup);

        if (update.hasCallbackQuery()) {
            final CallbackQuery query = update.getCallbackQuery();
            final String chat = String.valueOf(query.getMessage().getChatId());

            message.setChatId(chat);
        } else {
            final String chat = String.valueOf(update.getMessage().getChatId());

            message.setChatId(chat);
        }

        return message;
    }
}
