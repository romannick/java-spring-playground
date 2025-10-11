package com.nikolayr.playground.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBodyAtLeastOneFieldException extends RuntimeException {
    public InvalidBodyAtLeastOneFieldException(List<String> fields) {
        super("At least one of the following fields must be present: " +
                fields.stream()
                        .map(f -> "'" + f + "'")
                        .collect(Collectors.joining(", ")));
    }
}