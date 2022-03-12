package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.data.User;
import com.github.onotoliy.opposite.treasure.convectors.DepositNotificationConvector;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.DepositService;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Action Telegram bot-а на запрос депозитов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DepositTelegramAction extends AbstractTelegramAction {

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Сервис чтения депозитов.
     */
    private final DepositService deposit;

    /**
     * Конструктор.
     *
     * @param cashbox Сервис чтения данных о кассе.
     * @param deposit Сервис чтения депозитов.
     * @param users Сервис чтения данных о пользвателях из Keycloak.
     */
    @Autowired
    public DepositTelegramAction(
        final ICashboxService cashbox,
        final DepositService deposit,
        final KeycloakRPC users
    ) {
        super("deposit");

        this.cashbox = cashbox;
        this.deposit = deposit;
        this.users = users;
    }

    @Override
    protected String getText(final Update update) {
        final DepositSearchParameter parameter =
            new DepositSearchParameter(0, Integer.MAX_VALUE);
        final Set<String> members = users.getAll()
                                         .stream()
                                         .map(User::getUuid)
                                         .collect(Collectors.toSet());

        String title = "Переплата на " + Dates.toShortFormat(Dates.now());
        String content = new DepositNotificationConvector(members, true)
            .toNotification(
                deposit.getAll(parameter).getContext(),
                cashbox.get()
            )
            .replaceAll("<br/>", "\n");

        return title + "\n" + content;
    }
}
