package com.example.demo.repository

import com.example.demo.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByNoticeId(noticeId: Long): List<Comment>
}
