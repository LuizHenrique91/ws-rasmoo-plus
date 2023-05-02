package com.client.ws.rasmooplus.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Map.Entry<String, String>> listError;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    private HttpStatus httpStatus;

    private Integer statusCode;
}
