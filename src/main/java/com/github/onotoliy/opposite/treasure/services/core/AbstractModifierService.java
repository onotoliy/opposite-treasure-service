package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.core.ModifierRepository;
import com.github.onotoliy.opposite.treasure.utils.UUIDUtil;
import org.jooq.Configuration;

import java.util.UUID;

public abstract class AbstractModifierService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends ModifierRepository<E, P>>
extends AbstractReaderService<E, P, R>
implements ModifierService<E, P> {

    public AbstractModifierService(R repository) {
        super(repository);
    }

    @Override
    public E create(E dto) {
        repository.transaction(configuration -> create(configuration, dto));

        return get(UUIDUtil.GUID.parse(dto));
    }

    protected void create(Configuration configuration, E dto) {
        repository.create(configuration, dto);
    }

    @Override
    public E update(E dto) {
        repository.transaction(configuration -> update(configuration, dto));

        return get(UUIDUtil.GUID.parse(dto));
    }

    protected void update(Configuration configuration, E dto) {
        repository.update(configuration, dto);
    }

    @Override
    public void delete(UUID uuid) {
        repository.transaction(configuration -> delete(configuration, uuid));
    }

    protected void delete(Configuration configuration, UUID uuid) {
        repository.delete(configuration, uuid);
    }
}
