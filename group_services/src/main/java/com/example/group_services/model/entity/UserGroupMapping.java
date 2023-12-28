package com.example.group_services.model.entity;

import com.example.group_services.model.constant.FieldNames;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = FieldNames.USER_GROUP)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupMapping {
    @Id
    @Column(name = FieldNames.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = FieldNames.USER_ID , nullable = false)
    private long userId;

    @Column(name = FieldNames.GROUP_ID , nullable = false)
    private long groupId;

    @Column(name = FieldNames.ACTIVE)
    private boolean active;

    @Column(name = FieldNames.CREATED_BY , nullable = false)
    private long createdBy;

    @Column(name = FieldNames.UPDATED_BY)
    private long updatedBy;

    @Column(name = FieldNames.CREATED_DATE , nullable = false)
    private Date createdDate;

    @Column(name = FieldNames.UPDATED_DATE)
    private Date updatedDate;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public long getGroupId() { return groupId; }

    public void setGroupId(long groupId) { this.groupId = groupId; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public long getCreatedBy() { return createdBy; }

    public void setCreatedBy(long createdBy) { this.createdBy = createdBy; }

    public long getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }
}
