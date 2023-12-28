package com.example.notification_services.serviceImplementation;

import com.example.notification_services.dao.api.NotificationRepository;
import com.example.notification_services.model.entity.Notification;
import com.example.notification_services.model.vo.NotificationVo;
import com.example.notification_services.model.vo.Response;
import com.example.notification_services.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

@Service("notifyBean")
public class NotificationSerivceImplementation implements NotificationService {
    @Autowired
    NotificationRepository notifyRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "com.quinbay.group.create", groupId = "group-id")
    public void listenGroup(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NotificationVo notificationVo = objectMapper.readValue(message, NotificationVo.class);
        System.out.println("received: " + notificationVo.getInformation());
        addNotification(notificationVo);
    }
    @KafkaListener(topics = "com.quinbay.expense.create", groupId = "group-id")
    public void listenExpense(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NotificationVo notificationVo = objectMapper.readValue(message, NotificationVo.class);
        System.out.println("received: " + notificationVo.getInformation());
        addNotification(notificationVo);
    }
    public Response addNotification(NotificationVo notificationVo){
        Notification notification = objectMapper.convertValue(notificationVo , Notification.class);
        notification.setUpdatedBy(notificationVo.getCreatedBy());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateWithoutTime;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
            notification.setCreatedDate(dateWithoutTime);
            notification.setUpdatedDate(dateWithoutTime);
        } catch (ParseException exception) {
            System.out.println(exception);
        }
        notification = notifyRepository.save(notification);
        return new Response(notification.getNotificationId() , "Added Successfully");
    }
    public List<NotificationVo> notifyListByUserId(long userId){
        List<Notification> notificationList = notifyRepository.findAllByUserId(userId);
        List<NotificationVo> unreadMessage = new ArrayList<>();
        for(Notification notification : notificationList){
            if(!notification.isReadMessage()){
                Calendar calendar = Calendar.getInstance();
                NotificationVo notifyMsg = objectMapper.convertValue(notification , NotificationVo.class);
//                calendar.setTime(notification.getCreatedDate());
//                calendar.add(Calendar.DAY_OF_YEAR, 1);
//                Date increasedDate = calendar.getTime();
//                notifyMsg.setCreatedDate(increasedDate);
//
//                calendar.setTime(notification.getUpdatedDate());
//                calendar.add(Calendar.DAY_OF_YEAR, 1);
//                increasedDate = calendar.getTime();
//                notifyMsg.setUpdatedDate(increasedDate);
                notifyMsg = setNotifyFieldsNull(notifyMsg);
                unreadMessage.add(notifyMsg);
            }
        }
//        unreadMessage.sort(Comparator.comparing(NotificationVo::getCreatedDate).reversed());
        Collections.reverse(unreadMessage);
        return unreadMessage;
    }
    public Response notifyRead(long notifyId){
        Notification notification = notifyRepository.findByNotificationId(notifyId);
        notification.setReadMessage(true);
        notifyRepository.save(notification);
        return new Response(notifyId , "Message Read");
    }
    private NotificationVo setNotifyFieldsNull(NotificationVo notificationVo){
        notificationVo.setUpdatedBy(0);
        notificationVo.setUpdatedDate(null);
        return notificationVo;
    }
}
