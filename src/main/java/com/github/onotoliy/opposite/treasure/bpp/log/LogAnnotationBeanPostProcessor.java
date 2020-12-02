package com.github.onotoliy.opposite.treasure.bpp.log;

import com.github.onotoliy.opposite.treasure.services.core.DBLoggerService;
import com.github.onotoliy.opposite.treasure.utils.GUIDs;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context == null
            ? null : context.getAuthentication();
        Object object = authentication == null
            ? null : authentication.getPrincipal();
        KeycloakPrincipal principal = object == null
            ? null : (KeycloakPrincipal) object;
        UUID author = principal == null
            ? null : GUIDs.parse(principal.getName());

        return original == null
            ? bean
            : Proxy.newProxyInstance(
                original.getClassLoader(),
                original.getInterfaces(),
                new LogInvocationHandler(original, bean, author, dbLogger));
    }
}
