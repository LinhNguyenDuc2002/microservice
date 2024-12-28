package com.example.orderservice.exception;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.Status;
import com.example.orderservice.dto.Error;
import com.example.orderservice.dto.response.Response;
import com.example.orderservice.i18n.I18nService;
import com.example.orderservice.util.StringUtil;
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
                .message(
                        i18nService.getMessage(I18nMessage.ERROR_DATA_INVALID, LocaleContextHolder.getLocale())
                )
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        Response<Object> response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .message(
                        i18nService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale())
                )
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidationException.class)
    public ResponseEntity<Object> handleInvalidationException(InvalidationException ex) {
        Response response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .message(
                        i18nService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale())
                )
                .build();
        response.setData(ex.getErrorObject());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle the Access Denied Exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
        Response response = Response.builder()
                .status(Status.FAIL.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(
                        i18nService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale())
                )
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
