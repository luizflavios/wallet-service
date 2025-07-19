package com.recargapay.wallet_service.core.exception;

import com.recargapay.wallet_service.core.exception.model.ApplicationErrorResponse;
import com.recargapay.wallet_service.domain.wallet.exception.AlreadyExistsWalletException;
import com.recargapay.wallet_service.domain.wallet.exception.IllegalTransferException;
import com.recargapay.wallet_service.domain.wallet.exception.InsufficientBalanceException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(AlreadyExistsWalletException.class)
    public ResponseEntity<ApplicationErrorResponse> alreadyExistsWalletExceptionHandler(AlreadyExistsWalletException exception) {
        final var applicationErrorResponse = ApplicationErrorResponse.builder()
                .status(CONFLICT)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();

        return new ResponseEntity<>(applicationErrorResponse, applicationErrorResponse.status());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        final var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .distinct()
                .collect(joining("; "));

        final var applicationErrorResponse = ApplicationErrorResponse.builder()
                .status(BAD_REQUEST)
                .error(exception.getClass().getSimpleName())
                .detail(errors)
                .build();

        return new ResponseEntity<>(applicationErrorResponse, applicationErrorResponse.status());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApplicationErrorResponse> entityNotFoundExceptionHandler(EntityNotFoundException exception) {
        final var applicationErrorResponse = ApplicationErrorResponse.builder()
                .status(NOT_FOUND)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();

        return new ResponseEntity<>(applicationErrorResponse, applicationErrorResponse.status());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApplicationErrorResponse> insufficientBalanceExceptionHandler(InsufficientBalanceException exception) {
        final var applicationErrorResponse = ApplicationErrorResponse.builder()
                .status(UNPROCESSABLE_ENTITY)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();

        return new ResponseEntity<>(applicationErrorResponse, applicationErrorResponse.status());
    }

    @ExceptionHandler(IllegalTransferException.class)
    public ResponseEntity<ApplicationErrorResponse> illegalTransferExceptionHandler(IllegalTransferException exception) {
        final var applicationErrorResponse = ApplicationErrorResponse.builder()
                .status(UNPROCESSABLE_ENTITY)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();

        return new ResponseEntity<>(applicationErrorResponse, applicationErrorResponse.status());
    }

}
