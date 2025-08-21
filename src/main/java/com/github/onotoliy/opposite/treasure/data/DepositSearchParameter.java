package com.github.onotoliy.opposite.treasure.data;

/**
 * Поисковые параметры для депозита.
 *
 * @author Anatoliy Pokhresnyi
 */
public class DepositSearchParameter extends SearchParameter {

    /**
     * Конструктор.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     */
    public DepositSearchParameter(final int offset, final int numberOfRows) {
        super(offset, numberOfRows);
    }
}
