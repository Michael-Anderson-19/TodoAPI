package io.github.MichaelAnderson19.TodoAPI.exception.handler;

//import io.github.MichaelAnderson19.TodoAPI.exception.response.ErrorResponse;

import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {TodoItemNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseBody handleEntityNotFoundException(TodoItemNotFoundException exception) {
        return new ErrorResponseBody(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //redundant
    public ErrorResponseBody handleJPAViolations(TransactionSystemException exception) {
        ErrorResponseBody responseBody = new ErrorResponseBody(HttpStatus.BAD_REQUEST, "Could not save changes");
        if (exception.getCause().getCause() instanceof ConstraintViolationException violationException) {
            List<String> errorMessages = new ArrayList<>();
            violationException.getConstraintViolations().stream()
                    .map(violation ->
                            "%s %s".formatted(violation.getPropertyPath(), violation.getMessage())
                    ).collect(Collectors.toList());
            responseBody.setMessage(String.join(", ", errorMessages));
        }
        return responseBody;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseBody handleBindErrors(MethodArgumentNotValidException exception) {
        List<String> errorList = exception.getFieldErrors().stream()
                .map(error -> "%s %s".formatted(error.getField(), error.getDefaultMessage())).collect(Collectors.toList());
        String errorMessage = errorList.size() > 0 ? String.join(", ", errorList) : "Passed data not valid";
        return new ErrorResponseBody(HttpStatus.BAD_REQUEST,
                errorMessage);
    }

    @Data
    public static class ErrorResponseBody {
        private int status;
        private LocalDateTime timestamp;
        private String error;
        private String message;

        public ErrorResponseBody(int status, String error, String message, LocalDateTime timestamp) {
            this.status = status;
            this.timestamp = timestamp; //LocalDateTime.now();
            this.error = error;
            this.message = message;
        }

        public ErrorResponseBody(HttpStatus status, String message) {
            this(status.value(), status.name(), message, LocalDateTime.now());
        }

    }


}
