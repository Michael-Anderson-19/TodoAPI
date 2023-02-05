package io.github.MichaelAnderson19.TodoAPI.exception;

public class UserAlreadyExistsException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
