package com.github.onotoliy.opposite.treasure.repositories.core;

import com.github.onotoliy.opposite.treasure.bpp.log.LogLevel;
import com.github.onotoliy.opposite.treasure.utils.Dates;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Throwables;

import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_LOG;

/**
 * Репозиторий логирования.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class DBLoggerRepository {

    /**
     * Контекст подключения к БД.
     */
    private final DSLContext dsl;

    /**
     * Конструктор.
     *
     * @param dsl Контекст подключения к БД.
     */
    @Autowired
    public DBLoggerRepository(final DSLContext dsl) {
        this.dsl = dsl;
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
        log(LogLevel.INFO, author, clazz, message, null);
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
        log(LogLevel.TRACE, author, clazz, message, null);
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
        log(LogLevel.WARN, author, clazz, message, null);
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
        log(LogLevel.DEBUG, author, clazz, message, null);
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
        log(LogLevel.ERROR, author, clazz, message, null);
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
        log(LogLevel.ERROR, author, clazz, message, throwable);
    }

    /**
     * Логирование события.
     *
     * @param level Уровень логирования.
     * @param author Автор.
     * @param clazz Класс
     * @param message Сообщение
     * @param throwable Ошибка.
     */
    private void log(final LogLevel level,
                     final UUID author,
                     final Class<?> clazz,
                     final String message,
                     final Throwable throwable) {
        dsl.insertInto(TREASURE_LOG)
           .set(TREASURE_LOG.GUID, GUIDs.random())
           .set(TREASURE_LOG.LEVEL, level.name())
           .set(TREASURE_LOG.AUTHOR, author)
           .set(TREASURE_LOG.BEAN_NAME, clazz.getCanonicalName())
           .set(TREASURE_LOG.MESSAGE, message)
           .set(TREASURE_LOG.CREATION_DATE, Dates.now())
           .set(TREASURE_LOG.STACK_TRACE, Throwables.format(throwable))
           .execute();
    }
}
