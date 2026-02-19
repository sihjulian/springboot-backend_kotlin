package com.example.demo.service

import com.example.demo.dto.NoticeCreateRequest
import com.example.demo.dto.NoticeUpdateRequest
import com.example.demo.exception.BadRequestException
import com.example.demo.exception.NotFoundException
import com.example.demo.model.Notice
import com.example.demo.model.NoticeCategory
import com.example.demo.model.NoticeStatus
import com.example.demo.repository.NoticeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(
    private val communityService: CommunityService,
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val noticeRepository: NoticeRepository,
) {
    @Transactional
    fun create(request: NoticeCreateRequest): Notice {
        val community = communityService.get(request.communityId)
        val author = userService.get(request.authorUserId)

        val notice = Notice(
            community = community,
            authorUser = author,
            category = request.category,
            title = request.title.trim(),
            description = request.description.trim(),
        )
        val saved = noticeRepository.save(notice)
        notificationService.notifyNoticeCreated(saved)
        return saved
    }

    fun list(communityId: Long?, category: String?): List<Notice> {
        val categoryEnum = category?.let { parseCategory(it) }
        val values = when {
            communityId != null && categoryEnum != null ->
                noticeRepository.findByCommunityIdAndCategory(communityId, categoryEnum)
            communityId != null ->
                noticeRepository.findByCommunityId(communityId)
            categoryEnum != null ->
                noticeRepository.findByCategory(categoryEnum)
            else ->
                noticeRepository.findAll()
        }
        return values.sortedByDescending { it.createdAt }
    }

    fun get(id: Long): Notice = noticeRepository.findById(id)
        .orElseThrow { NotFoundException("Notice $id not found") }

    @Transactional
    fun update(id: Long, request: NoticeUpdateRequest): Notice {
        val existing = get(id)
        existing.category = request.category
        existing.title = request.title.trim()
        existing.description = request.description.trim()
        val saved = noticeRepository.save(existing)
        notificationService.notifyNoticeUpdated(saved)
        return saved
    }

    @Transactional
    fun markAttended(id: Long): Notice {
        val existing = get(id)
        existing.status = NoticeStatus.ATTENDED
        val saved = noticeRepository.save(existing)
        notificationService.notifyNoticeAttended(saved)
        return saved
    }

    @Transactional
    fun delete(id: Long) {
        if (!noticeRepository.existsById(id)) {
            throw NotFoundException("Notice $id not found")
        }
        noticeRepository.deleteById(id)
    }

    private fun parseCategory(raw: String): NoticeCategory {
        return try {
            NoticeCategory.valueOf(raw.trim().uppercase())
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("Invalid category: $raw")
        }
    }
}
