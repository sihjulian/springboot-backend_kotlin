package com.example.demo.controller

import com.example.demo.dto.UserCreateRequest
import com.example.demo.dto.UserUpdateRequest
import com.example.demo.model.User
import com.example.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserCreateRequest): User =
        userService.create(request)

    @GetMapping
    fun list(@RequestParam(required = false) communityId: Long?): List<User> =
        userService.list(communityId)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): User = userService.get(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest,
    ): User = userService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}
