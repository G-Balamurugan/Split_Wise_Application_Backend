package com.example.group_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class LoginVo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String userPassword;
    private boolean validity;                       // default is false but it need to hold the value
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long updatedBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date createdDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date updatedDate;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isValidity() { return validity; }

    public void setValidity(boolean validity) { this.validity = validity; }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }
}
