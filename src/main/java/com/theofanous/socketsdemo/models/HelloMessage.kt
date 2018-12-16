package com.theofanous.socketsdemo.models

class HelloMessage {
    var name: String

    constructor() {
        name = ""
    }
    constructor(name: String) {
        this.name = name
    }

}