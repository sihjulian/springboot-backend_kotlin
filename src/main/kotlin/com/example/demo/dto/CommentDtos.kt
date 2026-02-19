package com.example.demo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

// Comment input for notice discussions.
data class CommentCreateRequest(
    @field:NotNull
    val noticeId: Long,
    @field:NotNull
    val authorUserId: Long,
    @field:NotBlank
    @field:Size(max = 600)
    val message: String,
)
