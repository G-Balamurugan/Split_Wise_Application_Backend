package com.example.expense_services.model.entity;

import com.example.expense_services.model.vo.UserExpenseMappingVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "expense")
public class Expense {
    private String expenseId;
    @Field
    @NonNull
    private long groupId;
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
    private double totalAmount;
    @Field
    @NonNull
    private String currencyType;
    @Field
    @NonNull
    private List<UserExpenseMappingVo> userList;
    private long updatedBy;
    @Field
    @NonNull
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

}
