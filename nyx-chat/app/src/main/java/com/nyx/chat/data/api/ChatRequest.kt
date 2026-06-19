package com.nyx.chat.data.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val model: String = "grok-4",
    val messages: List<Message>,
    val temperature: Double = 0.8,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1000
)

data class Message(
    val role: String,
    val content: String
)
