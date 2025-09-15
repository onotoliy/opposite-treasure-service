package com.github.onotoliy.opposite.treasure;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

/**
 * Класс описывающий переопределение operationId в openapi файле.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Получение Bean переопределения operationId в openapi файле.
     *
     * @return Bean.
     */
    @Bean
    public OperationCustomizer customOperationId() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final String clazz = handlerMethod.getBeanType().getSimpleName();
            final String path = handlerMethod.getMethod().getName();

            operation.setOperationId(path + clazz);

            return operation;
        };
    }
}

