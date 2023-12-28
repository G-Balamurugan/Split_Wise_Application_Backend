package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GroupPaymentSummary {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long groupId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String groupName;
    private double totalToReceive;
    private double totalToPay;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double totalAmountSpent;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public double getTotalToReceive() {
        return totalToReceive;
    }

    public void setTotalToReceive(double totalToReceive) {
        this.totalToReceive = totalToReceive;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(double totalToPay) {
        this.totalToPay = totalToPay;
    }

    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }

    public void setTotalAmountSpent(double totalAmountSpent) {
        this.totalAmountSpent = totalAmountSpent;
    }
}
