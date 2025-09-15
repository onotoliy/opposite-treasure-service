package com.github.onotoliy.opposite.treasure.data.page;

/**
 * Мета данные о странице.
 *
 * @param total Общее количество записей.
 * @param paging Описание страницы.
 * @author Anatoliy Pokhresnyi
 */
public record Meta(
    int total,
    Paging paging
) {

}
