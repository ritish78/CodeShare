package com.codesharing.platform.codeshareplatform.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Entity
public class Code {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String body;
    private String dateTime;

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public Code() {
        /**
         * This constructor is used by JPA. When we save the code into the
         * database, we are assigning date and time to current date. So,
         * we are assigning date and time to each column in database to current time.
         */
       this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
    }

    public Code(String body) {
        this.body = body;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
    }

    public Code(String body, String dateTime) {
        this.body = body;
        this.dateTime = dateTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\": \"" + body + "\"" +
                "}";
    }
}
