package com.github.onotoliy.opposite.treasure.data.core;

import java.time.Instant;

/**
 * Класс содержит поле "Дата создания".
 *
 * @author Anatoliy Pokhresnyi
 */
public interface HasCreationDate {

    /**
     * Возвращает дату создания.
     *
     * @return Дата создания.
     */
    Instant creationDate();
}
