package com.example.demo.controller

import com.example.demo.dto.CommunityCreateRequest
import com.example.demo.dto.CommunityUpdateRequest
import com.example.demo.model.Community
import com.example.demo.service.CommunityService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/communities")
class CommunityController(
    private val communityService: CommunityService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CommunityCreateRequest): Community =
        communityService.create(request)

    @GetMapping
    fun list(): List<Community> = communityService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Community = communityService.get(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CommunityUpdateRequest,
    ): Community = communityService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        communityService.delete(id)
    }
}
