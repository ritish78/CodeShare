package com.codesharing.platform.codeshareplatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Time limit exceeded")
public class TimeExceededException extends RuntimeException {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-ss HH:mm:ss";

    public TimeExceededException() {
        logger.info("Time limit exceeded for code on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
    }

    public TimeExceededException(String message) {
        super(message);
        logger.info(message);
    }
}
