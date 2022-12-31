package io.github.MichaelAnderson19.TodoAPI.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("");
    }
    public InvalidCredentialsException(String message) {
        super(message);
    }

}
