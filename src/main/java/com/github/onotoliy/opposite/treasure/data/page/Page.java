package com.github.onotoliy.opposite.treasure.data.page;

import java.util.List;

public record Page<T>(
        Meta meta,
        List<T> context
) {

}

