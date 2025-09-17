package com.github.onotoliy.opposite.treasure;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import jakarta.ws.rs.DELETE;
import java.util.Objects;
import java.util.function.Consumer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.HandlerMethod;

import static org.springframework.http.MediaType.*;

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

    /**
     * Получение Bean переопределения ApiResponses в openapi файле.
     *
     * @return Bean.
     */
    @Bean
    public OperationCustomizer globalGetResponsesCustomizer() {
        final Content content = new Content()
            .addMediaType(
                APPLICATION_JSON_VALUE,
                new MediaType().schema(
                    new Schema<>().$ref(
                        "#/components/schemas/ExceptionInformation"
                    )
                )
            );
        final Consumer<ApiResponses> _400 = responses -> {
            responses.addApiResponse("400", new ApiResponse()
                .description("Неверный запрос (Bad Request)")
                .content(content));
        };
        final Consumer<ApiResponses> _404 = responses -> {
            responses.addApiResponse("404", new ApiResponse()
                .description("Не найдено (Not Found)")
                .content(content));
        };
        final Consumer<ApiResponses> _500 = responses -> {
            responses.addApiResponse("500", new ApiResponse()
                .description("Ошибка сервера (Internal Server Error)")
                .content(content));
        };
        final Consumer<ApiResponses> _409 = responses -> {
            responses.addApiResponse("409", new ApiResponse()
                .description("Конфликт (Conflict)")
                .content(content));
        };

        return (operation, handlerMethod) -> {
            ApiResponses responses = operation.getResponses();

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(GetMapping.class))) {
                _400.accept(responses);
                _404.accept(responses);
                _500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(PostMapping.class))) {
                _409.accept(responses);
                _400.accept(responses);
                _404.accept(responses);
                _500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(PutMapping.class))) {
                _409.accept(responses);
                _400.accept(responses);
                _404.accept(responses);
                _500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(DeleteMapping.class))) {
                _400.accept(responses);
                _404.accept(responses);
                _500.accept(responses);
            }

            return operation;
        };
    }
}

