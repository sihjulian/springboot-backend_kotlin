package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmailIgnoreCase(email: String): Boolean
    fun findByEmailIgnoreCase(email: String): User?
    fun findByCommunityId(communityId: Long): List<User>
}
