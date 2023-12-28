package com.example.group_services.controller;

import com.example.group_services.model.vo.*;
import com.example.group_services.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/httpmethod")
public class HttpMethodController {
    @Autowired
    GroupService groupBean;

    @PostMapping("/register/{userId}")
    public Response userLoginRegister(@PathVariable long userId , @RequestBody LoginVo loginVo){
        return groupBean.userRegister(userId , loginVo);
    }
    @PostMapping("/login")
    public Response userLoginRequest(@RequestBody LoginVo loginVo){
        return groupBean.userLogin(loginVo);
    }
    @PostMapping("/logout")
    public Response userLogoutRequest(@RequestBody LoginVo loginVo){
        return groupBean.userLogout(loginVo.getUserId());
    }
    @GetMapping("/group-list/{userId}")
    public List<GroupVo> groupListForUser(@PathVariable long userId){
        return groupBean.groupList(userId);
    }
    @GetMapping("/group-details/{groupId}")
    public GroupVo groupDetailsRequest(@PathVariable long groupId){
        return groupBean.groupDetails(groupId);
    }
    @GetMapping("/user-details/{userId}")
    public UserVo userDetailsRequest(@PathVariable long userId){
        return groupBean.userDetails(userId);
    }
    @GetMapping("/group-member/{groupId}")
    public List<UserVo> groupMemberList(@PathVariable long groupId){
        return groupBean.getMemberList(groupId);
    }
    @PostMapping("/add/group/{userId}")
    public Response addGroupRequest(@PathVariable long userId , @RequestBody GroupVo groupVo){
        System.out.println("GroupVo details ..... " + groupVo);
        return groupBean.addGroup(userId , groupVo);
    }
    @GetMapping("/user-list")
    public List<UserVo> allUserList(){
        return groupBean.userList();
    }
    @PutMapping("/update/group/{userId}")
    public Response updateGroupDetails(@PathVariable long userId , @RequestBody GroupVo groupVo ){
        return groupBean.updateGroup(userId , groupVo);
    }
    @GetMapping("/filter-group/{groupName}")
    public List<GroupVo> filterByGroupName(@PathVariable String groupName){
        return groupBean.filterGroupName(groupName);
    }
    @GetMapping("/filter-user/{userName}")
    public List<UserVo> filterByUserName(@PathVariable String userName){
        return groupBean.filterUserName(userName);
    }
    @GetMapping("/user-list/{userId}")
    public List<UserVo> specificUserList(@PathVariable long userId) {
        return groupBean.userListSpecific(userId);
    }
}
