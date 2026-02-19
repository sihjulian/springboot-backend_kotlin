package com.example.demo.controller

import com.example.demo.dto.NoticeCreateRequest
import com.example.demo.dto.NoticeUpdateRequest
import com.example.demo.model.Notice
import com.example.demo.service.NoticeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notices")
class NoticeController(
    private val noticeService: NoticeService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: NoticeCreateRequest): Notice =
        noticeService.create(request)

    @GetMapping
    fun list(
        @RequestParam(required = false) communityId: Long?,
        @RequestParam(required = false) category: String?,
    ): List<Notice> = noticeService.list(communityId, category)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Notice = noticeService.get(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: NoticeUpdateRequest,
    ): Notice = noticeService.update(id, request)

    @PutMapping("/{id}/attended")
    fun markAttended(@PathVariable id: Long): Notice = noticeService.markAttended(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        noticeService.delete(id)
    }
}
