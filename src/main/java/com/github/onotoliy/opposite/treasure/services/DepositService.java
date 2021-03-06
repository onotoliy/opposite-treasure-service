package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.DepositSearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис чтения депозитов.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DepositService {

    /**
     * Репозиторий.
     */
    private final DepositRepository repository;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий.
     */
    @Autowired
    public DepositService(final DepositRepository repository) {
        this.repository = repository;
    }

    /**
     * Получениие депозита.
     *
     * @param uuid Уникальный идентификатор.
     * @return Депозит.
     */
    public Deposit get(final UUID uuid) {
        return repository.get(uuid);
    }

    /**
     * Поиск депозитов.
     *
     * @param parameter Поисковые параметры.
     * @return Депозиты.
     */
    public Page<Deposit> getAll(final DepositSearchParameter parameter) {
        return repository.getAll(parameter);
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
    public Page<Deposit> sync(final int offset, final int numberOfRows) {
        return repository.sync(offset, numberOfRows);
    }
}
