package com.github.onotoliy.opposite.treasure.telegram.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Action Telegram bot-а.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface TelegramAction {

    /**
     * Определение этому ли Action-у принадлежит запрос.
     *
     * @param update Команда.
     * @return Результат проверки.
     */
    boolean mine(Update update);

    /**
     * Получение сообщения в результате выполнения Action-а.
     *
     * @param update Команда.
     * @return Текстовое сообщение.
     */
    SendMessage message(Update update);

}
