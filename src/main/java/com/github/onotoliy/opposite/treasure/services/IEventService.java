package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;

/**
 * Сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface IEventService
extends ModifierService<Event, EventSearchParameter> {

}
