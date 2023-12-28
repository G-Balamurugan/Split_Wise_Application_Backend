package com.example.expense_services.serviceImplementation;

import com.example.expense_services.dao.api.NonGroupExpenseRepository;
import com.example.expense_services.model.entity.NonGroupExpense;
import com.example.expense_services.model.vo.NonGroupExpenseVo;
import com.example.expense_services.model.vo.NotificationVo;
import com.example.expense_services.model.vo.Response;
import com.example.expense_services.model.vo.UserVo;
import com.example.expense_services.service.NonGroupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("nonGroupBean")
public class NonGroupImplementation implements NonGroupService {
    @Autowired
    NonGroupExpenseRepository nonGroupExpenseRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
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
    public List<NonGroupExpenseVo> nonGroupPaymentsList(long createdUser , long receiverUser){
        List<NonGroupExpenseVo> finalList = new ArrayList<>();
        NonGroupExpenseVo nonGroupExpenseVo;

        List<NonGroupExpense> nonGroupList = nonGroupExpenseRepository.findByCreatedByAndReceiverUser(createdUser , receiverUser);
        for(NonGroupExpense nonGroupExpense : nonGroupList){
            nonGroupExpenseVo = objectMapper.convertValue(nonGroupExpense , NonGroupExpenseVo.class);
            nonGroupExpenseVo = setNonGroupFieldsNull(nonGroupExpenseVo);
            finalList.add(nonGroupExpenseVo);
        }
        if(createdUser == receiverUser)
            return finalList;

        nonGroupList = nonGroupExpenseRepository.findByCreatedByAndReceiverUser(receiverUser , createdUser);
        for(NonGroupExpense nonGroupExpense : nonGroupList){
            nonGroupExpenseVo = objectMapper.convertValue(nonGroupExpense , NonGroupExpenseVo.class);
            nonGroupExpenseVo = setNonGroupFieldsNull(nonGroupExpenseVo);
            if(finalList.indexOf(nonGroupExpenseVo) == -1)
            finalList.add(nonGroupExpenseVo);
        }
        return finalList;
    }
    public Response addNonGroupExpense(NonGroupExpenseVo nonGroupVo)
    {
        Response response = new Response();
        UserVo createdUser = getUserInfo(nonGroupVo.getCreatedBy());
        if (createdUser == null)
            return new Response("UserId " + nonGroupVo.getCreatedBy() + " , Not found");
        UserVo member = getUserInfo(nonGroupVo.getReceiverUser());
        if (member == null)
            return new Response( "UserId " + nonGroupVo.getReceiverUser() + " , Not found");
        if(nonGroupVo.getAmount() <= 0)
            return new Response("Amount can only be positive");

        double payAmount = ExpenseServiceImplementation.currencyExchange( nonGroupVo.getCurrencyType() , nonGroupVo.getAmount());
        nonGroupVo.setAmount(payAmount);
        nonGroupVo.setExpenseId(ExpenseServiceImplementation.generateUniqueId());
        nonGroupVo.setUpdatedBy(nonGroupVo.getCreatedBy());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
            nonGroupVo.setCreatedDate(dateWithoutTime);
            nonGroupVo.setUpdatedDate(dateWithoutTime);
        } catch (ParseException exception) {
            System.out.println(exception);
        }
        if(nonGroupVo.getCreatedBy() != nonGroupVo.getReceiverUser())
        {
            NotificationVo notificationVo = new NotificationVo();
            notificationVo.setUserId(nonGroupVo.getReceiverUser());
            notificationVo.setCreatedBy(nonGroupVo.getCreatedBy());
            notificationVo.setInformation("New Non Group Payment request from " + nonGroupVo.getCreatedBy());
            try {
                sendMessage(notificationVo);
            } catch (JsonProcessingException exception) {
                System.out.println(exception);
            }
        }
        else
            nonGroupVo.setPayed(true);
        NonGroupExpense nonGroupExpense = objectMapper.convertValue(nonGroupVo , NonGroupExpense.class);
        nonGroupExpense = nonGroupExpenseRepository.save(nonGroupExpense);
        return new Response(nonGroupExpense.getExpenseId() , "Added Successfully");
    }
    public Response payNonGroupExpense(String expenseId, long memberId) {
        UserVo member = getUserInfo(memberId);
        if (member == null) {
            return new Response("UserId: " + memberId + ", Not found");
        }
        Query query = new Query(Criteria.where("expenseId").is(expenseId));
        NonGroupExpense nonGroupExpense = mongoTemplate.findOne(query, NonGroupExpense.class);
        if (nonGroupExpense == null) {
            return new Response("ExpenseId: " + expenseId + ", Not found");
        }
        if (memberId != nonGroupExpense.getReceiverUser()) {
            return new Response("UserId: " + memberId + ", Not Valid");
        }
        Update update = new Update();
        update.set("payed", true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
            update.set("updatedDate", dateWithoutTime);
            update.set("updatedBy", memberId);
        } catch (ParseException exception) {
            System.out.println(exception);
        }
        NonGroupExpense updatedNonGroupExpense = mongoTemplate.findAndModify(query, update, NonGroupExpense.class);
        Response response = new Response();
        if (updatedNonGroupExpense != null) {
            response.setId(expenseId);
            if (updatedNonGroupExpense.isPayed())
                response.setStatus("Already Paid");
            else
                response.setStatus("Paid Successfully");
        }
        else {
            response.setStatus("ExpenseId : " + expenseId + " , Not Found");
        }
        return response;
    }
    public String pendingUserPaymentCheck(long userId , long memberId){
        List<NonGroupExpense> nonGroupExpenseList = nonGroupExpenseRepository.findByCreatedByAndReceiverUser(userId , memberId);
        for(NonGroupExpense nonGroupExpense : nonGroupExpenseList)
            if(!nonGroupExpense.isPayed())
                return "Payments pending";
        nonGroupExpenseList = nonGroupExpenseRepository.findByCreatedByAndReceiverUser(memberId , userId);
        for(NonGroupExpense nonGroupExpense : nonGroupExpenseList)
            if(!nonGroupExpense.isPayed())
                return "Payments pending";
        return "Payments done";
    }
    private NonGroupExpenseVo setNonGroupFieldsNull(NonGroupExpenseVo nonGroupExpenseVo){
        nonGroupExpenseVo.setUpdatedBy(0);
        nonGroupExpenseVo.setUpdatedDate(null);
        nonGroupExpenseVo.setCreatedDate(null);
        return nonGroupExpenseVo;
    }
}
