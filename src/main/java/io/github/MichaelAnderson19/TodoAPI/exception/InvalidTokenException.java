package io.github.MichaelAnderson19.TodoAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    public InvalidTokenException(String message) {
        super(message);
    }
}
