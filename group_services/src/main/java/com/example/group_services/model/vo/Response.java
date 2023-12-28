package com.example.group_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String userName;

    public Response() {}
    public Response(String status){ this.status = status; }
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
