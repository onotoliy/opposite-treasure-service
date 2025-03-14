package com.github.onotoliy.opposite.treasure.data;

import com.github.onotoliy.opposite.treasure.data.core.HasName;
import com.github.onotoliy.opposite.treasure.data.core.HasUUID;

import java.util.Set;
import java.util.UUID;

public record User(
        UUID uuid,
        String name,
        String login,
        String email,
        String phone,
        boolean notifyByPhone,
        boolean notifyByEmail,
        Set<String> roles
) implements HasUUID, HasName {


}
