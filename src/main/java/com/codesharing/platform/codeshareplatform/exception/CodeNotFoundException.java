package com.codesharing.platform.codeshareplatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "code not found")
public class CodeNotFoundException extends RuntimeException{

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public CodeNotFoundException() {
        logger.error("Unknown error occurred on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
    }

    public CodeNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
