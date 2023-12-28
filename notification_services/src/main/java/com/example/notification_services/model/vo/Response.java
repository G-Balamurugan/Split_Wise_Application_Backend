package com.example.notification_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String status;

    public Response() {}
    public Response(long id , String status){
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
