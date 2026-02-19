package com.example.demo.repository

import com.example.demo.model.Notice
import com.example.demo.model.NoticeCategory
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {
    fun findByCommunityId(communityId: Long): List<Notice>
    fun findByCategory(category: NoticeCategory): List<Notice>
    fun findByCommunityIdAndCategory(communityId: Long, category: NoticeCategory): List<Notice>
}
