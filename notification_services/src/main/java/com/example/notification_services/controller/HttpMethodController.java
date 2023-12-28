package com.example.notification_services.controller;

import com.example.notification_services.model.vo.NotificationVo;
import com.example.notification_services.model.vo.Response;
import com.example.notification_services.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/httpmethod")
public class HttpMethodController {
    @Autowired
    NotificationService notifyBean;
    @PostMapping("/add/notification")
    public Response addNewNotification(@RequestBody NotificationVo notificationVo) {
        return notifyBean.addNotification(notificationVo);
    }
    @GetMapping("/notify-list/{userId}")
    public List<NotificationVo> notificationList(@PathVariable long userId){
        return notifyBean.notifyListByUserId(userId);
    }
    @PutMapping("/notify-read/{notifyId}")
    public Response notificationRead(@PathVariable long notifyId){
        return notifyBean.notifyRead(notifyId);
    }
}
