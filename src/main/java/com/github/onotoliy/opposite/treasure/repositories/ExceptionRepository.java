package com.github.onotoliy.opposite.treasure.repositories;

import com.github.onotoliy.opposite.treasure.data.core.ExceptionDevice;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
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
           .set(TREASURE_EXCEPTION.AGENT, exception.agent())
           .set(TREASURE_EXCEPTION.DEVICE, exception.device())
           .set(TREASURE_EXCEPTION.MESSAGE, exception.message())
           .set(TREASURE_EXCEPTION.LOCALIZED_MESSAGE,
                exception.localizedMessage())
           .set(TREASURE_EXCEPTION.STACK_TRACE, exception.stackTrace())
           .execute();
    }

}
