package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class ExpenseVo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String expenseId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long groupId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String note;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double totalAmount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String currencyType;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<UserExpenseMappingVo> userList;
    private long updatedBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date createdDate;
    private Date updatedDate;

    public String getExpenseId() { return expenseId; }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrencyType() { return currencyType; }

    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }

    public List<UserExpenseMappingVo> getUserList() {
        return userList;
    }

    public void setUserList(List<UserExpenseMappingVo> userList) {
        this.userList = userList;
    }

    public long getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
