package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.convectors.DebtNotificationConvector;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.DebtService;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import com.github.onotoliy.opposite.treasure.telegram.TelegramUtils;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action Telegram bot-а на запрос долгов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DebtsTelegramAction extends AbstractTelegramAction {

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Сервис чтения долгов.
     */
    private final DebtService debt;

    /**
     * Конструктор.
     *
     * @param cashbox Сервис чтения данных о кассе.
     * @param users Сервис чтения данных о пользвателях из Keycloak.
     * @param debt Сервис чтения долгов.
     */
    @Autowired
    public DebtsTelegramAction(
        final ICashboxService cashbox,
        final KeycloakRPC users,
        final DebtService debt
    ) {
        super("debts");

        this.cashbox = cashbox;
        this.users = users;
        this.debt = debt;
    }

    @Override
    protected List<List<InlineKeyboardButton>> getKeyboard(
        final Update update
    ) {
        final String action = update.getCallbackQuery().getData();
        final Timestamp now = Dates.now();

        if (action.equalsIgnoreCase(this.action)) {

            final List<List<InlineKeyboardButton>> buttons = users
                .getAll()
                .stream()
                .filter(user -> !getDebts(now, user).isEmpty())
                .map(user -> InlineKeyboardButton
                    .builder()
                    .text(user.getName())
                    .callbackData(
                        this.action + "-" + user
                            .getUuid())
                    .build())
                .map(Collections::singletonList)
                .collect(Collectors.toList());

            buttons.add(Collections.singletonList(
                InlineKeyboardButton
                    .builder()
                    .text("Назад")
                    .callbackData("back")
                    .build()
            ));

            return buttons;
        } else {
            return TelegramUtils.TOP_MENU;
        }
    }

    @Override
    protected String getText(final Update update) {
        final Cashbox cb = cashbox.get();
        final Timestamp now = Dates.now();
        final String action = update.getCallbackQuery().getData();

        if (action.equalsIgnoreCase(this.action)) {
            final String title = "Итого долгов на " + Dates.toShortFormat(now);
            final String content = new DebtNotificationConvector(true)
                .toNotification(
                    users.getAll(),
                    user -> getDebts(now, user),
                    cb
                )
                .replaceAll("<br/>", "\n");

            return title + "\n" + content;
        } else {
            final String uuid = action.replaceFirst("debts-", "");
            final String title = "Долги на " + Dates.toShortFormat(Dates.now());

            return users
                .getAll()
                .stream()
                .filter(u -> u.getUuid().equals(uuid))
                .findFirst()
                .map(user -> title + "\n" + new DebtNotificationConvector(true)
                    .toNotification(user, getDebts(now, user), cb)
                    .replaceAll("<br/>", "\n")
                )
                .orElse("Упс! Пользователь не найден.");
        }
    }

    /**
     * Получение долгов пользователя на текущей момент.
     *
     * @param now  Текущей момент.
     * @param user Пользваотель.
     * @return Списко долгов пользователя.
     */
    private List<Event> getDebts(final Timestamp now, final User user) {
        return debt
            .getDebts(GUIDs.parse(user))
            .getContext()
            .stream()
            .filter(dto -> now.compareTo(Dates.parse(dto.getDeadline())) >= 0)
            .collect(Collectors.toList());
    }
}
