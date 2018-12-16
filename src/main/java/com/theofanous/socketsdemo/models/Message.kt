package com.theofanous.socketsdemo.models

import java.sql.Timestamp
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Int? = null
    var firstName = ""
    var lastName = ""
    var username = ""
    var message = ""
    var timestamp : Timestamp = Timestamp.from(Instant.now())
}