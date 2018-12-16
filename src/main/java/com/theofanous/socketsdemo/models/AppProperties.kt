package com.theofanous.socketsdemo.models

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("app")
class AppProperties {
    var secret: String = "secret"
}