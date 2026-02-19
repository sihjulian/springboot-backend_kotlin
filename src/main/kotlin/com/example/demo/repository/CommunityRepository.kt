package com.example.demo.repository

import com.example.demo.model.Community
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository : JpaRepository<Community, Long>
