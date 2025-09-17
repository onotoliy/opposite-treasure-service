package com.github.onotoliy.opposite.treasure;

import com.github.onotoliy.opposite.treasure.data.core.ExceptionInformation;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.HandlerMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
     * Получение Bean переопределения OpenApiCustomizer в openapi файле.
     *
     * @return Bean.
     */
    @Bean
    public OpenApiCustomizer exceptionInformationSchemaCustomizer() {
        return openApi -> {
            Map<String, Schema> schemas =
                ModelConverters.getInstance().read(ExceptionInformation.class);
            openApi.getComponents().getSchemas().putAll(schemas);
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
        final Consumer<ApiResponses> s400 = responses -> {
            responses.addApiResponse("400", new ApiResponse()
                .description("Неверный запрос (Bad Request)")
                .content(content));
        };
        final Consumer<ApiResponses> s404 = responses -> {
            responses.addApiResponse("404", new ApiResponse()
                .description("Не найдено (Not Found)")
                .content(content));
        };
        final Consumer<ApiResponses> s500 = responses -> {
            responses.addApiResponse("500", new ApiResponse()
                .description("Ошибка сервера (Internal Server Error)")
                .content(content));
        };
        final Consumer<ApiResponses> s409 = responses -> {
            responses.addApiResponse("409", new ApiResponse()
                .description("Конфликт (Conflict)")
                .content(content));
        };

        return (operation, handlerMethod) -> {
            ApiResponses responses = operation.getResponses();

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(GetMapping.class))) {
                s400.accept(responses);
                s404.accept(responses);
                s500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(PostMapping.class))) {
                s409.accept(responses);
                s400.accept(responses);
                s404.accept(responses);
                s500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(PutMapping.class))) {
                s409.accept(responses);
                s400.accept(responses);
                s404.accept(responses);
                s500.accept(responses);
            }

            if (Objects.nonNull(handlerMethod.getMethodAnnotation(DeleteMapping.class))) {
                s400.accept(responses);
                s404.accept(responses);
                s500.accept(responses);
            }

            return operation;
        };
    }
}

