package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class UserExpenseSummary {
    private double totalToReceive;
    private double totalToPay;
    private double totalAmountSpent;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String groupId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<NetPaymentSummary> paymentSummaryList;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<GroupPaymentSummary> groupSummaryList;

    public UserExpenseSummary(){}

    public UserExpenseSummary(double totalToReceive, double totalToPay) {
        this.totalToReceive = totalToReceive;
        this.totalToPay = totalToPay;
    }

    public double getTotalToReceive() {
        return totalToReceive;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setTotalToReceive(double totalToReceive) {
        this.totalToReceive = totalToReceive;
    }

    public void setTotalToPay(double totalToPay) {
        this.totalToPay = totalToPay;
    }

    public List<NetPaymentSummary> getPaymentSummaryList() { return paymentSummaryList; }

    public void setPaymentSummaryList(List<NetPaymentSummary> paymentSummaryList) { this.paymentSummaryList = paymentSummaryList; }

    public List<GroupPaymentSummary> getGroupSummaryList() { return groupSummaryList; }

    public void setGroupSummaryList(List<GroupPaymentSummary> groupSummaryList) { this.groupSummaryList = groupSummaryList; }

    public double getTotalAmountSpent() { return totalAmountSpent; }

    public void setTotalAmountSpent(double totalAmountSpent) { this.totalAmountSpent = totalAmountSpent; }

}