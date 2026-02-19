package com.example.demo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// DTOs keep input validation separate from domain models.
data class CommunityCreateRequest(
    @field:NotBlank
    @field:Size(max = 120)
    val name: String,
    @field:NotBlank
    @field:Size(max = 200)
    val address: String,
)

data class CommunityUpdateRequest(
    @field:NotBlank
    @field:Size(max = 120)
    val name: String,
    @field:NotBlank
    @field:Size(max = 200)
    val address: String,
)
