package io.github.MichaelAnderson19.TodoAPI.exception;

public class UserNotFoundException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
