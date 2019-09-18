package com.github.onotoliy.opposite.treasure.rpc;

import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.treasure.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRPC {

    private static Map<UUID, Option> USERS =
        Map.of(
            UUID.fromString("8098207c-6fbb-45ec-ae10-184607262a1a"),
            new Option("8098207c-6fbb-45ec-ae10-184607262a1a", "Александр"),
            UUID.fromString("7167c7d7-9695-4a6c-b746-b860255b36bb"),
            new Option("7167c7d7-9695-4a6c-b746-b860255b36bb", "Максим"),
            UUID.fromString("29325b60-f623-417d-b7d4-a106c42e8c77"),
            new Option("29325b60-f623-417d-b7d4-a106c42e8c77", "Иван"),
            UUID.fromString("f6f2fdee-e611-4d27-9288-e30d124de1de"),
            new Option("f6f2fdee-e611-4d27-9288-e30d124de1de", "Артем"),
            UUID.fromString("8960887b-6f90-410e-8d10-5204e04982da"),
            new Option("8960887b-6f90-410e-8d10-5204e04982da", "Дмитрий"),
            UUID.fromString("fc4e6582-e4ac-46e0-a9e3-80dbdb3ad9a7"),
            new Option("fc4e6582-e4ac-46e0-a9e3-80dbdb3ad9a7", "Никита"),
            UUID.fromString("8cbc7413-7841-47af-b635-f6ac88e553b1"),
            new Option("8cbc7413-7841-47af-b635-f6ac88e553b1", "Михаил"),
            UUID.fromString("433e3a62-0d27-4d2d-8f46-c0cf04d253ae"),
            new Option("433e3a62-0d27-4d2d-8f46-c0cf04d253ae", "Даниил"),
            UUID.fromString("9f02281b-8696-4bb3-8e0c-0f8a1c7bceaa"),
            new Option("9f02281b-8696-4bb3-8e0c-0f8a1c7bceaa", "Егор"),
            UUID.fromString("c3facefb-c626-4505-8a7f-b3f83f308bfc"),
            new Option("c3facefb-c626-4505-8a7f-b3f83f308bfc", "Андрей"));

    public Option find(UUID uuid) {
        return findOption(uuid).orElseThrow(
            () -> new NotFoundException(Option.class, uuid));
    }

    public Optional<Option> findOption(UUID uuid) {
        return Optional.of(USERS.get(uuid));
    }

    public List<Option> getAll() {
        return new ArrayList<>(USERS.values());
    }
}
