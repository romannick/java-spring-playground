package com.nikolayr.playground.shared.dto.response;

import com.nikolayr.playground.shared.types.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
    private ErrorStatus status;
    private String error;
}