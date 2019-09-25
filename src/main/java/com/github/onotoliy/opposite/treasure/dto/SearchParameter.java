package com.github.onotoliy.opposite.treasure.dto;

/**
 * Базовае поисковые параметры.
 *
 * @author Anatoliy Pokhresnyi
 */
public abstract class SearchParameter {

    /**
     * Количество записей которое необходимо пропустить по умолчанию.
     */
    private static final int OFFSET_DEFAULT = 0;

    /**
     * Размер страницы по умолчанию.
     */
    private static final int NUMBER_OF_ROWS = 10;

    /**
     * Количество записей которое необходимо пропустить.
     */
    private final int offset;

    /**
     * Размер страницы.
     */
    private final int numberOfRows;

    /**
     * Конструктор.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     */
    protected SearchParameter(final int offset, final int numberOfRows) {
        this.offset = offset;
        this.numberOfRows = numberOfRows;
    }

    /**
     * Конструктор по умолчанию.
     */
    protected SearchParameter() {
        this(OFFSET_DEFAULT, NUMBER_OF_ROWS);
    }

    /**
     * Получает количество записей которое необходимо пропустить.
     *
     * @return Количество записей которое необходимо пропустить.
     */
    public int offset() {
        return offset;
    }

    /**
     * Получает размер страницы.
     *
     * @return Размер страницы.
     */
    public int numberOfRows() {
        return NUMBER_OF_ROWS;
    }
}
