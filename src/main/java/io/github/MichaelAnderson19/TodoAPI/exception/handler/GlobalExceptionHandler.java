package io.github.MichaelAnderson19.TodoAPI.exception.handler;

//import io.github.MichaelAnderson19.TodoAPI.exception.response.ErrorResponse;

import io.github.MichaelAnderson19.TodoAPI.exception.TodoItemNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {TodoItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorReponse handleEntityNotFoundException(TodoItemNotFoundException exception, WebRequest request) {
        return new ErrorReponse(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.getMessage());
    }
    /*
      @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.FORBIDDEN.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
     */

    //refreshtoken fail unauthroiszed
//    private ErrorResponse buildErrorResponse(
//            Exception exception, HttpStatus httpStatus,WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), LocalDateTime.now(), exception.getMessage());
//        return errorResponse;
//    }

    @Data
    @RequiredArgsConstructor
    public static class ErrorReponse {
        private final int status;
        private final LocalDateTime time;
        private final String message;
    }

}
