package com.example.spring.boot.config;

import com.example.spring.boot.domain.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by yuweijun on 2018-07-06.
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<HttpResult<String>> handleServerError(RuntimeException ex, WebRequest request) {
        LOGGER.error(ex.getMessage());
        HttpResult<String> httpResult = new HttpResult<>();
        httpResult.setStatus(500);
        httpResult.setMessage("server internal error");
        return new ResponseEntity<>(httpResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}