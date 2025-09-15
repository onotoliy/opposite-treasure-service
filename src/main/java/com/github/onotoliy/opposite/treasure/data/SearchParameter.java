package com.github.onotoliy.opposite.treasure.data;

/**
 * Базовае поисковые параметры.
 *
 * @author Anatoliy Pokhresnyi
 */
public interface SearchParameter {

    /**
     * Получает количество записей которое необходимо пропустить.
     *
     * @return Количество записей которое необходимо пропустить.
     */
    int offset();

    /**
     * Получает размер страницы.
     *
     * @return Размер страницы.
     */
    int numberOfRows();
}
