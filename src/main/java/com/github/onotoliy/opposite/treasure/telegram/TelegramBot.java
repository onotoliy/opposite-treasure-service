package com.github.onotoliy.opposite.treasure.telegram;

import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.telegram.actions.TelegramAction;
import com.github.onotoliy.opposite.treasure.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Telegram bot.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class TelegramBot extends TelegramLongPollingBot {

    /**
     * Список Action-ов.
     */
    private final List<TelegramAction> actions;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Название Telegram бота.
     */
    private final String bot;

    /**
     * Токен для Telegram бота.
     */
    private final String token;

    /**
     * Конструктор.
     *
     * @param bot Название Telegram бота.
     * @param token Токен для Telegram бота.
     * @param actions Список Action-ов.
     * @param users Сервис чтения данных о пользвателях из Keycloak.
     */
    @Autowired
    public TelegramBot(
        @Value("${treasure.telegram.bot}") final String bot,
        @Value("${treasure.telegram.bot-api-key}") final String token,
        final List<TelegramAction> actions,
        final KeycloakRPC users
    ) {
        this.actions = actions;
        this.users = users;
        this.bot = bot;
        this.token = token;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        try {
            execute(getMessage(update));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return bot;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    /**
     * Получения сообщения.
     *
     * @param update Команда.
     * @return Сообщение в Telegram.
     */
    private SendMessage getMessage(final Update update) {
        final User user = update.hasCallbackQuery()
            ? update.getCallbackQuery().getFrom()
            : update.getMessage().getFrom();
        final String username = TelegramUtils.getUsername(user);

        if (Strings.isEmpty(username)) {
            final String message = "Здравствуйте, нежданный гость! "
                + "Вы уверены, что Вам именно сюда? "
                + "Если да, то укажите ФИО или username в Telegram.";

            return TelegramUtils.getMessage(
                update,
                message,
                Collections.emptyList()
            );
        }

        if (!users.getTelegramUsernames().contains(username)) {
            final String message = "Здравствуйте, " + username + "! "
                + "Вы уверены, что Вам именно сюда? "
                + "Если да, то свяжитесь с администратором.";

            return TelegramUtils.getMessage(
                update,
                message,
                Collections.emptyList()
            );
        }

        Optional<TelegramAction> action = actions
            .stream()
            .filter(e -> e.mine(update))
            .findFirst();

        if (action.isPresent()) {
            return action.get().message(update);
        } else {
            final String callback = update.hasCallbackQuery()
                ? update.getCallbackQuery().getData()
                : "";

            if (callback.equals("back")) {
                return TelegramUtils.getMessage(
                    update,
                    "Назад",
                    TelegramUtils.TOP_MENU
                );
            }

            final String message = "Здравствуйте, " + username + "! "
                + "Вы выбрали неизвестную операцию. "
                + "Пожалуйста, выберете операцию из списка!";

            return TelegramUtils.getMessage(
                update,
                message,
                TelegramUtils.TOP_MENU
            );
        }
    }

}
