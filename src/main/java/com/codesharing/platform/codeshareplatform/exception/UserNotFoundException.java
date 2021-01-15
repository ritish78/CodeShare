package com.codesharing.platform.codeshareplatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserNotFoundException extends RuntimeException {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public UserNotFoundException() {
        logger.error("Unknown User error occurred on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
    }

    public UserNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
