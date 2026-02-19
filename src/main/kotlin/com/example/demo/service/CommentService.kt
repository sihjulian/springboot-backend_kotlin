package com.example.demo.service

import com.example.demo.dto.CommentCreateRequest
import com.example.demo.exception.NotFoundException
import com.example.demo.model.Comment
import com.example.demo.repository.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val noticeService: NoticeService,
    private val userService: UserService,
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun create(request: CommentCreateRequest): Comment {
        val notice = noticeService.get(request.noticeId)
        val author = userService.get(request.authorUserId)

        val comment = Comment(
            notice = notice,
            authorUser = author,
            message = request.message.trim(),
        )
        return commentRepository.save(comment)
    }

    fun list(noticeId: Long?): List<Comment> {
        val values = if (noticeId == null) {
            commentRepository.findAll()
        } else {
            commentRepository.findByNoticeId(noticeId)
        }
        return values.sortedBy { it.createdAt }
    }

    @Transactional
    fun delete(id: Long) {
        if (!commentRepository.existsById(id)) {
            throw NotFoundException("Comment $id not found")
        }
        commentRepository.deleteById(id)
    }
}
