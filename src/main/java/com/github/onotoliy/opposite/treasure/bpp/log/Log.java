package com.github.onotoliy.opposite.treasure.bpp.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация реализующая логику логирования вызова функции.
 *
 * @author Anatoliy Pokhresnyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface Log {
    /**
     * Получает уровень логирования.
     *
     * @return Уровень логирования.
     */
    LogLevel level() default LogLevel.DEBUG;

    /**
     * Получает флаг записи лога в БД.
     *
     * @return Флаг записи лога в БД.
     */
    boolean db() default false;
}
