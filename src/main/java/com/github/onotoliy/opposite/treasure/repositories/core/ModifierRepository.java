package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import org.jooq.Configuration;

import java.util.UUID;
import java.util.function.Consumer;

public interface ModifierRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter>
extends ReaderRepository<E, P> {

    void transaction(Consumer<Configuration> consumer);

    E create(E dto);

    E create(Configuration configuration, E dto);

    E update(E dto);

    E update(Configuration configuration, E dto);

    void delete(UUID uuid);

    void delete(Configuration configuration, UUID uuid);
}
