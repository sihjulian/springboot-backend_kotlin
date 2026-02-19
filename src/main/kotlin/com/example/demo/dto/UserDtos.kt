package com.example.demo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

// User input for registration and updates.
data class UserCreateRequest(
    @field:NotNull
    val communityId: Long,
    @field:NotBlank
    @field:Size(max = 120)
    val name: String,
    @field:NotBlank
    @field:Email
    @field:Size(max = 120)
    val email: String,
    @field:NotBlank
    @field:Size(max = 200)
    val address: String,
)

data class UserUpdateRequest(
    @field:NotBlank
    @field:Size(max = 120)
    val name: String,
    @field:NotBlank
    @field:Email
    @field:Size(max = 120)
    val email: String,
    @field:NotBlank
    @field:Size(max = 200)
    val address: String,
)

data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val address: String,
)
