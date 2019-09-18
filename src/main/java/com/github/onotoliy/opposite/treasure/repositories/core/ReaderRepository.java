package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.SearchParameter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReaderRepository<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor,
    P extends SearchParameter> {

    Optional<E> getOptional(UUID uuid);

    E get(UUID uuid);

    List<Option> getAll();

    Page<E> getAll(P parameter);
}
