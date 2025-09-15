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

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer customOperationId() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String httpMethod = handlerMethod.getMethod().getName();

            System.out.println("Method " + httpMethod);

            String path = getPath(handlerMethod.getMethod().getAnnotations());

            System.out.println("Path " + path);

            if (path == null) {
                path = handlerMethod.getMethod().getName();
            }

            String cleaned = path.replaceAll("[{}]", "").replaceAll("^/", "");
            String[] parts = cleaned.split("/");

            StringBuilder sb = new StringBuilder(httpMethod);
            for (String part : parts) {
                if (!part.isBlank()) {
                    sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
                }
            }

            operation.setOperationId(sb.toString());
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
}

