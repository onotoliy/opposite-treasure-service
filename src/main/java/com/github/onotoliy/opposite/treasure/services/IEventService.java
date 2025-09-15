package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.treasure.data.Deposit;
import com.github.onotoliy.opposite.treasure.data.Event;
import com.github.onotoliy.opposite.treasure.data.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.data.page.Page;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;

import java.util.UUID;

/**
 * Сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface IEventService
extends ModifierService<Event, EventSearchParameter> {

    /**
     * Получение списка должников.
     *
     * @param uuid Уникальный идентификатор события.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Списк должников.
     */
    Page<Deposit> getDebtors(UUID uuid, int offset, int numberOfRows);
}
