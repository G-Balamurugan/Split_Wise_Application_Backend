package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;

public class NonGroupExpenseVo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String expenseId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String note;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdBy;
    private double amount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String currencyType;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long receiverUser;
    private boolean payed;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long updatedBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date createdDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date updatedDate;

    public String getExpenseId() { return expenseId; }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
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

    public String getCurrencyType() { return currencyType; }

    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public long getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }

    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public long getReceiverUser() { return receiverUser; }

    public void setReceiverUser(long receiverUser) { this.receiverUser = receiverUser; }

    public boolean isPayed() { return payed; }

    public void setPayed(boolean payed) { this.payed = payed; }
}
