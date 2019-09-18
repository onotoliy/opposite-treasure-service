package com.github.onotoliy.opposite.treasure.dto;

import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;

public class EventSearchParameter extends SearchParameter {

    private final String name;

    public EventSearchParameter(String name, int offset, int numberOfRows) {
        super(offset, numberOfRows);

        this.name = name;
    }

    public boolean hasName() {
        return STRING.nonEmpty(name);
    }

    public String getName() {
        return name;
    }
}
