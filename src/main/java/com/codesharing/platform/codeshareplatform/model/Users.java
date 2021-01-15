package com.codesharing.platform.codeshareplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Component
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    //@Column(name = "iduser", unique = true, nullable = false)
    private Long id;

    @JsonIgnore
    private String uuid;

    private String username;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Code> codeList;

    public Users() {
        this.uuid = UUID.randomUUID().toString();
    }


    public Users(String username, String email, String password) {
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Users(String uuid, String username, String email, String password) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        if (this.id == null) {
            return 0L;
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
