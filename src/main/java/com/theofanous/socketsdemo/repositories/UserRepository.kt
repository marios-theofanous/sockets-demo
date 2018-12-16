package com.theofanous.socketsdemo.repositories

import com.theofanous.socketsdemo.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}
