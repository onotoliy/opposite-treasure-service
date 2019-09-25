package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.core.ModifierRepository;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
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
    public E create(final E dto) {
        repository.transaction(configuration -> create(configuration, dto));

        return get(GUIDs.parse(dto));
    }

    protected void create(final Configuration configuration, final E dto) {
        repository.create(configuration, dto);
    }

    @Override
    public E update(final E dto) {
        repository.transaction(configuration -> update(configuration, dto));

        return get(GUIDs.parse(dto));
    }

    protected void update(final Configuration configuration, final E dto) {
        repository.update(configuration, dto);
    }

    @Override
    public void delete(final UUID uuid) {
        repository.transaction(configuration -> delete(configuration, uuid));
    }

    protected void delete(final Configuration configuration, final UUID uuid) {
        repository.delete(configuration, uuid);
    }
}
