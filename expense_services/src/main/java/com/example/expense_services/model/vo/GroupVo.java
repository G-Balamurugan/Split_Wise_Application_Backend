package com.example.expense_services.model.vo;

import java.util.List;

public class GroupVo {
    private long groupId;
    private String groupName;
    private long createdBy;
    private List<UserVo> memberPresent;

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

}
