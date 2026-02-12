package com.dev.analysis.api.config;

import com.dev.analysis.support.error.ErrorType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI analysisOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Analysis API")
                        .version("v1")
                        .description("CSV 분석 API 문서")
                );
    }

    @Bean
    public OperationCustomizer endpointErrorResponseCustomizer() {
        return (operation, handlerMethod) -> {
            List<ErrorType> errorTypes = extractErrorType(handlerMethod);

            if (errorTypes.isEmpty()) {
                return operation;
            }

            ApiResponses responses = operation.getResponses();
            if (responses == null) {
                responses = new ApiResponses();
                operation.setResponses(responses);
            }

            Map<HttpStatus, List<ErrorType>> errorTypeListMap = groupByStatus(errorTypes);

            for (Map.Entry<HttpStatus, List<ErrorType>> entry : errorTypeListMap.entrySet()) {
                HttpStatus status = entry.getKey();

                ApiResponse content = new ApiResponse()
                        .description(status.getReasonPhrase())
                        .content(errorContent(entry.getValue()));

                responses.putIfAbsent(String.valueOf(status.value()), content);
            }

            return operation;
        };
    }

    private List<ErrorType> extractErrorType(HandlerMethod handlerMethod) {
        ErrorDocumented classDoc = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), ErrorDocumented.class);
        ErrorDocumented methodDoc = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), ErrorDocumented.class);

        return Stream.of(classDoc, methodDoc)
                .filter(Objects::nonNull)
                .flatMap(errorDocumented -> Arrays.stream(errorDocumented.value()))
                .distinct()
                .toList();
    }

    private Map<HttpStatus, List<ErrorType>> groupByStatus(List<ErrorType> errorTypes) {
        return errorTypes.stream()
                .sorted(Comparator.comparingInt(it -> it.getHttpStatus().value()))
                .collect(Collectors.groupingBy(
                        ErrorType::getHttpStatus,
                        LinkedHashMap::new,
                        Collectors.toList())
                );
    }

    private Content errorContent(List<ErrorType> errorTypes) {
        MediaType mediaType = new MediaType();
        Map<String, Example> examples = new LinkedHashMap<>();

        for (ErrorType errorType : errorTypes) {
            examples.put(
                    errorType.name(),
                    new Example()
                            .summary(errorType.name())
                            .value(errorExample(errorType))
            );
        }

        mediaType.setExamples(examples);
        return new Content().addMediaType("application/json", mediaType);
    }

    private Map<String, Object> errorExample(ErrorType errorType) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("statusCode", errorType.getHttpStatus().value());
        error.put("message", errorType.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("resultType", "ERROR");
        body.put("data", null);
        body.put("error", error);
        return body;
    }
}
