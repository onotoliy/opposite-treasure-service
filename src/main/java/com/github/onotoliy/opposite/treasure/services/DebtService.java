package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис чтения долгов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DebtService {

    /**
     * Репозиторий.
     */
    private final DebtRepository repository;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий.
     */
    @Autowired
    public DebtService(final DebtRepository repository) {
        this.repository = repository;
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
