package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Cashbox;

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
    Cashbox get();
}
