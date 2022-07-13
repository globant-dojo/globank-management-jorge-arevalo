package com.bank.management.challenge.infrastructure.config.exception;

import com.bank.management.challenge.infrastructure.rest.output.FormatMessage;
import com.bank.management.challenge.infrastructure.rest.output.FormatOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(GeneralValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<FormatOutput<Object>> handleGeneralValidationException(
            GeneralValidationException exception,
            WebRequest request) {
        log.error(exception.toString());
        FormatOutput<Object> formatOutput = new FormatOutput<>();
        formatOutput.setMessages(exception.getMessages());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatOutput);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<FormatMessage> messages = ex.getBindingResult().getFieldErrors().stream().map(
                x -> new FormatMessage(x.getField(), x.getDefaultMessage())
        ).collect(Collectors.toList());
        FormatOutput<Object> formatOutput = new FormatOutput<>();
        formatOutput.setMessages(messages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatOutput);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<FormatOutput<Object>> handleItemNotFoundException(
            NotFoundException exception,
            WebRequest request) {
        log.error(exception.getMessage(), exception);
        FormatMessage message = new FormatMessage(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                exception.getMessage());
        FormatOutput<Object> formatOutput = new FormatOutput<>();
        formatOutput.setMessages(Arrays.asList(message));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formatOutput);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<FormatOutput<Object>> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request) {
        log.error(exception.getMessage(), exception);
        FormatMessage message = new FormatMessage(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Error Message: " + exception.getMessage());
        FormatOutput<Object> formatOutput = new FormatOutput<>();
        formatOutput.setMessages(Arrays.asList(message));
        return ResponseEntity.internalServerError().body(formatOutput);
    }
}
