package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.data.core.ExceptionDevice;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.github.onotoliy.opposite.treasure.jooq.Tables.TREASURE_EXCEPTION;

/**
 * Репозиторий управления ошибками устройства.
 *
 * @author Anatoliy Pokhresnyi
 */
@Repository
public class ExceptionRepository {

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
    public ExceptionRepository(final DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Регистрация ошибки устройства.
     *
     * @param exception Описание ошибки устройства.
     */
    public void registration(final ExceptionDevice exception) {
        dsl.insertInto(TREASURE_EXCEPTION)
           .set(TREASURE_EXCEPTION.GUID, GUIDs.random())
           .set(TREASURE_EXCEPTION.AGENT, exception.getAgent())
           .set(TREASURE_EXCEPTION.DEVICE, exception.getDevice())
           .set(TREASURE_EXCEPTION.MESSAGE, exception.getMessage())
           .set(TREASURE_EXCEPTION.LOCALIZED_MESSAGE,
                exception.getLocalizedMessage())
           .set(TREASURE_EXCEPTION.STACK_TRACE, exception.getStackTrace())
           .execute();
    }

}
