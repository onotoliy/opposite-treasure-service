package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface ModifierResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor>
extends ReaderResource<E> {

    @PostMapping
    E create(@RequestBody E dto);

    @PutMapping
    E update(@RequestBody E dto);

    @DeleteMapping(value = "/{uuid}")
    void delete(@PathVariable("uuid") UUID uuid);

}
