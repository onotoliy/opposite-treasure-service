package com.github.onotoliy.opposite.treasure.dto;

import com.github.onotoliy.opposite.data.TransactionType;

import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.ObjectUtil.OBJECT;
import static com.github.onotoliy.opposite.treasure.utils.StringUtil.STRING;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

public class TransactionSearchParameter extends SearchParameter {

    private final String name;

    private final UUID user;

    private final UUID event;

    private final TransactionType type;

    public TransactionSearchParameter(String name, UUID user, UUID event, TransactionType type, int offset, int numberOfRows) {
        super(offset, numberOfRows);
        this.name = name;
        this.user = user;
        this.event = event;
        this.type = type;
    }

    public boolean hasName() {
        return STRING.nonEmpty(name);
    }

    public String getName() {
        return name;
    }

    public boolean hasUser() {
        return GUID.nonEmpty(user);
    }

    public UUID getUser() {
        return user;
    }

    public boolean hasEvent() {
        return GUID.nonEmpty(event);
    }

    public UUID getEvent() {
        return event;
    }

    public boolean hasType() {
        return OBJECT.nonEmpty(type) && OBJECT.nonEqually(type, TransactionType.NONE);
    }

    public TransactionType getType() {
        return type;
    }
}
