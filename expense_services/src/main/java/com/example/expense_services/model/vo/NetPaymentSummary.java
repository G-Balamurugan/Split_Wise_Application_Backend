package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class NetPaymentSummary {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String type;
    private double amount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<GroupPaymentSummary> groupSummaryList;

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<GroupPaymentSummary> getGroupSummaryList() { return groupSummaryList; }

    public void setGroupSummaryList(List<GroupPaymentSummary> groupSummaryList) { this.groupSummaryList = groupSummaryList; }
}
