package com.example.demo.service

import com.example.demo.dto.LoginRequest
import com.example.demo.dto.UserCreateRequest
import com.example.demo.dto.UserUpdateRequest
import com.example.demo.exception.BadRequestException
import com.example.demo.exception.NotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val communityService: CommunityService,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(request: UserCreateRequest): User {
        val community = communityService.get(request.communityId)

        val normalizedEmail = request.email.trim().lowercase()
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw BadRequestException("Email already registered")
        }

        val user = User(
            community = community,
            name = request.name.trim(),
            email = normalizedEmail,
            address = request.address.trim(),
        )
        return userRepository.save(user)
    }

    fun list(communityId: Long?): List<User> {
        val values = if (communityId == null) {
            userRepository.findAll()
        } else {
            userRepository.findByCommunityId(communityId)
        }
        return values.sortedBy { it.id }
    }

    fun get(id: Long): User = userRepository.findById(id)
        .orElseThrow { NotFoundException("User $id not found") }

    @Transactional
    fun update(id: Long, request: UserUpdateRequest): User {
        val existing = get(id)
        val normalizedEmail = request.email.trim().lowercase()
        val other = userRepository.findByEmailIgnoreCase(normalizedEmail)
        if (other != null && other.id != existing.id) {
            throw BadRequestException("Email already registered")
        }

        existing.name = request.name.trim()
        existing.email = normalizedEmail
        existing.address = request.address.trim()
        return userRepository.save(existing)
    }

    @Transactional
    fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw NotFoundException("User $id not found")
        }
        userRepository.deleteById(id)
    }

    fun login(request: LoginRequest): User {
        val normalizedEmail = request.email.trim().lowercase()
        val user = userRepository.findByEmailIgnoreCase(normalizedEmail)
            ?: throw BadRequestException("Invalid email/address")

        if (!user.address.equals(request.address.trim(), ignoreCase = true)) {
            throw BadRequestException("Invalid email/address")
        }
        return user
    }
}
