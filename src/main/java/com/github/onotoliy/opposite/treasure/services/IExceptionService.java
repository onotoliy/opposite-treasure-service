package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.core.ExceptionDevice;

/**
 * Сервис управления ошибками устройства.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface IExceptionService {

    /**
     * Регистрация ошибки устройства.
     *
     * @param exception Описание ошибки устройства.
     */
    void registration(ExceptionDevice exception);
}
