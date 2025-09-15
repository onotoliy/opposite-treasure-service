package com.github.onotoliy.opposite.treasure.data.page;

import java.util.List;

/**
 * Страница.
 *
 * @param meta Мета данные о странице.
 * @param context Содержимое стриницы.
 * @param <T> Тип объектов на стринице.
 * @author Anatoliy Pokhresnyi
 */
public record Page<T>(
    Meta meta,
    List<T> context
) {

}
