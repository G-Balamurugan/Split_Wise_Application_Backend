package com.example.notification_services.model.entity;

import com.example.notification_services.model.constant.FieldNames;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = FieldNames.NOTIFICATION_T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @Column(name = FieldNames.NOTIFICATION_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationId;

    @Column(name = FieldNames.USER_ID , nullable = false)
    private long userId;

    @Column(name = FieldNames.GROUP_ID )
    private long groupId;

    @Column(name = FieldNames.INFORMATION , nullable = false)
    private String information;

    @Column(name = FieldNames.READ_MESSAGE)
    private boolean readMessage;

    @Column(name = FieldNames.CREATED_BY , nullable = false)
    private long createdBy;

    @Column(name = FieldNames.UPDATED_BY)
    private long updatedBy;

    @Column(name = FieldNames.CREATED_DATE , nullable = false)
    private Date createdDate;

    @Column(name = FieldNames.UPDATED_DATE)
    private Date updatedDate;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isReadMessage() {
        return readMessage;
    }

    public void setReadMessage(boolean readMessage) {
        this.readMessage = readMessage;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

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
