package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.services.core.ModifierService;

import java.util.UUID;

public abstract class AbstractModifierResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends ModifierService<E, P>>
extends AbstractReaderResource<E, P, R>
implements ModifierResource<E> {

    public AbstractModifierResource(R repository) {
        super(repository);
    }

    @Override
    public E create(E dto) {
        return service.create(dto);
    }

    @Override
    public E update(E dto) {
        return service.update(dto);
    }

    @Override
    public void delete(UUID uuid) {
        service.delete(uuid);
    }
}