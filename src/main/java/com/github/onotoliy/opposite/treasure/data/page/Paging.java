package com.github.onotoliy.opposite.treasure.data.page;

/**
 * Описание стриницы.
 *
 * @param start Начальный элемент.
 * @param size Размер страницы.
 * @author Anatoliy Pokhresnyi
 */
public record Paging(
    int start,
    int size
) {

}
