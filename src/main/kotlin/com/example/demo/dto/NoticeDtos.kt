package com.example.demo.dto

import com.example.demo.model.NoticeCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

// Notice input for create and update endpoints.
data class NoticeCreateRequest(
    @field:NotNull
    val communityId: Long,
    @field:NotNull
    val authorUserId: Long,
    @field:NotNull
    val category: NoticeCategory,
    @field:NotBlank
    @field:Size(max = 140)
    val title: String,
    @field:NotBlank
    @field:Size(max = 1000)
    val description: String,
)

data class NoticeUpdateRequest(
    @field:NotNull
    val category: NoticeCategory,
    @field:NotBlank
    @field:Size(max = 140)
    val title: String,
    @field:NotBlank
    @field:Size(max = 1000)
    val description: String,
)
