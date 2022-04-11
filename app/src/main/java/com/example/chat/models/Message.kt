package com.example.chat.models

data class Message(
    val sentTo:String ="",
    val sentFrom:String = "",
    val msgText:String = "",
    val timeCreated:String = "")