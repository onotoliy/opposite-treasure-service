package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.treasure.telegram.TelegramUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Абстрактный action Telegram bot-а.
 *
 * @author Anatoliy Pokhresnyi
 */
public abstract class AbstractTelegramAction implements TelegramAction {

    /**
     * Название action.
     */
    protected final String action;

    /**
     * Конструктор.
     *
     * @param action Название action.
     */
    public AbstractTelegramAction(final String action) {
        this.action = action;
    }

    /**
     * Заполнение ответа кнопками.
     *
     * @param update Команда.
     * @return Меню кнопок.
     */
    protected List<List<InlineKeyboardButton>> getKeyboard(
        final Update update
    ) {
        return TelegramUtils.TOP_MENU;
    }

    /**
     * Заполнение ответа текстовым сообщением.
     *
     * @param update Команда.
     * @return Текстовое сообщение.
     */
    protected String getText(final Update update) {
        return "";
    }

    @Override
    public boolean mine(final Update update) {
        return update.hasCallbackQuery()
            && update.getCallbackQuery().getData().startsWith(action);
    }

    @Override
    public SendMessage message(final Update update) {
        return TelegramUtils.getMessage(
            update,
            getText(update),
            getKeyboard(update)
        );
    }
}
