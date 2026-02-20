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
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class UserService(
    private val communityService: CommunityService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
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
            passwordHash = passwordEncoder.encode(request.password),
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
        if (!request.password.isNullOrBlank()) {
            existing.passwordHash = passwordEncoder.encode(request.password)
        }
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

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw BadRequestException("Invalid email/password")
        }
        return user
    }
}
