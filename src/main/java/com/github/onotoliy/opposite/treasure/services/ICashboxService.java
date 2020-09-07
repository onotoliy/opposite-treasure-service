package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.treasure.bpp.log.Log;

/**
 * Сервис чтения данных о кассе.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface ICashboxService {

    /**
     * Получение данных о кассе.
     *
     * @return Данные о кассе.
     */
    @Log(db = true)
    Cashbox get();
}
