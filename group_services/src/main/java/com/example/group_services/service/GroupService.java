package com.example.group_services.service;

import com.example.group_services.model.vo.*;

import java.util.List;

public interface GroupService {
    Response userRegister(long userId , LoginVo loginVo);
    Response userLogin(LoginVo loginVo);
    Response userLogout(long userId);
    List<GroupVo> groupList(long userId);
    GroupVo groupDetails(long groupId);
    UserVo userDetails(long userId);
    List<UserVo> getMemberList(long groupId);
    Response addGroup(long userId , GroupVo groupVo);
    Response updateGroup(long userId , GroupVo groupVo);
    List<UserVo> userList();
    List<GroupVo> filterGroupName(String groupName);
    List<UserVo> filterUserName(String userName);
    List<UserVo> userListSpecific(long userId);
}
