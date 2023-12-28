package com.example.group_services.serviceImplementation;

import com.example.group_services.dao.api.GroupRepository;
import com.example.group_services.dao.api.UserGroupMappingRepository;
import com.example.group_services.dao.api.LoginRepository;
import com.example.group_services.dao.api.UserRepository;
import com.example.group_services.model.entity.Group;
import com.example.group_services.model.entity.Login;
import com.example.group_services.model.entity.User;
import com.example.group_services.model.entity.UserGroupMapping;
import com.example.group_services.model.vo.*;
import com.example.group_services.service.GroupService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("groupBean")
public class GroupServiceImplementation implements GroupService {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserGroupMappingRepository userGroupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String , String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String expenseBaseUrl = "http://localhost:8089/httpmethod";

    private void sendMessage(NotificationVo notificationVo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send("com.quinbay.group.create", objectMapper.writeValueAsString(notificationVo));
    }
    private static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("Hashing algorithm not available.", exception);
        }
    }
    public Response userRegister(long userId , LoginVo loginVo){
        Response response = new Response();
        Login loginCheck = loginRepository.findByPhoneNumber(loginVo.getPhoneNumber());
        if(Objects.isNull(loginCheck)){
            String hashValue = generateHash(loginVo.getUserPassword());
            Login login = new Login();
            login.setPhoneNumber(loginVo.getPhoneNumber());
            login.setUserPassword(hashValue);
            login.setCreatedBy(userId);
            login.setUpdatedBy(userId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWithoutTime;
            try {
                dateWithoutTime = sdf.parse(sdf.format(new Date()));
                login.setCreatedDate(dateWithoutTime);
                login.setUpdatedDate(dateWithoutTime);
            } catch (ParseException exception) {
                System.out.println(exception);
            }
            login = loginRepository.save(login);
            response.setId(login.getUserId());
            response.setStatus("Register Successful");
        }
        else {
            response.setStatus("Already Registered");
        }
        return response;
    }
    public Response userLogin(LoginVo loginVo){
        String hashValue = generateHash(loginVo.getUserPassword());
        Login loginInfo = loginRepository.findByPhoneNumberAndUserPassword(loginVo.getPhoneNumber() , hashValue);
        Response response = new Response();
        if (Objects.isNull(loginInfo)) {
            response.setStatus("Login Failed");
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWithoutTime;
            try {
                dateWithoutTime = sdf.parse(sdf.format(new Date()));
                loginInfo.setUpdatedDate(dateWithoutTime);
            } catch (ParseException exception) {
                System.out.println(exception);
            }
            loginRepository.save(loginInfo);
            response.setStatus("Login Successful");
            response.setUserName(userDetails(loginInfo.getUserId()).getUserName());
            response.setId(loginInfo.getUserId());
        }
        return response;
    }
    public Response userLogout(long userId){
        Response response = new Response();
//        Login loginInfo = loginRepository.findUserDetailsById(userId);
//        if(loginInfo.isValidity()){
//            loginInfo.setValidity(false);
//            loginRepository.save(loginInfo);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date dateWithoutTime;
//            try {
//                dateWithoutTime = sdf.parse(sdf.format(new Date()));
//                loginInfo.setUpdatedDate(dateWithoutTime);
//            } catch (ParseException exception) {
//                System.out.println(exception);
//            }
//
//            response.setStatus("Logout Successful");
//        }
//        else {
//            response.setStatus("Not Logged In");
//        }
        response.setStatus("Logout");
        return response;
    }
    public List<GroupVo> groupList(long userId) {
        List<UserGroupMapping> groupList = userGroupRepository.findByUserId(userId);
        List<GroupVo> groupListInfo = new ArrayList<>();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Comparator<GroupVo> comparator = Comparator.comparing(groupVo -> pendingPaymentCheck(userId, groupVo.getGroupId()));
        comparator = comparator.thenComparing(Comparator.comparing(GroupVo::getCreatedDate, Comparator.reverseOrder()));
        groupListInfo = groupList.stream()
                .map(userGroup -> {
                    Group group = groupRepository.findByGroupId(userGroup.getGroupId());
                    GroupVo groupVo = objectMapper.convertValue(group, GroupVo.class);
                    groupVo = setGroupFieldsNull(groupVo);
                    return groupVo;
                })
                .sorted(comparator)
                .limit(30)
                .collect(Collectors.toList());
        return groupListInfo;
    }
    private Boolean pendingPaymentCheck(long userId , long groupId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(expenseBaseUrl+"/payment-pending/"+userId+"/"+groupId).build();
        String paymentCheck = restTemplate.exchange(builder.toUriString()
                ,HttpMethod.GET
                ,entity
                ,String.class).getBody();
        return paymentCheck.equals("Payments done");
    }
    public GroupVo groupDetails(long groupId){
        Group group = groupRepository.findByGroupId(groupId);
        GroupVo groupVo = objectMapper.convertValue(group , GroupVo.class);
        if(groupVo != null){
        groupVo = setGroupFieldsNull(groupVo);
        groupVo.setMemberPresent(getMemberList(groupId));}
        return groupVo;
    }
    public UserVo userDetails(long userId){
        User user = userRepository.findByUserId(userId);
        UserVo userVo = objectMapper.convertValue(user , UserVo.class);
        if(userVo != null)
        userVo = setUserFieldsNull(userVo);
        return userVo;
    }
    public List<UserVo> getMemberList(long groupId){
        List<UserGroupMapping> groupMemberVoList = userGroupRepository.findByGroupId(groupId);
        List<UserVo> memberList = new ArrayList<>();
        for (UserGroupMapping userGroup : groupMemberVoList){
            if(userGroup.isActive()) {
                User userDetails = userRepository.findByUserId(userGroup.getUserId());
                UserVo userVo = objectMapper.convertValue(userDetails, UserVo.class);
                userVo = setUserFieldsNull(userVo);
                userVo.setId(0);
                userVo.setAddress(null);
                userVo.setEmail(null);

                memberList.add(userVo);
            }
        }
        return memberList;
    }
    public Response addGroup(long userId, GroupVo groupVo){
        String message;
        Response response = new Response();
        Group chkGroupPresent = groupRepository.findByGroupName(groupVo.getGroupName());
        if(!Objects.isNull(chkGroupPresent))
            message = "Group Name Already Exists";
        else {
            Group newGroup = new Group();
            newGroup.setGroupName(groupVo.getGroupName());
            newGroup.setCreatedBy(userId);
            newGroup.setUpdatedBy(userId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWithoutTime;
            try {
                dateWithoutTime = sdf.parse(sdf.format(new Date()));
                newGroup.setCreatedDate(dateWithoutTime);
                newGroup.setUpdatedDate(dateWithoutTime);
            } catch (ParseException exception) {
                System.out.println(exception);
            }
            newGroup = groupRepository.save(newGroup);
            message = " Group Created Successfully";
            for (UserVo userGroup : groupVo.getMemberPresent())
            {
                addUserGroup(newGroup.getGroupId() , userGroup.getUserId() , userId);
                if(userGroup.getUserId() != newGroup.getCreatedBy()){
                    NotificationVo notificationVo = new NotificationVo();
                    notificationVo.setUserId(userGroup.getUserId());
                    notificationVo.setGroupId(newGroup.getGroupId());
                    notificationVo.setCreatedBy(userId);
                    UserVo userVo = userDetails(userId);
                    notificationVo.setInformation("Group " + newGroup.getGroupName() + " has been created and User " + userVo.getUserName() + " added you");
                    try{
                        sendMessage(notificationVo);
                    }catch (JsonProcessingException exception){
                        System.out.println(exception);
                    }
                }
            }
            response.setId(newGroup.getGroupId());
        }
        response.setStatus(message);
        return response;
    }
    private Response addUserGroup(long groupId , long userId , long createdBy){
        String message;
        UserGroupMapping userGroup = userGroupRepository.findByGroupIdAndUserId(groupId , userId);
        if(Objects.isNull(userGroup))
        {
            UserGroupMapping newUserGroup = new UserGroupMapping();
            newUserGroup.setUserId(userId);
            newUserGroup.setGroupId(groupId);
            newUserGroup.setCreatedBy(createdBy);
            newUserGroup.setUpdatedBy(createdBy);
            newUserGroup.setActive(true);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWithoutTime;
            try {
                dateWithoutTime = sdf.parse(sdf.format(new Date()));
                newUserGroup.setCreatedDate(dateWithoutTime);
                newUserGroup.setUpdatedDate(dateWithoutTime);
            } catch (ParseException exception) {
                System.out.println(exception);
            }
            userGroupRepository.save(newUserGroup);
            message = "Added Successfully";
        }
        else {
            if(!userGroup.isActive())
            {
                userGroup.setActive(true);
                userGroupRepository.save(userGroup);
                message = "Member is Active now";
            }
            else
                message = "Already Exist";
        }
        Response response = new Response();
        response.setStatus(message);
        return response;
    }
    private Response removeUserGroup(long groupId , long userId , long updatedBy) {
        String message;
        UserGroupMapping userGroup = userGroupRepository.findByGroupIdAndUserId(groupId , userId);
        if(Objects.isNull(userGroup)){
            message = "No such entry";
        }
        else {
            userGroup.setActive(false);
            userGroup.setUpdatedBy(updatedBy);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWithoutTime;

            try {
                dateWithoutTime = sdf.parse(sdf.format(new Date()));
                userGroup.setUpdatedDate(dateWithoutTime);
            } catch (ParseException exception) {
                System.out.println(exception);
            }

            userGroupRepository.save(userGroup);
            message = "Updated successfully";
        }
        Response response = new Response();
        response.setStatus(message);
        return response;
    }
    public List<UserVo> userList(){
        List<User> userDetailList = userRepository.findAll();
        List<UserVo> finalList = objectMapper.convertValue(userDetailList,
                new TypeReference<List<UserVo>>() {});
        for(UserVo userVo : finalList)
            userVo = setUserFieldsNull(userVo);
        return finalList;
    }
    public List<UserVo> userListSpecific(long userId) {
        List<User> userDetailList = userRepository.findAll();
        List<UserVo> finalList = new ArrayList<>();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Comparator<UserVo> comparator = Comparator.comparing(userVo -> pendingUserPaymentsCheck(userId, userVo.getUserId()));
        comparator = comparator
                .thenComparing(Comparator.comparing(UserVo::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder())));
        finalList = userDetailList.stream()
                .map(user -> {
                    UserVo userVo = objectMapper.convertValue(user, UserVo.class);
                    if(userId == userVo.getUserId())
                        userVo.setUserName( userVo.getUserName() + " (you)" );
                    userVo = setUserFieldsNull(userVo);
                    return userVo;
                })
                .sorted(comparator)
                .limit(30)
                .collect(Collectors.toList());
        return finalList;
    }
    private Boolean pendingUserPaymentsCheck(long userId , long memberId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(expenseBaseUrl+"/user-payment-pending/"+userId+"/"+memberId).build();
        String paymentCheck = restTemplate.exchange(builder.toUriString()
                ,HttpMethod.GET
                ,entity
                ,String.class).getBody();
        return paymentCheck.equals("Payments done");
    }
    public Response updateGroup(long userId , GroupVo groupVo) {
        long groupId = groupVo.getGroupId();
        Response response = new Response();
        response.setId(groupVo.getGroupId());

        Group group = groupRepository.findByGroupId(groupId);
        if(Objects.isNull(group)) {
            return new Response("Group Not Found");
        }
        if(!groupVo.getGroupName().equals("")){
            group.setGroupName(groupVo.getGroupName());
        }

        Map<Long , Boolean> memberCheck = new HashMap<>();
        List<UserVo> oldMemberList = getMemberList(groupId);

        for (UserVo userVo : oldMemberList){
            memberCheck.put(userVo.getUserId() , false);
        }

        long tempUserId;
        for (UserVo userVo : groupVo.getMemberPresent()){
            tempUserId = userVo.getUserId();
            if(memberCheck.containsKey(tempUserId)){
                memberCheck.put(tempUserId , true);
            }
            else {
                User user = userRepository.findByUserId(tempUserId);
                if(Objects.isNull(user))
                    return new Response("UserId : "+ tempUserId + " , Not Found");
            }
        }
        for (UserVo userVo : groupVo.getMemberPresent()){
            tempUserId = userVo.getUserId();
            if(!memberCheck.containsKey(tempUserId))
                addUserGroup(groupId , tempUserId , userId);
        }
        for (Map.Entry<Long , Boolean> entry : memberCheck.entrySet()){
            if(!entry.getValue()){
                removeUserGroup(groupId , entry.getKey() , userId);
            }
        }
        groupRepository.save(group);
        return new Response("Updated Successfully");
    }
    public List<GroupVo> filterGroupName(String groupName){
        groupName = groupName.toLowerCase().replace(" ","");
        List<Group> groupList = groupRepository.findGroupByNameIgnoreCaseAndNormalized(groupName);
        List<GroupVo> finalList = new ArrayList<>();
        GroupVo groupVo;
        for(Group group : groupList){
            groupVo = objectMapper.convertValue(group , GroupVo.class);
            groupVo = setGroupFieldsNull(groupVo);
            finalList.add(groupVo);
        }
        return finalList;
    }
    public List<UserVo> filterUserName(String userName){
        userName = userName.toLowerCase().replace(" " , "");
        List<User> userList = userRepository.findByUserNameIgnoreCaseAndNormalized(userName);
        List<UserVo> finalList = new ArrayList<>();
        UserVo userVo;
        for(User user : userList){
            userVo = objectMapper.convertValue(user , UserVo.class);
            userVo = setUserFieldsNull(userVo);
            finalList.add(userVo);
        }
        return finalList;
    }
    private UserVo setUserFieldsNull(UserVo userVo){
        userVo.setCreatedDate(null);
        userVo.setUpdatedDate(null);
        userVo.setCreatedBy(0);
        userVo.setUpdatedBy(0);
        return userVo;
    }
    private GroupVo setGroupFieldsNull(GroupVo groupVo){
        groupVo.setUpdatedBy(0);
        groupVo.setUpdatedDate(null);
        return groupVo;
    }
}
