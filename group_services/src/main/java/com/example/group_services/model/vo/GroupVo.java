package com.example.group_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class GroupVo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long groupId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String groupName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<UserVo> memberPresent;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long updatedBy;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date createdDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Date updatedDate;

    public List<UserVo> getMemberPresent() { return memberPresent; }

    public void setMemberPresent(List<UserVo> memberPresent) { this.memberPresent = memberPresent; }

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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }
}
