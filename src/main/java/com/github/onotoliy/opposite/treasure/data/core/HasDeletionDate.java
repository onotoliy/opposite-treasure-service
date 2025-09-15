package com.github.onotoliy.opposite.treasure.data.core;

import java.time.Instant;

/**
 * Класс содержит поле "Дата удаления".
 *
 * @author Anatoliy Pokhresnyi
 */
public interface HasDeletionDate {

    /**
     * Возращает дату удаления.
     *
     * @return Дата удаления.
     */
    Instant deletionDate();
}
