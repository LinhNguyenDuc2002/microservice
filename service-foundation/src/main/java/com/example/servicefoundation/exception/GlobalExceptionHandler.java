package com.example.servicefoundation.exception;

import com.example.servicefoundation.base.response.Error;
import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.constant.Status;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private I18nService i18nService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        Response<Object> response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();

        if (ex.hasFieldErrors()) {
            for (FieldError error : ex.getFieldErrors()) {
                errors.add(
                        Error.builder()
                                .field(StringUtil.camelCaseToSnakeCase(error.getField()))
                                .message(error.getDefaultMessage())
                                .build()
                );
            }
            response.setData(errors);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Response response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .message(
                        i18nService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale())
                )
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(I18nException.class)
    public ResponseEntity<Object> handleNotFoundException(I18nException ex) {
        Response<Object> response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(ex.getCode().value())
                .message(
                        i18nService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale())
                )
                .build();
        return ResponseEntity.status(ex.getCode()).body(response);
    }
}
