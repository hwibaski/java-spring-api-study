package com.cafe.controllerAdvice;

import com.cafe.dto.ApiResponse;
import com.cafe.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionAdvice {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            CustomException ex) {

        var apiResponse = ApiResponse.createErrorResponse(ex.getMessage(), ex.getCode());

        return ResponseEntity
                .status(ex.getStatus())
                .body(apiResponse);
    }
}
