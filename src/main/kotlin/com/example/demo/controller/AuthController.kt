package com.example.demo.controller

import com.example.demo.dto.AuthResponse
import com.example.demo.dto.LoginRequest
import com.example.demo.security.JwtService
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
    private val jwtService: JwtService,
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse {
        val user = userService.login(request)
        val token = jwtService.generateToken(user.email)
        return AuthResponse(token = token)
    }
}
