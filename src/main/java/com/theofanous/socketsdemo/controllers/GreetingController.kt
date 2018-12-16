package com.theofanous.socketsdemo.controllers

import com.theofanous.socketsdemo.models.Greeting
import com.theofanous.socketsdemo.models.HelloMessage
import com.theofanous.socketsdemo.models.User
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GreetingController(private val messagingTemplate: SimpMessagingTemplate) {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage, token: UsernamePasswordAuthenticationToken): Greeting {

        messagingTemplate.convertAndSendToUser("marios","/queue/reply", Greeting("Geeee"))

        val user = (token.principal as User)

        return Greeting("${Date()} ${user.firstName}: ${message.name}")
    }
}