package com.github.onotoliy.opposite.treasure.web.core;

import com.github.onotoliy.opposite.data.core.HasAuthor;
import com.github.onotoliy.opposite.data.core.HasCreationDate;
import com.github.onotoliy.opposite.data.core.HasName;
import com.github.onotoliy.opposite.data.core.HasUUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface ReaderResource<
    E extends HasUUID & HasName & HasCreationDate & HasAuthor> {

    @GetMapping(value = "/{uuid}")
    E get(@PathVariable("uuid") UUID uuid);

}
