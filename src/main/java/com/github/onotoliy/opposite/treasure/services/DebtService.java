package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Debt;
import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.repositories.DebtRepository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(DebtService.class);

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

    /**
     * Получение должников.
     *
     * @param event Событие.
     * @return Список должников.
     */
    public Page<Deposit> getDebtors(final UUID event) {
        return repository.getDebtors(event);
    }

    /**
     * Получение версии сущности.
     *
     * @return Версия сущности.
     */
    public Option version() {
        return repository.version();
    }

    /**
     * Данные, которые необходимо синхронизировать.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Данные, которые необходимо синхронизировать.
     */
    public Page<Debt> sync(final int offset, final int numberOfRows) {
        LOGGER.info("Sync offset {} numberOfRows {}", offset, numberOfRows);

        return repository.sync(offset, numberOfRows);
    }
}
