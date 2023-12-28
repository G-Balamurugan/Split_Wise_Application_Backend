package com.example.expense_services.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "non_group")
public class NonGroupExpense {
    @Field
    @NonNull
    private String expenseId;
    @Field
    @NonNull
    private String note;
    @Field
    @NonNull
    private String category;
    @Field
    @NonNull
    private long createdBy;
    @Field
    @NonNull
    private double amount;
    @Field
    @NonNull
    private String currencyType;
    @Field
    @NonNull
    private long receiverUser;
    @Field
    private boolean payed;
    private long updatedBy;
    @Field
    @NonNull
    private Date createdDate;
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

