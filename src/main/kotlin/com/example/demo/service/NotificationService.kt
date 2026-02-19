package com.example.demo.service

import com.example.demo.model.Notice
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    // Simulates email/push notifications using logs.
    fun notifyNoticeCreated(notice: Notice) {
        logger.info("Notification: notice created id=${notice.id} title=${notice.title}")
    }

    fun notifyNoticeUpdated(notice: Notice) {
        logger.info("Notification: notice updated id=${notice.id} title=${notice.title}")
    }

    fun notifyNoticeAttended(notice: Notice) {
        logger.info("Notification: notice attended id=${notice.id} title=${notice.title}")
    }
}
