package com.github.onotoliy.opposite.treasure.dto;

public abstract class SearchParameter {

    private final int offset;

    private final int numberOfRows;

    protected SearchParameter(int offset, int numberOfRows) {
        this.offset = offset;
        this.numberOfRows = numberOfRows;
    }

    protected SearchParameter() {
        this(0, 10);
    }

    public int offset() {
        return offset;
    }

    public int numberOfRows() {
        return numberOfRows;
    }
}
