package com.github.onotoliy.opposite.treasure.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Конфигурация Telegram bot-а.
 *
 * @author Anatoliy Pokhresnyi
 */
@Component
public class TelegramConfiguration {

    /**
     * Telegram bot.
     */
    private final TelegramBot bot;

    /**
     * Конструтор.
     *
     * @param bot Telegram bot.
     */
    @Autowired
    public TelegramConfiguration(final TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Запускает Telegram bot-а.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void initialization() {
        try {
            new TelegramBotsApi(DefaultBotSession.class)
                .registerBot(bot);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
