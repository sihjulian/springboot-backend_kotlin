package com.example.demo.controller

import com.example.demo.dto.LoginRequest
import com.example.demo.model.User
import com.example.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): User =
        userService.login(request)
}
