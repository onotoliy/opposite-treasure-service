package com.github.onotoliy.opposite.treasure.services.core;

import com.github.onotoliy.opposite.treasure.repositories.core.DBLoggerRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис логирования.
 *
 * @author Anatoliy Pokhresnyi
 */
@Service
public class DBLoggerService {

    /**
     * Репозиторий логирования.
     */
    private final DBLoggerRepository repository;

    /**
     * Конструктор.
     *
     * @param repository Репозиторий логирования.
     */
    @Autowired
    public DBLoggerService(final DBLoggerRepository repository) {
        this.repository = repository;
    }

    /**
     * Логирование события уровня INFO.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     */
    public void info(final UUID author,
                     final Class<?> clazz,
                     final String message) {
        repository.info(author, clazz, message);
    }

    /**
     * Логирование события уровня TRACE.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     */
    public void trace(final UUID author,
                      final Class<?> clazz,
                      final String message) {
        repository.trace(author, clazz, message);
    }

    /**
     * Логирование события уровня WARN.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     */
    public void warn(final UUID author,
                     final Class<?> clazz,
                     final String message) {
        repository.warn(author, clazz, message);
    }

    /**
     * Логирование события уровня DEBUG.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     */
    public void debug(final UUID author,
                      final Class<?> clazz,
                      final String message) {
        repository.debug(author, clazz, message);
    }

    /**
     * Логирование события уровня ERROR.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     */
    public void error(final UUID author,
                      final Class<?> clazz,
                      final String message) {
        repository.error(author, clazz, message);
    }

    /**
     * Логирование события уровня ERROR.
     *
     * @param author Автор.
     * @param clazz Класс.
     * @param message Сообщение.
     * @param throwable Ошибка.
     */
    public void error(final UUID author,
                      final Class<?> clazz,
                      final String message,
                      final Throwable throwable) {
        repository.error(author, clazz, message, throwable);
    }
}
