package com.example.expense_services.model.vo;

public class UserVo {
    private long id;
    private long userId;
    private String userName;


    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

