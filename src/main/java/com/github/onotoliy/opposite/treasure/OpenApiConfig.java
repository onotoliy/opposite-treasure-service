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
//            final Annotation[] annotations = handlerMethod.getMethod().getAnnotations();
            final String clazz = handlerMethod.getBeanType().getSimpleName();
            final String path = handlerMethod.getMethod().getName();

//            final StringBuilder sb = new StringBuilder(getMethod(annotations));
//            final String[] parts = path
//                    .replaceAll("[{}]", "").replaceAll("^/", "").split("/");
//
//            for (String part : parts) {
//                if (!part.isBlank()) {
//                    sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
//                }
//            }
//            sb.append(clazz);

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

