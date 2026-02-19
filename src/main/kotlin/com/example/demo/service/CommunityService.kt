package com.example.demo.service

import com.example.demo.dto.CommunityCreateRequest
import com.example.demo.dto.CommunityUpdateRequest
import com.example.demo.exception.NotFoundException
import com.example.demo.model.Community
import com.example.demo.repository.CommunityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommunityService(
    private val communityRepository: CommunityRepository,
) {
    @Transactional
    fun create(request: CommunityCreateRequest): Community {
        val community = Community(
            name = request.name.trim(),
            address = request.address.trim(),
        )
        return communityRepository.save(community)
    }

    fun list(): List<Community> = communityRepository.findAll().sortedBy { it.id }

    fun get(id: Long): Community = communityRepository.findById(id)
        .orElseThrow { NotFoundException("Community $id not found") }

    @Transactional
    fun update(id: Long, request: CommunityUpdateRequest): Community {
        val existing = get(id)
        existing.name = request.name.trim()
        existing.address = request.address.trim()
        return communityRepository.save(existing)
    }

    @Transactional
    fun delete(id: Long) {
        if (!communityRepository.existsById(id)) {
            throw NotFoundException("Community $id not found")
        }
        communityRepository.deleteById(id)
    }
}
