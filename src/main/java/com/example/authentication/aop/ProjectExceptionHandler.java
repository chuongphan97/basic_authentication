package com.example.authentication.aop;


import com.example.authentication.common.response.APIResponseError;
import com.example.authentication.exception.BadRequestException;
import com.example.authentication.exception.NotFoundException;
import com.example.authentication.utils.constants.APIConstants;
import com.example.authentication.utils.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ProjectExceptionHandler {

    private final Logger log;

    public ProjectExceptionHandler() {
        log = LoggerFactory.getLogger(LoggingAspect.class);
    }

    @ExceptionHandler(value = {com.example.authentication.exception.AuthenticationException.class})
    protected ResponseEntity<APIResponseError> handleAuthenticationException(com.example.authentication.exception.AuthenticationException e) {
        if (log.isErrorEnabled()) {
            if (e.isPrintStackTrace()) {
                log.error(e.getMessage(), e);
            } else {
                log.error(Constants.LOGGING_MESSAGE_PATTERN_KEY, e.getError(), e.getMessage());
            }
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(e.getError())
                .message(e.getMessage())
                .build();
        if (log.isWarnEnabled()) {
            log.warn(String.format("%s%s%s", Constants.LOG_LEVEL_WARN, Constants.COLON, e.getMessage()));
        }
        return new ResponseEntity<>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<APIResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<String> msgList = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> msgList.add(error.getDefaultMessage()));

        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message(msgList.toString().replace("[", Constants.EMPTY).replace("]", Constants.EMPTY))
                .build();

        return new ResponseEntity<>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<APIResponseError> handleNotFoundException(NotFoundException e) {
        if (log.isErrorEnabled()) {
            if (e.isPrintStackTrace()) {
                log.error(e.getMessage(), e);
            } else {
                log.error(Constants.LOGGING_MESSAGE_PATTERN_KEY, e.getError(), e.getMessage());
            }
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<APIResponseError> handleBadRequestException(BadRequestException e) {
        if (log.isErrorEnabled()) {
            if (e.isPrintStackTrace()) {
                log.error(e.getMessage(), e);
            } else {
                log.error(Constants.LOGGING_MESSAGE_PATTERN_KEY, e.getError(), e.getMessage());
            }
        }
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<APIResponseError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponseError> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : APIConstants.ERROR_UNKNOWN_MSG)
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler DisabledException.
     *
     * @param e DisabledException exception
     * @return error response
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<APIResponseError> handleDisabledException(DisabledException e) {
        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(com.example.authentication.exception.AuthenticationException.UNAUTHORIZED_USER_BLOCKED)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.NOT_FOUND);
    }
}
