package com.theofanous.socketsdemo.repositories

import com.theofanous.socketsdemo.models.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Int> {
    fun findAllByOrderByTimestampAsc(): List<Message>
}