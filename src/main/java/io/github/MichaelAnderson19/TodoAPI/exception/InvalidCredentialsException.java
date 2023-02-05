package io.github.MichaelAnderson19.TodoAPI.exception;

public class InvalidCredentialsException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    
    public InvalidCredentialsException(String message) {
        super(message);
    }

}
