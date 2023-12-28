package com.example.group_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class UserVo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long updatedBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date createdDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date updatedDate;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
