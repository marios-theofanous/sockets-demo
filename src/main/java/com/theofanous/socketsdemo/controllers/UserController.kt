package com.theofanous.socketsdemo.controllers

import com.theofanous.socketsdemo.models.User
import com.theofanous.socketsdemo.repositories.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/user")
@RestController
class UserController(private val userRepository: UserRepository, private val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody user: User): String {
        if (!userRepository.existsByUsername(user.username)) {
            user.password = bCryptPasswordEncoder.encode(user.password)
            userRepository.save(user)
            return "OKAY"
        }
        return "ERROR"
    }

    @GetMapping
    fun getUser(token: UsernamePasswordAuthenticationToken): User {
        return (token.principal as User)
    }
}