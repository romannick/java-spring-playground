package com.nikolayr.playground.shared.dto.response;

import com.nikolayr.playground.shared.types.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private ErrorStatus status;
    private Map<String, String> errors;
}