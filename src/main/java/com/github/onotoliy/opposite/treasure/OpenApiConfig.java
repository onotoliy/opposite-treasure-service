package com.github.onotoliy.opposite.treasure;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import io.swagger.v3.oas.models.Operation;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Optional;

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer customOperationId() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final String clazz = handlerMethod.getBeanType().getSimpleName();
            final String path = handlerMethod.getMethod().getName();

            operation.setOperationId(path + clazz);

            return operation;
        };
    }

    private String getPath(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann instanceof GetMapping m && m.value().length > 0) return m.value()[0];
            if (ann instanceof PostMapping m && m.value().length > 0) return m.value()[0];
            if (ann instanceof PutMapping m && m.value().length > 0) return m.value()[0];
            if (ann instanceof DeleteMapping m && m.value().length > 0) return m.value()[0];
            if (ann instanceof RequestMapping m && m.value().length > 0) return m.value()[0];
        }
        return null;
    }

    private String getMethod(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann instanceof GetMapping) return "get";
            if (ann instanceof PostMapping) return "post";
            if (ann instanceof PutMapping) return "put";
            if (ann instanceof DeleteMapping) return "delete";
        }
        return "method";
    }
}

