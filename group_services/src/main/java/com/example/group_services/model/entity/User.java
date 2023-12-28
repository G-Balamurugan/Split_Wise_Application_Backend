package com.example.group_services.model.entity;

import com.example.group_services.model.constant.FieldNames;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = FieldNames.USER_T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = FieldNames.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = FieldNames.USER_ID , nullable = false)
    private long userId;

    @Column(name = FieldNames.USER_NAME , nullable = false)
    private String userName;

    @Column(name = FieldNames.ADDRESS)
    private String address;

    @Column(name = FieldNames.EMAIL)
    private String email;

    @Column(name = FieldNames.CREATED_BY , nullable = false)
    private long createdBy;

    @Column(name = FieldNames.UPDATED_BY)
    private long updatedBy;

    @Column(name = FieldNames.CREATED_DATE , nullable = false)
    private Date createdDate;

    @Column(name = FieldNames.UPDATED_DATE)
    private Date updatedDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) { this.createdBy = createdBy; }

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
