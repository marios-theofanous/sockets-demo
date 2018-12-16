package com.theofanous.socketsdemo.services

import com.theofanous.socketsdemo.models.User
import com.theofanous.socketsdemo.repositories.UserRepository
import org.springframework.core.annotation.Order
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Order(1)
class UserDetailsServiceImpl(var userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
    }
}