package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.rpc.UserRPC;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.util.UUID;

import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class EventService
extends AbstractModifierService<Event, EventSearchParameter, EventRepository> {

    /**
     * Репозиторий управления долгами.
     */
    private final DebtRepository debt;

    /**
     * Сервис чтения пользователей.
     */
    private final UserRPC user;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий событий.
     * @param debt Репозиторий долгов.
     * @param user Сервис пользователей.
     */
    @Autowired
    public EventService(final EventRepository repository,
                        final DebtRepository debt,
                        final UserRPC user) {
        super(repository);
        this.debt = debt;
        this.user = user;
    }

    @Override
    protected void create(final Configuration configuration, final Event dto) {
        repository.create(configuration, dto);

        user.getAll().forEach(e ->
            debt.cost(configuration, GUIDs.parse(e), GUIDs.parse(dto)));
    }

    /**
     * Получение долгов пользователя.
     *
     * @param person Пользователь.
     * @return Список долгов.
     */
    public Page<Event> getDebts(final UUID person) {
        return repository.getDebts(person);
    }
}
