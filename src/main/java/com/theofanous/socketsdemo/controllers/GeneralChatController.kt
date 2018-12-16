package com.theofanous.socketsdemo.controllers

import com.theofanous.socketsdemo.models.Message
import com.theofanous.socketsdemo.models.User
import com.theofanous.socketsdemo.repositories.MessageRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RestController

@RestController
@MessageMapping("/general")
class GeneralChatController(private val messageRepository: MessageRepository) {

    @MessageMapping("/chat")
    @SendTo("/topic/general")
    fun generalChat(message: Message, token: UsernamePasswordAuthenticationToken): Message {
        val user = (token.principal as User)
        message.username = user.username
        message.firstName = user.firstName
        message.lastName = user.lastName
        val savedMessage = messageRepository.save(message)
        return savedMessage
    }
}