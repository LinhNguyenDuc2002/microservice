package com.example.servicefoundation.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class I18nException extends Exception {
    private HttpStatusCode code;

    private Object object;

    private String message;
}
