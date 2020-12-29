package com.github.onotoliy.opposite.treasure.bpp.log;

import com.github.onotoliy.opposite.treasure.services.core.DBLoggerService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;
import com.github.onotoliy.opposite.treasure.utils.Objects;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Обработчик запросов логгирования.
 *
 * @author Anatoliy Pokhresnyi
 */
public class LogInvocationHandler implements InvocationHandler {

    /**
     * Список стандартных Logger-ов.
     */
    private static final Map<String, Logger> LOGGERS = new HashMap<>();

    /**
     * Bean.
     */
    private final Object bean;

    /**
     * Оригинальный тип Bean класса.
     */
    private final Class<?> obc;

    /**
     * Стандартный Logger.
     */
    private final Logger logger;

    /**
     * Logger записывающий в БД.
     */
    private final DBLoggerService dbLogger;

    /**
     * Конструктор.
     *
     * @param obc Оригинальный тип Bean класса.
     * @param bean Bean.
     * @param dbLogger Logger записывающий в БД.
     */
    public LogInvocationHandler(final Class<?> obc,
                                final Object bean,
                                final DBLoggerService dbLogger) {
        this.obc = obc;
        this.bean = bean;
        this.dbLogger = dbLogger;
        this.logger = LOGGERS.computeIfAbsent(
            obc.getCanonicalName(),
            s -> LoggerFactory.getLogger(obc)
        );
    }

    @Override
    public Object invoke(final Object proxy,
                         final Method method,
                         final Object[] args
    ) throws Throwable {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context == null
            ? null : context.getAuthentication();
        Object object = authentication == null
            ? null : authentication.getPrincipal();
        UUID author = UUID.fromString("00000000-0000-0000-0000-000000000001");

        if (Objects.nonEmpty(object)) {
            if (object instanceof KeycloakPrincipal) {
                author = GUIDs.parse(((KeycloakPrincipal) object).getName());
            }
        }

        Log annotation = obc
            .getMethod(method.getName(), method.getParameterTypes())
            .getAnnotation(Log.class);

        if (annotation == null) {
            annotation = obc.getAnnotation(Log.class);
        }

        if (annotation == null) {
            return method.invoke(bean, args);
        }

        String arguments = Arrays
            .stream(args)
            .map(Object::toString)
            .collect(Collectors.joining(", "));

        String message = String.format(
            "Author: %s. Service: %s. Method: %s. Arguments %s.",
            author, obc.getCanonicalName(), method.getName(), arguments
        );

        try {
            log(annotation.level(), author, annotation.db(), message);

            return method.invoke(bean, args);
        } catch (Exception exception) {
            dbLogger.error(author, obc, message, exception);

            throw exception;
        }
    }

    /**
     * Логгирование события.
     *
     * @param level Уровень логирования.
     * @param author Автор.
     * @param bd Записать лог в БД.
     * @param message Сообщение.
     */
    private void log(final LogLevel level,
                     final UUID author,
                     final boolean bd,
                     final String message) {
        switch (level) {
            case INFO:
                logger.info(message);

                if (bd) {
                    dbLogger.info(author, obc, message);
                }

                break;
            case ERROR:
                logger.error(message);

                if (bd) {
                    dbLogger.error(author, obc, message);
                }

                break;
            case TRACE:
                logger.trace(message);

                if (bd) {
                    dbLogger.trace(author, obc, message);
                }

                break;
            case WARN:
                logger.warn(message);

                if (bd) {
                    dbLogger.warn(author, obc, message);
                }

                break;
            default:
                logger.debug(message);

                if (bd) {
                    dbLogger.debug(author, obc, message);
                }

                break;
        }
    }
}
