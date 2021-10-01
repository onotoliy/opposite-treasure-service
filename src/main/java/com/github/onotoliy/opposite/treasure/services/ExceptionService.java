package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.core.ExceptionDevice;
import com.github.onotoliy.opposite.treasure.repositories.ExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис управления ошибками устройства.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class ExceptionService implements IExceptionService {

    /**
     * Репозиторий ошибок устройств.
     */
    private final ExceptionRepository repository;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий ошибок устройств.
     */
    @Autowired
    public ExceptionService(final ExceptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registration(final ExceptionDevice exception) {
        repository.registration(exception);
    }
}
