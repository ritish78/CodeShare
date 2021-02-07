package com.codesharing.platform.codeshareplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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

    //To remove the 255 Character limit in database, setting columnDefinition to 'LongText'
    @Column(name = "body", columnDefinition = "LONGTEXT")
    private String body;
    private String dateTime;

    //Added these fields later in development
    @JsonIgnore
    private String uuid;
    private Integer viewsLeft;
    private Long timeInSeconds;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = true, updatable = false)
    private Users user;

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
         * Also, setting max views of 1 time and time left to 10 hours
         * (3600 seconds) if we provide just body
         */
       this.uuid = UUID.randomUUID().toString();
        this.viewsLeft = 1;
        this.timeInSeconds = 3600L;
    }

    public Code(String body) {
        this.body = body;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        /**
         * Also, doing the same as in no arg constructor
         */
        this.uuid = UUID.randomUUID().toString();
        this.viewsLeft = 1;
        this.timeInSeconds = 3600L;
    }

    public Code(String body, String dateTime, Integer viewsLeft, Long timeInSeconds) {
        /**
         * Also, doing the same as in no arg constructor
         */
        this.body = body;
        this.dateTime = dateTime;
        this.viewsLeft = viewsLeft;
        this.timeInSeconds = timeInSeconds;
        this.uuid = UUID.randomUUID().toString();
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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
