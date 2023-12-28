package com.example.expense_services.serviceImplementation;

import com.example.expense_services.dao.api.ExpenseRepository;
import com.example.expense_services.dao.api.NonGroupExpenseRepository;
import com.example.expense_services.model.entity.Expense;
import com.example.expense_services.model.entity.NonGroupExpense;
import com.example.expense_services.model.vo.*;
import com.example.expense_services.service.ExpenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("expenseBean")
public class ExpenseServiceImplementation implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    NonGroupExpenseRepository nonGroupRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String , String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static String groupBaseUrl = "http://localhost:8081/httpmethod";

    private void sendMessage(NotificationVo notificationVo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send("com.quinbay.expense.create", objectMapper.writeValueAsString(notificationVo));
    }
    public static String generateUniqueId() {
        SecureRandom secureRandom = new SecureRandom();
        long mostSigBits = secureRandom.nextLong();
        long leastSigBits = secureRandom.nextLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        return uuid.toString();
    }
    public static double currencyExchange(String currencyType , double amount)
    {
        Map<String,Double> currency = new HashMap<>();
        currency.put("USD" , 83.24);
        currency.put("EURO" , 91.52);
        return currencyType.equals("INR") ? amount : currency.get(currencyType) * amount;
    }
    private String validateUserSplit(ExpenseVo expenseVo){
        double percentage = 0;
        for(UserExpenseMappingVo userExpense : expenseVo.getUserList())
        { percentage += userExpense.getSplitPercentage(); }
        return percentage == 100 ? "Correct Split" : "Incorrect Split";
    }
    public Response addExpense(ExpenseVo expenseVo){
        GroupVo groupVo = getGroupInfo(expenseVo.getGroupId());
        if(groupVo == null)return new Response("" , "Group Id : " + expenseVo.getGroupId() + " , Not Found");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(groupBaseUrl+"/group-member/"+expenseVo.getGroupId()).build();
        List<UserVo> userVoList = restTemplate.exchange(builder.toUriString()
                ,HttpMethod.GET
                ,entity
                ,new ParameterizedTypeReference<List<UserVo>>() {}
                ).getBody();

        Map<Long , Boolean> userValid = new HashMap<>();
        for(UserVo userVo : userVoList){ userValid.put(userVo.getUserId() , true); }
        for(UserExpenseMappingVo userExpense : expenseVo.getUserList()){
            if (!userValid.containsKey(userExpense.getUserId()))
                return new Response("UserId " + userExpense.getUserId() + " , Not valid");
        }
        if(!userValid.containsKey(expenseVo.getCreatedBy()))
            return new Response("UserId " + expenseVo.getCreatedBy()+ " , Not valid");
        if(validateUserSplit(expenseVo).equals("Incorrect Split"))
            return new Response("Incorrect Split");
        if(expenseVo.getTotalAmount() <= 0)
            return new Response("Amount can only be positive");
        expenseVo.setExpenseId(generateUniqueId());
        for(UserExpenseMappingVo userExpense : expenseVo.getUserList()){
            if(userExpense.getSplitPercentage() <= 0)
                return new Response("Percentage can only be positive");
            double amount = (expenseVo.getTotalAmount() * userExpense.getSplitPercentage())/100;
            userExpense.setSplitAmount(currencyExchange(expenseVo.getCurrencyType() , amount));
            if(userExpense.getUserId() != expenseVo.getCreatedBy()){
                NotificationVo notificationVo = new NotificationVo();
                notificationVo.setGroupId(expenseVo.getGroupId());
                notificationVo.setUserId(userExpense.getUserId());
                notificationVo.setCreatedBy(expenseVo.getCreatedBy());
                String groupNameInfo = groupVo.getGroupName();
                notificationVo.setInformation("New Split : " + expenseVo.getNote() + " ,Group : " + groupNameInfo);
                try{
                    sendMessage(notificationVo);
                }catch (JsonProcessingException exception){
                    System.out.println(exception);
                }
            }
        }
        expenseVo.setTotalAmount(currencyExchange(expenseVo.getCurrencyType() , expenseVo.getTotalAmount()));
        Expense expense = objectMapper.convertValue(expenseVo , Expense.class);
        expense.setUpdatedBy(expenseVo.getCreatedBy());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
            expense.setCreatedDate(dateWithoutTime);
            expense.setUpdatedDate(dateWithoutTime);

        } catch (ParseException exception) {
            System.out.println(exception);
        }
        expenseRepository.save(expense);
        return new Response("Added Successfully");
    }
    public Response userPayment(String expenseId, long userId) {
        Query query = new Query(Criteria.where("expenseId").is(expenseId).and("userList.userId").is(userId));
        Update update = new Update();
        update.set("userList.$.payed", true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
            update.set("updatedDate", dateWithoutTime);
            update.set("updatedBy" , userId);
        } catch (ParseException exception) {
            System.out.println(exception);
        }

        Expense updatedExpense = mongoTemplate.findAndModify(query, update, Expense.class);
        Response response = new Response();
        if (updatedExpense != null) {
            boolean userAlreadyPaid = updatedExpense.getUserList().stream()
                    .filter(userExpense -> userExpense.getUserId() == userId)
                    .anyMatch(UserExpenseMappingVo::isPayed);
            if (userAlreadyPaid) {
                response.setId(expenseId);
                response.setStatus("Already Paid");
            } else {
                response.setId(expenseId);
                response.setStatus("Added Successfully");
            }
        } else {
            response.setStatus("User Not Found");
        }
        return response;
    }
    public List<ExpenseVo> expenseList(long groupId){
        List<Expense> expenseList = expenseRepository.findExpenseListByGroupId(groupId);
        List<ExpenseVo> expenseVoList = new ArrayList<>();
        for(Expense expense : expenseList){
            ExpenseVo expenseVo = objectMapper.convertValue(expense , ExpenseVo.class);
            expenseVo = setExpenseFieldsNull(expenseVo);
            expenseVoList.add(expenseVo);
        }
        return expenseVoList;
    }
    private List<GroupPaymentSummary> userGroupList(long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(groupBaseUrl + "/group-list/" + userId).build();
        List<GroupPaymentSummary> groupPaymentSummaryList;

        groupPaymentSummaryList = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<GroupPaymentSummary>>() {}
        ).getBody();

        List<GroupPaymentSummary> resultSummary = new ArrayList<>();
        for (GroupPaymentSummary groupPayment : groupPaymentSummaryList) {
            GroupPaymentSummary result = getUserGroupSummary(groupPayment.getGroupId(), userId);
            resultSummary.add(result);
        }
        return resultSummary;
    }
    private GroupPaymentSummary getUserGroupSummary(long groupId, long userId) {
        double totalToReceive = 0, totalToPay = 0;
        double totalSpent = 0;

        List<Expense> groupExpense = expenseRepository.findByGroupIdAndCreatedBy(groupId, userId);
        for (Expense expense : groupExpense) {
            totalSpent += expense.getTotalAmount();
            for (UserExpenseMappingVo userExpense : expense.getUserList()) {
                if(userExpense.getUserId() != userId)
                {
                    if (!userExpense.isPayed()) {
                        totalToReceive += userExpense.getSplitAmount();
                    }
                    else{
                        totalSpent -= userExpense.getSplitAmount();
                    }
                }
            }
        }
        groupExpense = expenseRepository.findByUserListUserIdAndGroupId(userId, groupId);
        for (Expense expense : groupExpense) {
            if (expense.getCreatedBy() != userId) {
                for (UserExpenseMappingVo userExpense : expense.getUserList()) {
                    if (!userExpense.isPayed()) {
                        totalToPay += userExpense.getSplitAmount();
                    }
                    else {
                        totalSpent += userExpense.getSplitAmount();
                    }
                }
            }
        }
        GroupPaymentSummary groupPaymentSummary = new GroupPaymentSummary();
        groupPaymentSummary.setUserId(userId);
        groupPaymentSummary.setGroupId(groupId);
        groupPaymentSummary.setGroupName(getGroupInfo(groupId).getGroupName());
        groupPaymentSummary.setTotalToPay(totalToPay);
        groupPaymentSummary.setTotalToReceive(totalToReceive);
        groupPaymentSummary.setTotalAmountSpent(totalSpent);
        return groupPaymentSummary;
    }
    private GroupVo getGroupInfo(long groupId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(groupBaseUrl+"/group-details/"+groupId).build();
        GroupVo group = restTemplate.exchange(builder.toUriString()
                ,HttpMethod.GET
                ,entity
                ,GroupVo.class).getBody();
        return group;
    }
    public UserExpenseSummary getUserExpenseSummary(long userId) {
        Map<Long, Double> paymentMap = new HashMap<>();
        double totalToReceive = 0, totalToPay = 0;
        Map<Long , Map<Long , GroupPaymentSummary>> childSummary = new HashMap<>();

        List<Expense> userExpenses = expenseRepository.findByCreatedBy(userId);
        for (Expense expense : userExpenses) {
            for (UserExpenseMappingVo userExpense : expense.getUserList()) {
                long tempUserId = userExpense.getUserId();
                if (!userExpense.isPayed() && tempUserId != userId) {
                    totalToReceive += userExpense.getSplitAmount();
                    updatePaymentMap(paymentMap, tempUserId, userExpense.getSplitAmount());
                    updateChildSummaryMap(childSummary,tempUserId,expense.getGroupId(),0 , userExpense.getSplitAmount());
                }
            }
        }
        List<Expense> userExpensesWithUser = expenseRepository.findByUserListUserId(userId);
        for (Expense expense : userExpensesWithUser) {
            long tempUserId = expense.getCreatedBy();
            if (tempUserId != userId) {
                for (UserExpenseMappingVo userExpense : expense.getUserList()) {
                    if (!userExpense.isPayed()) {
                        totalToPay += userExpense.getSplitAmount();
                        updatePaymentMap(paymentMap, tempUserId, -userExpense.getSplitAmount());
                        updateChildSummaryMap(childSummary,tempUserId,expense.getGroupId(),userExpense.getSplitAmount(),0);
                    }
                }
            }
        }
        double amount;
        List<NetPaymentSummary> netPaymentList = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : paymentMap.entrySet()) {
            NetPaymentSummary netPayment = new NetPaymentSummary();
            netPayment.setUserId(entry.getKey());
            netPayment.setUserName(getUserInfo(entry.getKey()).getUserName());

            amount = nonGroupUserSummary(userId , entry.getKey());
            amount += entry.getValue();

            if (amount < 0) {
                netPayment.setType("Need to Pay");
                netPayment.setAmount(-amount);
            } else {
                netPayment.setType("Need to Receive");
                netPayment.setAmount(amount);
            }
            netPayment = addGroupSummaryList(netPayment,childSummary.get(entry.getKey()));
            netPaymentList.add(netPayment);
        }
        UserExpenseSummary filteredResult = new UserExpenseSummary(totalToReceive, totalToPay);
        filteredResult.setPaymentSummaryList(netPaymentList);
        filteredResult.setGroupSummaryList(userGroupList(userId));
        return filteredResult;
    }
    private double nonGroupUserSummary(long userId , long memberId){
        double amount = 0;
        List<NonGroupExpense> nonGroupExpenseList = nonGroupRepository.findByCreatedByAndReceiverUser(userId , memberId);
        for( NonGroupExpense nonGroupExpense : nonGroupExpenseList){
            if(!nonGroupExpense.isPayed())
                amount += nonGroupExpense.getAmount();
        }
        nonGroupExpenseList = nonGroupRepository.findByCreatedByAndReceiverUser(memberId , userId);
        for(NonGroupExpense nonGroupExpense : nonGroupExpenseList){
            if(!nonGroupExpense.isPayed())
                amount -= nonGroupExpense.getAmount();
        }
        return amount;
    }
    private NetPaymentSummary addGroupSummaryList(NetPaymentSummary netPaymentSummary, Map<Long, GroupPaymentSummary> innerMap) {
        if (netPaymentSummary == null || innerMap == null)
            return netPaymentSummary;

        List<GroupPaymentSummary> newList = netPaymentSummary.getGroupSummaryList();
        if (newList == null) {
            newList = new ArrayList<>();
        }

        for (Map.Entry<Long, GroupPaymentSummary> entry : innerMap.entrySet()) {
            newList.add(entry.getValue());
        }
        netPaymentSummary.setGroupSummaryList(newList);
        return netPaymentSummary;
    }
    private void updatePaymentMap(Map<Long, Double> paymentMap, long userId, double amount) {
        if (paymentMap.containsKey(userId)) {
            paymentMap.put(userId, paymentMap.get(userId) + amount);
        } else {
            paymentMap.put(userId, amount);
        }
    }
    private void updateChildSummaryMap(Map<Long, Map<Long,GroupPaymentSummary>> childSummary,long userId,long groupId,double toPay,double toGet){
        if(!childSummary.containsKey(userId))
        {
            Map<Long , GroupPaymentSummary> innerMap = new HashMap<>();
            childSummary.put(userId , innerMap);
        }
        updateInnerSummary(childSummary.get(userId) , groupId , toPay , toGet);
    }
    private void updateInnerSummary(Map<Long , GroupPaymentSummary> innerMap , long groupId , double toPay , double toGet){
        GroupPaymentSummary groupPayment;
        if(innerMap.containsKey(groupId)){
            groupPayment = innerMap.get(groupId);
            groupPayment.setTotalToPay(groupPayment.getTotalToPay() + toPay);
            groupPayment.setTotalToReceive(groupPayment.getTotalToReceive() + toGet);
        }
        else {
            groupPayment = new GroupPaymentSummary();
            groupPayment.setGroupId(groupId);
            groupPayment.setGroupName(getGroupInfo(groupId).getGroupName());
            groupPayment.setTotalToPay(toPay);
            groupPayment.setTotalToReceive(toGet);
        }
        innerMap.put(groupId , groupPayment);
    }
    private UserVo getUserInfo(long userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(groupBaseUrl+"/user-details/"+userId).build();
        UserVo user = restTemplate.exchange(builder.toUriString()
                ,HttpMethod.GET
                ,entity
                ,UserVo.class).getBody();
        return user;
    }
    public List<ExpenseVo> filterByGroupIdAndCategory(long groupId, String category) {
        List<Expense> expenseList = expenseRepository.findByGroupIdAndCategory(groupId, category);
        List<ExpenseVo> expenseVoList = objectMapper.convertValue(expenseList, new TypeReference<List<ExpenseVo>>() {});
        for (ExpenseVo expenseVo : expenseVoList)
            expenseVo = setExpenseFieldsNull(expenseVo);
        return expenseVoList;
    }
    public List<UserExpenseSummary> filterAllCategory(long userId) {
        List<String> categoryList = new ArrayList<>();
        List<Expense> expenseList = expenseRepository.findAll();
        boolean check;
        for (Expense expense : expenseList) {
            System.out.println(expense);
            check = false;
            if (expense.getCreatedBy() == userId) {
                check = true;
            }
            if(expense.getUserList() != null) {
                for (UserExpenseMappingVo userExpenseMappingVo : expense.getUserList()) {

                    if (userExpenseMappingVo.getUserId() == userId) {
                        check = true;
                    }}
            }
            if (check) {
                for (String eachCategory : categoryList) {
                    if (eachCategory.equals(expense.getCategory()))
                        check = false;
                }
                if (check)
                    categoryList.add(expense.getCategory());
            }
        }
        List<UserExpenseSummary> finalFilter = new ArrayList<>();
        for (String category : categoryList) {
            UserExpenseSummary eachCategory = filterCategory(userId, category);
            finalFilter.add(eachCategory);
        }
        return finalFilter;
    }
    private UserExpenseSummary filterCategory(long userId, String category) {
        double toPay = 0, toGet = 0;
        double totalSpent = 0;

        List<Expense> expenseList = expenseRepository.findByCategoryAndCreatedBy(category, userId);
        for (Expense expense : expenseList) {
            totalSpent += expense.getTotalAmount();
            for (UserExpenseMappingVo userExpense : expense.getUserList()) {
                if(userExpense.getUserId() != userId)
                {
                    if(!userExpense.isPayed())
                        toGet += userExpense.getSplitAmount();
                    else
                        totalSpent -= userExpense.getSplitAmount();
                }
            }
        }
        expenseList = expenseRepository.findByCategoryAndUserListUserId(category, userId);
        for( Expense expense : expenseList ){
            if(expense.getCreatedBy() != userId)
                for(UserExpenseMappingVo userExpense : expense.getUserList()){
                if(!userExpense.isPayed())
                    toPay += userExpense.getSplitAmount();
                else
                    totalSpent += userExpense.getSplitAmount();
            }
        }
        UserExpenseSummary userExpenseSummary = filterNonGroupCategory(userId , category);
        userExpenseSummary.setCategory(category);
        userExpenseSummary.setTotalToPay(toPay + userExpenseSummary.getTotalToPay());
        userExpenseSummary.setTotalToReceive(toGet + userExpenseSummary.getTotalToReceive());
        userExpenseSummary.setTotalAmountSpent(totalSpent + userExpenseSummary.getTotalAmountSpent());
        return userExpenseSummary;
    }
    private UserExpenseSummary filterNonGroupCategory(long userId , String category) {
        double toPay = 0, toGet = 0;
        double amountSpent = 0;
        List<NonGroupExpense> nonGroupExpenseList = nonGroupRepository.findByCategoryAndUserId(category,userId);
        for(NonGroupExpense nonGroupExpense : nonGroupExpenseList){
            if(nonGroupExpense.getCreatedBy() == userId)
            {
                if(!nonGroupExpense.isPayed())
                toGet += nonGroupExpense.getAmount();
                amountSpent += nonGroupExpense.getAmount();
            }

            else if(nonGroupExpense.getReceiverUser() == userId){
                if(!nonGroupExpense.isPayed())
                    toPay += nonGroupExpense.getAmount();
                else
                    amountSpent += nonGroupExpense.getAmount();
            }
        }
        UserExpenseSummary userExpenseSummary = new UserExpenseSummary();
        userExpenseSummary.setCategory(category);
        userExpenseSummary.setTotalToPay(toPay);
        userExpenseSummary.setTotalToReceive(toGet);
        userExpenseSummary.setTotalAmountSpent(amountSpent);
        return userExpenseSummary;
    }
    public Response groupSettlement(long userId , long groupId){
        GroupVo groupVo = getGroupInfo(groupId);
        if(groupVo == null){
            return new Response("Group Not found");
        }
//        if(groupVo.getCreatedBy() != userId)
//            return new Response("" , "Only Group Admin can make settlements");
        Response response;
        List<ExpenseVo> expenseVoList = expenseList(groupId);
        for(ExpenseVo expenseVo : expenseVoList){
            if(expenseVo.getCreatedBy() == userId) {
                for (UserExpenseMappingVo userExpense : expenseVo.getUserList()) {
                    if (!userExpense.isPayed()) {
                        response = userPayment(expenseVo.getExpenseId(), userExpense.getUserId());
                        if (!response.getStatus().equals("Added Successfully") && !response.getStatus().equals("Already Paid"))
                            return response;
                    }
                }
            }
        }
        return new Response("Settlements made");
    }
    public String pendingPaymentCheck(long userId , long groupId){
        List<Expense> createdCheck = expenseRepository.findByGroupIdAndCreatedBy(groupId , userId);
        for(Expense expense : createdCheck)
            for(UserExpenseMappingVo userExpense : expense.getUserList())
                if(!userExpense.isPayed())
                    return "Payments pending";
        List<Expense> userListCheck = expenseRepository.findByUserListUserIdAndGroupId(userId , groupId);
        for(Expense expense : userListCheck)
            for(UserExpenseMappingVo userExpense : expense.getUserList())
                if(!userExpense.isPayed())
                    return "Payments pending";
        return "Payments done";
    }
    public Response categoryList(){
        Response response = new Response();
        response.setCategoryList(Arrays.asList("Food","Transportation","Accommodation","Others"));
        return response;
    }
    private ExpenseVo setExpenseFieldsNull(ExpenseVo expenseVo){
        expenseVo.setUpdatedBy(0);
        expenseVo.setUpdatedDate(null);
        expenseVo.setCreatedDate(null);
        return expenseVo;
    }
}

