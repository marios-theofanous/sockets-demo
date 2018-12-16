package com.theofanous.socketsdemo.controllers

import com.theofanous.socketsdemo.models.Message
import com.theofanous.socketsdemo.repositories.MessageRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
class MessageController(private val messageRepository: MessageRepository) {

    @GetMapping
    fun getAllMessages(): List<Message> {
        return messageRepository.findAllByOrderByTimestampAsc()
    }
}