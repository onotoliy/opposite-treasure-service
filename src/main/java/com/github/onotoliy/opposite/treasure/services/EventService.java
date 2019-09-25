package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Transaction;
import com.github.onotoliy.opposite.data.TransactionType;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static com.github.onotoliy.opposite.treasure.utils.BigDecimalUtil.MONEY;
import static com.github.onotoliy.opposite.treasure.utils.UUIDUtil.GUID;

@Service
public class EventService
extends AbstractModifierService<Event, EventSearchParameter, EventRepository> {

    private final DebtRepository debt;

    private final UserRPC user;

    @Autowired
    public EventService(final EventRepository repository,
                        final DebtRepository debt,
                        final UserRPC user) {
        super(repository);
        this.debt = debt;
        this.user = user;
    }

    @Override
    protected void create(final Configuration configuration, Event dto) {
        repository.create(configuration, dto);
        user.getAll().forEach(e -> debt.cost(configuration, GUID.parse(e), GUID.parse(dto)));
    }

    public Page<Event> getDebts(final UUID person) {
        return repository.getDebts(person);
    }
}
