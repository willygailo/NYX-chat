package com.nyx.chat.data.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val model: String,          // injected dynamically per provider
    val messages: List<Message>,
    val temperature: Double = 0.9,
    @SerializedName("max_tokens")
    val maxTokens: Int = 2048,
    val stream: Boolean = false
)

data class Message(
    val role: String,           // system | user | assistant
    val content: String
)
