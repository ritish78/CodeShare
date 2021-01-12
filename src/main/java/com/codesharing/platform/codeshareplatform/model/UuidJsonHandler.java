package com.codesharing.platform.codeshareplatform.model;

public class UuidJsonHandler {

    private String uuid;

    public UuidJsonHandler(String uuid) {
        this.uuid = uuid;
    }

    public UuidJsonHandler() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UuidJsonHandler{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
