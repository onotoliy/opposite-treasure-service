package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.SearchParameter;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Поисковые параметры для депозита.
 *
 * @param q Поисковая строка
 * @param enable Активные пользователи
 * @param offset Количество записей которое необходимо пропустить
 * @param numberOfRows Размер страницы
 * @author Anatoliy Pokhresnyi
 */
public record DepositSearchParameter(
    @Parameter(description = "Поисковая строка")
    @RequestParam(value = "q", required = false)
    String q,
    @Parameter(description = "Активные пользователи")
    @RequestParam(value = "enable", required = false)
    Boolean enable,
    @Parameter(description = "Количество записей которое необходимо пропустить")
    @RequestParam(value = "offset", required = false, defaultValue = "0")
    int offset,
    @Parameter(description = "Размер страницы")
    @RequestParam(value = "numberOfRows", required = false, defaultValue = "10")
    int numberOfRows
) implements SearchParameter {

    /**
     * Параметры поиска абсолютно всех депозитов.
     */
    public static final DepositSearchParameter ALL =
        new DepositSearchParameter(null, null, 0, 1000);

    /**
     * Параметры поиска всех активных депозитов.
     */
    public static final DepositSearchParameter ALL_ENABLE =
        new DepositSearchParameter(null, true, 0, 1000);
}
