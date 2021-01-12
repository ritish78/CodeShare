package com.codesharing.platform.codeshareplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@Entity
public class Code {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    private String body;
    private String dateTime;

    //Added these fields later in development
    @JsonIgnore
    private String uuid;
    private Integer viewsLeft;
    private Long timeInSeconds;


    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public Code() {
        /**
         * This constructor is used by JPA. When we save the code into the
         * database, we are assigning date and time to current date. So,
         * we are assigning date and time to each column in database to current time.
         */
       this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        /**
         * Generating a random UUID once this constructor is called.
         * Also, setting max views of 1 time and time left to 60 seconds
         * if we provide just body.
         */
       this.uuid = UUID.randomUUID().toString();
       this.viewsLeft = 1;
       this.timeInSeconds = 60L;
    }

    public Code(String body) {
        this.body = body;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        /**
         * Also, doing the same as in no arg constructor
         */
        this.uuid = UUID.randomUUID().toString();
        this.viewsLeft = 1;
        this.timeInSeconds = 60L;
    }

    public Code(String body, String dateTime) {
        /**
         * Also, doing the same as in no arg constructor
         */
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getViewsLeft() {
        return viewsLeft;
    }

    public void setViewsLeft(Integer viewsLeft) {
        this.viewsLeft = viewsLeft;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", uuid=" + uuid +
                ", views=" + viewsLeft +
                ", timeInSeconds=" + timeInSeconds +
                '}';
    }
}
