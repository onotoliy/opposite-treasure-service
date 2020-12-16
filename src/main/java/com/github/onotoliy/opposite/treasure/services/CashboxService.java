package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Cashbox;
import com.github.onotoliy.opposite.treasure.repositories.CashboxRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис чтения данных о кассе.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class CashboxService implements ICashboxService {

    /**
     * Репозиторий.
     */
    private final CashboxRepository repository;

    /**
     * Конструтор.
     *
     * @param repository Репозиторий.
     */
    @Autowired
    public CashboxService(final CashboxRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cashbox get() {
        return repository.find();
    }
}
