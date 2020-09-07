package com.github.onotoliy.opposite.treasure.bpp.log;

import com.github.onotoliy.opposite.treasure.rpc.KeycloakRPC;
import com.github.onotoliy.opposite.treasure.services.core.DBLoggerService;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
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
     * Карта Bean-ов которые необходимо перехватывать.
     */
    private final Map<String, Class<?>> beans = new HashMap<>();

    /**
     * Сервис логирования.
     */
    private final DBLoggerService dbLogger;

    /**
     * Сервис чтения данных о пользвателях из Keycloak.
     */
    private final KeycloakRPC users;

    /**
     * Кноструктор.
     *
     * @param dbLogger Сервис логирования.
     * @param users Сервис чтения данных о пользвателях из Keycloak.
     */
    @Autowired
    public LogAnnotationBeanPostProcessor(final DBLoggerService dbLogger,
                                          final KeycloakRPC users) {
        this.users = users;
        this.dbLogger = dbLogger;
    }

    @Override
    public Object postProcessBeforeInitialization(
        final Object bean,
        final @NotNull String beanName
    ) throws BeansException {
        Class<?> clazz = bean.getClass();

        boolean isPresent = Arrays
            .stream(clazz.getMethods())
            .anyMatch(m -> m.isAnnotationPresent(Log.class));

        if (clazz.isAnnotationPresent(Log.class) || isPresent) {
            beans.put(beanName, bean.getClass());
        }

        return bean;
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
                new LogInvocationHandler(original,
                                         bean,
                                         users.getAuthenticationUUID(),
                                         dbLogger
                ));
    }
}
