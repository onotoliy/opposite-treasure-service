package com.github.onotoliy.opposite.treasure.bpp.log;

import com.github.onotoliy.opposite.treasure.services.core.DBLoggerService;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor аннотации {@link Log}.
 *
 * @author Anatoliy Pokhresnyi
 */
@Component
public class LogAnnotationBeanPostProcessor implements BeanPostProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(LogAnnotationBeanPostProcessor.class);

    /**
     * Карта Bean-ов которые необходимо перехватывать.
     */
    private final Map<String, Class<?>> beans = new HashMap<>();

    /**
     * Сервис логирования.
     */
    private final DBLoggerService dbLogger;

    /**
     * Кноструктор.
     *
     * @param dbLogger Сервис логирования.
     */
    @Autowired
    public LogAnnotationBeanPostProcessor(final DBLoggerService dbLogger) {
        this.dbLogger = dbLogger;
    }

    @Override
    public Object postProcessBeforeInitialization(
        final Object bean,
        final @NotNull String beanName
    ) throws BeansException {
        Class<?> clazz = bean.getClass();

        if (isAnnotationPresent(clazz)) {
            LOGGER.info(
                "Bean {} constraint annotation @Log",
                clazz.getCanonicalName()
            );

            beans.put(beanName, bean.getClass());
        }

        return bean;
    }

    /**
     * Проверка содержит ли класс аннотацию {@link Log}.
     *
     * @param clazz Класс.
     * @return Результат проверки.
     */
    private boolean isAnnotationPresent(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Log.class)) {
            return true;
        }

        boolean isAnnotationPresent = Arrays
            .stream(clazz.getMethods())
            .anyMatch(m -> m.isAnnotationPresent(Log.class));

        if (isAnnotationPresent) {
            return true;
        } else {
            return Arrays
                .stream(clazz.getInterfaces())
                .anyMatch(this::isAnnotationPresent);
        }
    }

    @Override
    public Object postProcessAfterInitialization(
        final @NotNull Object bean,
        final @NotNull String beanName
    ) throws BeansException {
        Class<?> original = beans.get(beanName);

        return original == null
            ? bean
            : Proxy.newProxyInstance(
                original.getClassLoader(),
                original.getInterfaces(),
                new LogInvocationHandler(original, bean, dbLogger));
    }
}
