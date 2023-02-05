package io.github.MichaelAnderson19.TodoAPI.exception;

public class TodoItemNotFoundException extends RuntimeException {
    
    private static final Long serialVersionUID = 1L;

    public TodoItemNotFoundException(String message) {
        super(message);
    }
}
