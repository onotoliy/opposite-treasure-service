package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ReaderService;

import java.util.UUID;

public abstract class AbstractReaderResource <
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    S extends ReaderService<E, P>>
implements ReaderResource<E> {

    protected final S service;

    public AbstractReaderResource(S service) {
        this.service = service;
    }

    @Override
    public E get(UUID uuid) {
        return service.get(uuid);
    }
}
