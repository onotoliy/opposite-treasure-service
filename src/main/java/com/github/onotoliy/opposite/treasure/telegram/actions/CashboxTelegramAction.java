package com.github.onotoliy.opposite.treasure.telegram.actions;

import com.github.onotoliy.opposite.treasure.convectors.CashboxNotificationConvector;
import com.github.onotoliy.opposite.treasure.services.ICashboxService;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Action Telegram bot-а на запрос депозитов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class CashboxTelegramAction extends AbstractTelegramAction {

    /**
     * Сервис чтения данных о кассе.
     */
    private final ICashboxService cashbox;

    /**
     * Конструктор.
     *
     * @param cashbox Сервис чтения данных о кассе.
     */
    @Autowired
    public CashboxTelegramAction(
        final ICashboxService cashbox
    ) {
        super("cashbox");

        this.cashbox = cashbox;
    }

    @Override
    protected String getText(final Update update) {
        String title = "На " + Dates.toShortFormat(Dates.now());
        String content = new CashboxNotificationConvector(true)
            .toNotification(
                cashbox.get(),
                cashbox.get()
            )
            .replaceAll("<br/>", "\n");

        return title + "\n" + content;
    }
}
