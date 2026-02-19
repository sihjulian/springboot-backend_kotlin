package com.example.demo.controller

import com.example.demo.dto.CommentCreateRequest
import com.example.demo.model.Comment
import com.example.demo.service.CommentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CommentCreateRequest): Comment =
        commentService.create(request)

    @GetMapping
    fun list(@RequestParam(required = false) noticeId: Long?): List<Comment> =
        commentService.list(noticeId)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        commentService.delete(id)
    }
}
