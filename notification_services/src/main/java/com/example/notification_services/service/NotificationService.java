package com.example.notification_services.service;

import com.example.notification_services.model.vo.NotificationVo;
import com.example.notification_services.model.vo.Response;

import java.util.List;

public interface NotificationService {
    Response addNotification(NotificationVo notificationVo);
    List<NotificationVo> notifyListByUserId(long userId);
    Response notifyRead(long notifyId);
}
