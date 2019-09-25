package com.github.onotoliy.opposite.treasure.dto;

import com.github.onotoliy.opposite.treasure.utils.Strings;

/**
 * Поисковые параметры для события.
 *
 * @author Anatoliy Pokhresnyi
 */
public class EventSearchParameter extends SearchParameter {

    /**
     * Название.
     */
    private final String name;

    /**
     * Конструктор.
     *
     * @param name Название.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     */
    public EventSearchParameter(final String name,
                                final int offset,
                                final int numberOfRows) {
        super(offset, numberOfRows);

        this.name = name;
    }

    /**
     * Проверяет необходимо ли производить поиск по названию.

     * @return Результат проверки.
     */
    public boolean hasName() {
        return Strings.nonEmpty(name);
    }

    /**
     * Получает название.
     *
     * @return Название.
     */
    public String getName() {
        return name;
    }
}
