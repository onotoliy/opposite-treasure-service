package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.core.ReaderRepository;

import java.util.List;
import java.util.UUID;

public abstract class AbstractReaderService<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter,
    R extends ReaderRepository<E, P>>
implements ReaderService<E, P> {

    protected final R repository;

    public AbstractReaderService(R repository) {
        this.repository = repository;
    }

    @Override
    public E get(UUID uuid) {
        return repository.get(uuid);
    }

    public List<Option> getAll() {
        return repository.getAll();
    }

    @Override
    public Page<E> getAll(P parameter) {
        return repository.getAll(parameter);
    }
}
